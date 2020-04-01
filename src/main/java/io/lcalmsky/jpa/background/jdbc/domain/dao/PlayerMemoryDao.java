package io.lcalmsky.jpa.background.jdbc.domain.dao;

import io.lcalmsky.jpa.background.jdbc.domain.dto.Player;

import java.util.Map;

public class PlayerMemoryDao implements PlayerDao {
    private final Map<Long, Player> internalMap;

    public PlayerMemoryDao(Map<Long, Player> internalMap) {
        this.internalMap = internalMap;
    }


    @Override
    public Player findPlayerById(Long id) {
        return internalMap.get(id);
    }

    @Override
    public void savePlayer(Player player) {
        internalMap.put(player.getId(), player);
    }
}
