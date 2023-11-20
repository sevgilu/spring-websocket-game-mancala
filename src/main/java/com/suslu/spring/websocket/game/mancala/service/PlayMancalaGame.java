package com.suslu.spring.websocket.game.mancala.service;

import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.helper.MancalaConstants;
import com.suslu.spring.websocket.game.mancala.helper.MancalaGameHelper;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayMancalaGame {

    public void sowStones(MancalaGame game, int pitIndex) {
        // validate before sow
        validate(game, pitIndex);

        MancalaGameHelper gameHelper = new MancalaGameHelper(game);

        int lastSowedPitIndex = sowStonesNow(gameHelper, pitIndex);

        controlIfGameIsOverAndFinish(gameHelper);

        if(GameState.ACTIVE.equals(game.getGameState())) {
            switchPlayerIfRequired(gameHelper, lastSowedPitIndex);
        }
    }

    private void validate(MancalaGame game, int pitIndex) {
        checkGameStateToSow(game);
        checkPit(game, pitIndex);
    }

    private void checkGameStateToSow(MancalaGame game) {
        if (!GameState.ACTIVE.equals(game.getGameState())) {
            throw new MancalaRuntimeException("Game is not active. gameId: " + game.getGameId());
        }
    }

    private void checkPit(MancalaGame game, int pitIndex) {
        if (MancalaConstants.TOTAL_PIT_COUNT <= pitIndex) {
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

    private int sowStonesNow(MancalaGameHelper gameHelper, int pitIndex) {
        List<Integer> pits = gameHelper.getGame().getPits();
        int opponentsBigPitIndex = gameHelper.getOpponentsBigPitIndex();
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
        captureStonesIfAvailable(gameHelper, lastSowedPitIndex);
        return lastSowedPitIndex;
    }

    private void captureStonesIfAvailable(MancalaGameHelper gameHelper, int lastSowedPitIndex) {
        List<Integer> pits = gameHelper.getGame().getPits();

        if( ! MancalaConstants.isBigPitIndex(lastSowedPitIndex)  &&
                gameHelper.isCurrentPlayersPitIndex(lastSowedPitIndex) &&
                pits.get(lastSowedPitIndex) == 1
        ) {
            int currentPlayersBigPitIndex = gameHelper.getCurrentPlayersBigPitIndex();
            int oppositeIndexToCapture = MancalaConstants.OPPOSITE_INDEX[lastSowedPitIndex];

            int newCountOfBigPit = pits.get(currentPlayersBigPitIndex) + pits.get(oppositeIndexToCapture) + 1;

            pits.set(lastSowedPitIndex, 0);
            pits.set(oppositeIndexToCapture, 0);
            pits.set(currentPlayersBigPitIndex, newCountOfBigPit);
        }
    }

    private void controlIfGameIsOverAndFinish(MancalaGameHelper gameHelper) {
        int remainingStoneCount = gameHelper.getCurrentPlayersRemainingStoneCount();
        if(remainingStoneCount == 0) {
            // move opponent's remaining stones to his big pit and get stone count
            gameHelper.moveOpponentsRemainingStonesToBigPit();
            gameHelper.finishGame();
        }
    }

    private void switchPlayerIfRequired(MancalaGameHelper gameHelper, int lastSowedPitIndex) {
        int currentPlayersBigPitIndex = gameHelper.getCurrentPlayersBigPitIndex();

        if(currentPlayersBigPitIndex != lastSowedPitIndex) {
            gameHelper.switchCurrentPlayer();;
        }
    }

}
