package com.suslu.spring.websocket.game.mancala.helper;

import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;

import java.util.List;

public class MancalaGameHelper {
    private MancalaGame game;

    public MancalaGameHelper(MancalaGame game) {
        this.game = game;
    }

    public MancalaGame getGame() {
        return game;
    }

    /**
     * returns PlayerType of opponent
     */
    public PlayerType getOpponentPlayer() {
        return PlayerType.PLAYER_1.equals(game.getCurrentPlayer()) ?
                PlayerType.PLAYER_2 : PlayerType.PLAYER_1;
    }

    /**
     * returns Big Pit index of current player
     */
    public int getCurrentPlayersBigPitIndex() {
        return MancalaConstants.getBigPitIndexOf(game.getCurrentPlayer());
    }

    /**
     * returns Big Pit index of opponent
     */
    public int getOpponentsBigPitIndex() {
        return MancalaConstants.getBigPitIndexOf(getOpponentPlayer());
    }

    /**
     * calculates stone count of current player in the pits (bigPit not included)
     * returns int stone count
     */
    public int getCurrentPlayersRemainingStoneCount() {
        return getPlayersRemainingStoneCount(game.getCurrentPlayer());
    }

    /**
     * calculates stone count for given player in the pits (bigPit not included)
     * returns int stone count
     */
    private int getPlayersRemainingStoneCount(PlayerType playerType) {
        int stoneCount = 0;
        int[] indexes = MancalaConstants.getPitIndexIntervalOf(playerType);
        for (int i = indexes[0]; i <= indexes[1]; i++) {
            stoneCount += game.getPits().get(i);
        }
        return stoneCount;
    }

    /**
     * returns true if given index is one of the pit indexes of current player
     */
    public boolean isCurrentPlayersPitIndex(int index) {
        return MancalaConstants.isPlayersPitIndex(game.getCurrentPlayer(), index);
    }

    /**
     * collect opponent's remaining stones and
     * move to opponent's big pit
     */
    public void moveOpponentsRemainingStonesToBigPit() {
        movePlayersRemainingStonesToBigPit( getOpponentPlayer() );
    }

    /**
     * collect player's remaining stones and
     * move to player's big pit
     */
    private void movePlayersRemainingStonesToBigPit(PlayerType playerType) {
        int bigPitIndex = MancalaConstants.getBigPitIndexOf(playerType);
        int[] indexes = MancalaConstants.getPitIndexIntervalOf(playerType);
        List<Integer> pits = game.getPits();

        int bigPitCount = pits.get(bigPitIndex);
        for (int i = indexes[0]; i <= indexes[1]; i++) {
            bigPitCount += pits.get(i);
            pits.set(i, 0);
        }
        pits.set(bigPitIndex, bigPitCount);
    }

    /**
     * returns stoneBig Pit index for given playerType
     */
    public int getStoneCountInPlayersBigPit(PlayerType playerType) {
        int bigPitIndex = MancalaConstants.getBigPitIndexOf(playerType);
        return game.getPits().get(bigPitIndex);
    }

    /**
     * Switches currentPlayer
     * PLAYER_1 to PLAYER_2  OR  PLAYER_2 to PLAYER_1
     */
    public void switchCurrentPlayer() {
        if (PlayerType.PLAYER_1.equals(game.getCurrentPlayer())) {
            game.setCurrentPlayer(PlayerType.PLAYER_2);
        } else {
            game.setCurrentPlayer(PlayerType.PLAYER_1);
        }
    }

    /**
     * decide who the winner is and update game as finished
     */
    public void finishGame() {
        PlayerType opponent = getOpponentPlayer();
        // get players' stone counts in big pits
        int currentPlayersBigPitCount = getStoneCountInPlayersBigPit(game.getCurrentPlayer());
        int opponentsBigPitCount = getStoneCountInPlayersBigPit(opponent);

        PlayerType winner = null;
        if(currentPlayersBigPitCount > opponentsBigPitCount) {
            winner = game.getCurrentPlayer();
        } else if(currentPlayersBigPitCount < opponentsBigPitCount) {
            winner = opponent;
        } // equals stone count means game is even. No winner.

        game.setWinnerPlayer(winner);
        game.setGameState(GameState.FINISHED);
    }

}
