package com.suslu.spring.websocket.game.mancala.model.response;

import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.model.Player;

import java.util.List;

public class MancalaGameResponse {
    private String response;
    private String gameId;
    private Player player1;
    private Player player2;
    private GameState gameState;
    private PlayerType currentPlayer;
    private boolean creatorOfTheGame;
    private List<Integer> pits;
    private PlayerType winnerPlayer;

    public MancalaGameResponse(){}

    public MancalaGameResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public PlayerType getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerType currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isCreatorOfTheGame() {
        return creatorOfTheGame;
    }

    public void setCreatorOfTheGame(boolean creatorOfTheGame) {
        this.creatorOfTheGame = creatorOfTheGame;
    }

    public List<Integer> getPits() {
        return pits;
    }

    public void setPits(List<Integer> pits) {
        this.pits = pits;
    }

    public PlayerType getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(PlayerType winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }
}
