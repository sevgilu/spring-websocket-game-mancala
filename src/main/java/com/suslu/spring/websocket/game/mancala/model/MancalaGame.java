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
    private PlayerType winnerPlayer;

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


    /**
     * @return PlayerType of opponent
     */
    public PlayerType opponentsPlayerType() {
        return PlayerType.PLAYER_1.equals(currentPlayer) ?
                PlayerType.PLAYER_2 : PlayerType.PLAYER_1;
    }

    /**
     * @return int index of opponent's big pit
     */
    public int opponentsBigPitIndex() {
        return PlayerType.PLAYER_1.equals(currentPlayer) ?
                MancalaConstants.BIG_PIT_INDEX_2 : MancalaConstants.BIG_PIT_INDEX_1;
    }

    /**
     * @return int index of current player's big pit
     */
    public int currentPlayersBigPitIndex() {
        return PlayerType.PLAYER_1.equals(currentPlayer) ?
                MancalaConstants.BIG_PIT_INDEX_1 : MancalaConstants.BIG_PIT_INDEX_2;
    }

    /**
     * calculates stone count of current player in the pits (bigPit not included)
     * @return int stone count
     */
    public int currentPlayersRemainingStoneCount() {
        int stoneCount = 0;
        int[] indexes = pitIndexIntervalForCurrentPlayer();
        for (int i = indexes[0]; i <= indexes[1]; i++) {
            stoneCount += pits.get(i);
        }
        return stoneCount;
    }

    /**
     * sets begin and end index of the pits of the CURRENT PLAYER
     * both are included indexes
     * @return an int array with two elements.
     */
    public int[] pitIndexIntervalForCurrentPlayer() {
        int[] indexes = new int[2];
        if( PlayerType.PLAYER_1.equals(currentPlayer) ) {
            // CurrentPlayer is Player1
            indexes[0] = 0;
            indexes[1] = MancalaConstants.BIG_PIT_INDEX_1 - 1;
        } else {
            // CurrentPlayer is Player2
            indexes[0] = MancalaConstants.BIG_PIT_INDEX_1 + 1;
            indexes[1] = MancalaConstants.BIG_PIT_INDEX_2 - 1;
        }
        return indexes;
    }

    /**
     * sets begin and end index of the pits of the OPPONENT
     * both are included indexes
     * @return an int array with two elements.
     */
    public int[] pitIndexIntervalForOpponent() {
        int[] indexes = new int[2];

        if( PlayerType.PLAYER_1.equals(currentPlayer) ) {
            // Meaning opponent is Player2.Returning pit index interval for Player2.
            indexes[0] = MancalaConstants.BIG_PIT_INDEX_1 + 1;
            indexes[1] = MancalaConstants.BIG_PIT_INDEX_2 - 1;
        } else {
            // else currentPlayer is Player2.
            // Meaning opponent is Player1.Returning pit index interval for Player1.
            indexes[0] = 0;
            indexes[1] = MancalaConstants.BIG_PIT_INDEX_1 - 1;
        }
        return indexes;
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
