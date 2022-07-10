## 고아 객체란?

부모 엔터티와 연관관계가 끊어진 자식 엔터티를 말합니다.

잔인하게도 JPA에서는 고아가 된 객체를 보육원에 맡기는 게 아니라 흔적도 없이 제거해 버릴 수 있습니다.

## 동작 방식

매핑 애너테이션의 속성 중 `orphanRemoval`을 `true`로 지정하면 됩니다.

```java
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Parent {

  @Id
  @GeneratedValue
  @Column(name = "parent_id")
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
  private List<Child> children = new ArrayList<>();
}
```

이런식으로 `Parent`가 `Child`를 가지고 있을 때

```java
Parent parent = parentRepository.findById(PARENT_ID)
    .orElseThrow(IllegalArgumentException::new);
parent.getChildren().remove(0);
```

이렇게 첫 번째 `Child`를 제거하게 되면 `Child` 엔터티를 삭제하는 쿼리가 자동으로 발생합니다.

## 테스트

Parent, Child 객체를 생성한 뒤 연관관계를 맺어주고, Parent 객체에서 Child 객체를 제거해보겠습니다.

```java
package com.tistory.jaimenote.jpa.orphan.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Parent {

  @Id
  @GeneratedValue
  @Column(name = "parent_id")
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Child> children = new ArrayList<>();

  public static Parent createParent() {
    return new Parent();
  }

  public void takeAsChild(Child child) {
    children.add(child);
    child.serveAsParent(this);
  }
}
```

```java
package com.tistory.jaimenote.jpa.orphan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Child {

  @Id
  @GeneratedValue
  @Column(name = "child_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Parent parent;

  public static Child createChild() {
    return new Child();
  }

  public void serveAsParent(Parent parent) {
    this.parent = parent;
  }
}
```

```java
package com.tistory.jaimenote.jpa.orphan.infra.repository;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.tistory.jaimenote.jpa.orphan.entity.Child;
import com.tistory.jaimenote.jpa.orphan.entity.Parent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class ParentRepositoryTest {

  private static final Long PARENT_ID = 1L;
  @Autowired
  ParentRepository parentRepository;
  @Autowired
  ChildRepository childRepository;
  @PersistenceContext
  EntityManager entityManager;

  @Test
  @Rollback(false)
  void removeOrphanTest() {
    Child child = Child.createChild();
    childRepository.save(child);

    Parent parent = Parent.createParent();
    parent.takeAsChild(child);
    parentRepository.save(parent);

    entityManager.flush();
    entityManager.clear();

    Parent foundParent = parentRepository.findById(parent.getId())
        .orElseThrow(IllegalAccessError::new);
    foundParent.getChildren().remove(0);

    entityManager.flush();
    entityManager.clear();

    Child foundChild = childRepository.findById(child.getId()).orElse(null);
    assertNull(foundChild);
  }
}
```

중간중간 `update`가 일어난 이후 `entityManager`를 이용해 `flush`, `clear`를 호출해주었습니다.

테스트를 실행해보면 성공하는 것을 확인할 수 있고 더 자세히 로그를 추적해보면,

(1) child 객체 저장

```text
Hibernate: 
    insert 
    into
        child
        (parent_id, child_id) 
    values
        (?, ?)
2022-07-10 21:25:55.633 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [null]
2022-07-10 21:25:55.634 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [1]
```

(2) parent 객체 저장

```text
Hibernate: 
    insert 
    into
        parent
        (parent_id) 
    values
        (?)
2022-07-10 21:25:55.636 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
```

(3) child에 parent 객체 참조 업데이트

```text
Hibernate: 
    update
        child 
    set
        parent_id=? 
    where
        child_id=?
2022-07-10 21:25:55.638 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
2022-07-10 21:25:55.638 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [1]
```

(4) parent 객체 조회

```text
Hibernate: 
    select
        parent0_.parent_id as parent_i1_9_0_ 
    from
        parent parent0_ 
    where
        parent0_.parent_id=?
2022-07-10 21:25:55.656 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
```

(5) children 조회 (parent.getChildren())

```text
Hibernate: 
    select
        children0_.parent_id as parent_i2_2_0_,
        children0_.child_id as child_id1_2_0_,
        children0_.child_id as child_id1_2_1_,
        children0_.parent_id as parent_i2_2_1_ 
    from
        child children0_ 
    where
        children0_.parent_id=?
2022-07-10 21:25:55.667 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
```

(6) child 삭제

```text
Hibernate: 
    delete 
    from
        child 
    where
        child_id=?
2022-07-10 21:25:55.672 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [1]
```

(7) child 조회

```text
Hibernate: 
    select
        child0_.child_id as child_id1_2_0_,
        child0_.parent_id as parent_i2_2_0_,
        parent1_.parent_id as parent_i1_9_1_ 
    from
        child child0_ 
    left outer join
        parent parent1_ 
            on child0_.parent_id=parent1_.parent_id 
    where
        child0_.child_id=?
2022-07-10 21:25:55.675 TRACE 19300 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [1]
```

이렇게 순차적으로 발생한 것을 확인할 수 있습니다.

`childRepository`를 이용해 무언가를 삭제하지 않았음에도 `parent` 객체와의 연관이 끊어지자 `delete` 쿼리가 호출되는 것을 (6)에서 확인할 수 있습니다.

## 주의점

참조가 제거되는 순간 고아 객체로 간주하고 삭제하기 때문에 참조하는 곳이 한 군데일 때만 사용해야 합니다.

`@OneToOne`과 `@OneToMany`에서만 사용 가능합니다.

`CascadeType.REMOVE`와 동일하게 동작합니다.

CascadeType.ALL과 함께 사용할 경우 부모 엔터티만 조작하여 자식의 생명주기를 관리할 수 있습니다.

따라서 그런 특수한 조건일 경우에한해 유용하게 사용할 수 있는 옵션입니다.