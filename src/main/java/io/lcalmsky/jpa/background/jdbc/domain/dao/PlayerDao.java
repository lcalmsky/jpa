package io.lcalmsky.jpa.background.jdbc.domain.dao;

import io.lcalmsky.jpa.background.jdbc.domain.dto.Player;

public interface PlayerDao {
    Player findPlayerById(Long id);

    void savePlayer(Player player);
}