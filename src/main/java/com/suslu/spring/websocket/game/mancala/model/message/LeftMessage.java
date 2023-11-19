package com.suslu.spring.websocket.game.mancala.model.message;

import com.suslu.spring.websocket.game.mancala.enums.PlayerType;

public class LeftMessage {
    private String gameId;
    private PlayerType senderPlayer;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public PlayerType getSenderPlayer() {
        return senderPlayer;
    }

    public void setSenderPlayer(PlayerType senderPlayer) {
        this.senderPlayer = senderPlayer;
    }

}
