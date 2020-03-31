package io.lcalmsky.jpa.jdbc.domain.model.dao;

import io.lcalmsky.jpa.jdbc.domain.model.dto.Player;

public interface PlayerDao {
    Player findPlayerById(Long id);

    void savePlayer(Player player);
}