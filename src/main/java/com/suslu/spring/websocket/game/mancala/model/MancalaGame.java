package com.suslu.spring.websocket.game.mancala.model;

import com.suslu.spring.websocket.game.mancala.config.MancalaConstants;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;

import java.util.ArrayList;
import java.util.List;

public class MancalaGame {
    private String gameId;
    private Player player1;
    private Player player2;
    private GameState gameState;
    private PlayerType currentPlayer;
    private boolean creatorOfTheGame;
    private List<Integer> pits;

    public MancalaGame(){}

    public MancalaGame(String gameId, Player player1) {
        this.gameId = gameId;
        this.player1 = player1;
        this.gameState = GameState.WAITING_FOR_PLAYER_2;
        this.currentPlayer = PlayerType.PLAYER_1;
        this.creatorOfTheGame = true;
        initializePits(MancalaConstants.STONE_COUNT);
    }

    private void initializePits(int stoneCount) {
        pits = new ArrayList<>();

        for (int i = 0; i < MancalaConstants.PIT_COUNT; i++) {
            if(i == MancalaConstants.BIG_PIT_INDEX_1 || i == MancalaConstants.BIG_PIT_INDEX_2) {
                pits.add(0);
            } else {
                pits.add(stoneCount);
            }
        }
    }

    public int opponentsBigPitIndex() {
        return PlayerType.PLAYER_1.equals(currentPlayer) ?
                MancalaConstants.BIG_PIT_INDEX_2 : MancalaConstants.BIG_PIT_INDEX_1;
    }

    public int currentPlayersBigPitIndex() {
        return PlayerType.PLAYER_1.equals(currentPlayer) ?
                MancalaConstants.BIG_PIT_INDEX_1 : MancalaConstants.BIG_PIT_INDEX_2;
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

}
