package com.suslu.spring.websocket.game.mancala.model.message;

import com.suslu.spring.websocket.game.mancala.enums.PlayerType;

public class MoveMessage {
    private String gameId;
    private PlayerType senderPlayer;
    private int pitIndex;

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

    public int getPitIndex() {
        return pitIndex;
    }

    public void setPitIndex(int pitIndex) {
        this.pitIndex = pitIndex;
    }
}
