## Overview

영속성 전이(CASCADE)에 대해 알아봅니다.

## 영속성 전이란?

`Entity`를 `persist` 상태로 만들 때 연관된 `Entity`를 같이 `persist` 상태로 만드는 것을 말합니다.

예를 들면 부모-자식 관계의 `Entity`가 같이 저장되어야 할 때 둘을 각각 저장하는 것이 아니라 영속성 전이 상태를 만들어 하나의 `Entity`만 저장하더라도 같이 저장되게 할 수 있습니다. 

## 사용법

연관관계 매핑시 `cascade` 속성을 지정할 수 있습니다.

```
@OneToMany(mappedBy="person", cascade=CascadeType.ALL)
private List<Address> addresses
```

이 경우 하나의 `person`만 저장되더라도 매핑되는 모든 `children`이 같이 저장되게 됩니다.

## CASCADE 종류

### CascadeType.ALL

하위 `entity`까지 모든 작업을 전파합니다.

```java
@Entity
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
  private List<Address> addresses;
}
```

```java
@Entity
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String street;
  private int houseNumber;
  private String city;
  private int zipCode;
  @ManyToOne(fetch = FetchType.LAZY)
  private Person person;
}
```

`person` `entity`에 행해지는 모든 작업에 대해 `address` `entity`가 영향을 받게 됩니다.

### CascadeType.PERSIST

하위 `entity`까지 영속성을 전달합니다.

```java
@Test
public void whenParentSavedThenChildSaved() {
  Person person = new Person();
  Address address = new Address();
  address.setPerson(person);
  person.setAddresses(Arrays.asList(address));
  session.persist(person);
  session.flush();
  session.clear();
}
```

```text
Hibernate: insert into Person (name, id) values (?, ?)
Hibernate: insert into Address (city, houseNumber, person_id, street, zipCode, id) values (?, ?, ?, ?, ?, ?)
```

`person`만 `persist` 했지만 `address` 까지 저장된 것을 확인할 수 있습니다.


### CascadeType.MERGE

하위 `entity`까지 삭제 작업을 지속합니다.

```java
@Test
public void whenParentSavedThenMerged() {
  int addressId;
  Person person = buildPerson("devender");
  Address address = buildAddress(person);
  person.setAddresses(Arrays.asList(address));
  session.persist(person);
  session.flush();
  addressId = address.getId();
  session.clear();
  Address savedAddressEntity = session.find(Address.class, addressId);
  Person savedPersonEntity = savedAddressEntity.getPerson();
  savedPersonEntity.setName("devender kumar");
  savedAddressEntity.setHouseNumber(24);
  session.merge(savedPersonEntity);
  session.flush();
}
```

```text
Hibernate: select address0_.id as id1_0_0_, address0_.city as city2_0_0_, address0_.houseNumber as houseNum3_0_0_, address0_.person_id as person_i6_0_0_, address0_.street as street4_0_0_, address0_.zipCode as zipCode5_0_0_ from Address address0_ where address0_.id=?
Hibernate: select person0_.id as id1_1_0_, person0_.name as name2_1_0_ from Person person0_ where person0_.id=?
Hibernate: update Address set city=?, houseNumber=?, person_id=?, street=?, zipCode=? where id=?
Hibernate: update Person set name=? where id=?
```

`address`와 `person`을 모두 `select` 한 뒤 `update` 한 것을 확인할 수 있습니다.

### CascadeType.REMOVE

하위 `entity`까지 `merge` 작업을 지속합니다.

```java
@Test
public void whenParentRemovedThenChildRemoved() {
  int personId;
  Person person = buildPerson("devender");
  Address address = buildAddress(person);
  person.setAddresses(Arrays.asList(address));
  session.persist(person);
  session.flush();
  personId = person.getId();
  session.clear();
  Person savedPersonEntity = session.find(Person.class, personId);
  session.remove(savedPersonEntity);
  session.flush();
}
```

```text
Hibernate: delete from Address where id=?
Hibernate: delete from Person where id=?
```

`person`만 삭제해도 `address`까지 같이 삭제되는 것을 확인할 수 있습니다.

### CascadeType.REFRESH

하위 `entity`까지 DB로부터 값을 다시 읽어옵니다.

### CascadeType.DETACH

하위 `entity`까지 영속성을 제거합니다.

```java
@Test
public void whenParentDetachedThenChildDetached() {
  Person person = buildPerson("devender");
  Address address = buildAddress(person);
  person.setAddresses(Arrays.asList(address));
  session.persist(person);
  session.flush();
  assertThat(session.contains(person)).isTrue();
  assertThat(session.contains(address)).isTrue();
  session.detach(person);
  assertThat(session.contains(person)).isFalse();
  assertThat(session.contains(address)).isFalse();
}
```

`person`만 `detach` 시켜도 `address`도 `detach` 되는 것을 확인할 수 있습니다.

이 밖에도 `LOCK`, `REPLICATE`, `SAVE_UPDATE` 타입이 있습니다.

## 주의점

영속성 전이를 설정하는 것은 연관관계를 매핑하는 것과 아무 관련이 없습니다.

`entity`를 다룰 때 연관 `entity`를 편하게 조작할 수 있는 방법을 제공하는 것이므로 잘못 사용하면 기대하지 않은 결과가 나타날 수 있습니다.

---

![참조: baeldung](https://www.baeldung.com/jpa-cascade-types)