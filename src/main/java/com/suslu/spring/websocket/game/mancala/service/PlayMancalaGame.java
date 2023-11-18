package com.suslu.spring.websocket.game.mancala.service;

import com.suslu.spring.websocket.game.mancala.config.MancalaConstants;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayMancalaGame {

    public void sowStones(MancalaGame game, int pitIndex) {
        // validate before sow
        checkGameStateToSow(game);
        checkPit(game, pitIndex);

        // distribute stones
        int lastSowedPitIndex = distributeStones(game, pitIndex);

        // switch player if required
        switchPlayerIfRequired(game, lastSowedPitIndex);

        // TODO
        // check If game over
        // end game
    }

    private void checkGameStateToSow(MancalaGame game) {
        if (!GameState.ACTIVE.equals(game.getGameState())) {
            throw new MancalaRuntimeException("Game is not active. gameId: " + game.getGameId());
        }
    }

    private void checkPit(MancalaGame game, int pitIndex) {
        if (MancalaConstants.PIT_COUNT <= pitIndex) {
            throw new MancalaRuntimeException("Invalid pitIndex: " + pitIndex);
        }
        if ((MancalaConstants.BIG_PIT_INDEX_1 == pitIndex || MancalaConstants.BIG_PIT_INDEX_2 == pitIndex)) {
            throw new MancalaRuntimeException("Big pits can not be selected to move stones.");
        }
        if ((PlayerType.PLAYER_1.equals(game.getCurrentPlayer()) && pitIndex >= MancalaConstants.BIG_PIT_INDEX_1) ||
                (PlayerType.PLAYER_2.equals(game.getCurrentPlayer()) && pitIndex <= MancalaConstants.BIG_PIT_INDEX_1)) {
            throw new MancalaRuntimeException("Player can not move stones in the opponent's pits.");
        }
        List<Integer> pits = game.getPits();
        if(pits.get(pitIndex) == 0) {
            throw new MancalaRuntimeException("There is no stone in the selected pit.");
        }
    }

    private int distributeStones(MancalaGame game, int pitIndex) {
        List<Integer> pits = game.getPits();
        int opponentsBigPitIndex = game.opponentsBigPitIndex();
        int lastSowedPitIndex = pitIndex;   // So returns the given pit if there is no stones
        int stoneCount = pits.get(pitIndex);
        pits.set(pitIndex, 0);

        int currentIndex = pitIndex +1;
        while(stoneCount > 0) {
            for (int i = currentIndex; i < pits.size(); i++) {
                if( opponentsBigPitIndex != i) {
                    pits.set(i, pits.get(i)+1);
                    lastSowedPitIndex = i;
                    stoneCount--;
                    if(stoneCount == 0) {
                        break;
                    }
                }
            }
            currentIndex = 0;
        }

        captureStonesIfAvailable(game, lastSowedPitIndex);

        return lastSowedPitIndex;
    }

    private void captureStonesIfAvailable(MancalaGame game, int lastSowedPitIndex) {
        List<Integer> pits = game.getPits();

        if(MancalaConstants.BIG_PIT_INDEX_1 != lastSowedPitIndex &&
                MancalaConstants.BIG_PIT_INDEX_2 != lastSowedPitIndex &&
                pits.get(lastSowedPitIndex) == 1 &&
                isCurrentPlayersPitIndex(game.getCurrentPlayer(), lastSowedPitIndex)
        ) {

            int currentPlayersBigPitIndex = game.currentPlayersBigPitIndex();
            int oppositeIndexToCapture = MancalaConstants.OPPOSITE_INDEX[lastSowedPitIndex];

            int newCountOfBigPit = pits.get(currentPlayersBigPitIndex) + pits.get(oppositeIndexToCapture) + 1;

            pits.set(lastSowedPitIndex, 0);
            pits.set(oppositeIndexToCapture, 0);
            pits.set(currentPlayersBigPitIndex, newCountOfBigPit);
        }
    }

    private boolean isCurrentPlayersPitIndex(PlayerType currentPlayer, int pitIndex) {
        return
                (PlayerType.PLAYER_1.equals(currentPlayer) && pitIndex < MancalaConstants.BIG_PIT_INDEX_1) ||
                (PlayerType.PLAYER_2.equals(currentPlayer) && pitIndex > MancalaConstants.BIG_PIT_INDEX_1 &&
                        pitIndex != MancalaConstants.BIG_PIT_INDEX_2);
    }

    public void switchPlayerIfRequired(MancalaGame game, int lastSowedPitIndex) {
        int currentPlayersBigPitIndex = game.currentPlayersBigPitIndex();

        if(currentPlayersBigPitIndex != lastSowedPitIndex) {
            if (PlayerType.PLAYER_1 == game.getCurrentPlayer()) {
                game.setCurrentPlayer(PlayerType.PLAYER_2);
            } else {
                game.setCurrentPlayer(PlayerType.PLAYER_1);
            }
        }
    }
}
