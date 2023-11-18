package com.suslu.spring.websocket.game.mancala.service;

import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.Player;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;

public interface MancalaRegistryService {
    MancalaGame getActiveMancalaGame(String gameId);

    MancalaGameResponse joinGame(Player player);
}
