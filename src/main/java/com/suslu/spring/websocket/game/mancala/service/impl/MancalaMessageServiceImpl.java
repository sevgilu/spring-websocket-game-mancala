package com.suslu.spring.websocket.game.mancala.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.message.MoveMessage;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;
import com.suslu.spring.websocket.game.mancala.service.MancalaMessageService;
import com.suslu.spring.websocket.game.mancala.service.MancalaRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MancalaMessageServiceImpl implements MancalaMessageService {

    @Autowired
    MancalaRegistryService mancalaRegistryService;

    @Override
    public MancalaGameResponse makeMove(MoveMessage moveMessage) {
        MancalaGame game = mancalaRegistryService.getActiveMancalaGame(moveMessage.getGameId());

        checkGameStateToMove(game);
        checkPlayerForTurn(game, moveMessage.getSenderPlayer());
        checkPit(game, moveMessage.getPitIndex());

        processMove(game, moveMessage.getPitIndex());

        ObjectMapper objectMapper = new ObjectMapper();
        MancalaGameResponse response = objectMapper.convertValue(game, MancalaGameResponse.class);
        response.setResponse("Succeed");

        return response;
    }

    private void processMove(MancalaGame game, int pitIndex) {
        System.out.println("Processing move:" + pitIndex);

        game.switchCurrentPlayer();
    }

    private void checkGameStateToMove(MancalaGame game) {
        if (!GameState.ACTIVE.equals(game.getGameState())) {
            throw new MancalaRuntimeException("Game is not active. gameId: " + game.getGameId());
        }
    }

    private void checkPlayerForTurn(MancalaGame game, PlayerType senderPlayer) {
        if (!game.getCurrentPlayer().equals(senderPlayer)) {
            throw new MancalaRuntimeException("It is " + game.getCurrentPlayer() + "'s turn. But sender is " + senderPlayer);
        }
    }

    private void checkPit(MancalaGame game, int pitIndex) {
        List<Integer> pits = game.getPits();

        if (MancalaGame.PIT_COUNT <= pitIndex) {
            throw new MancalaRuntimeException("Invalid pitIndex: " + pitIndex);
        }
        if ((MancalaGame.BIG_PIT_INDEX_1 == pitIndex || MancalaGame.BIG_PIT_INDEX_2 == pitIndex)) {
            throw new MancalaRuntimeException("Big pits can not be selected to move stones.");
        }
        if ((PlayerType.PLAYER_1.equals(game.getCurrentPlayer()) && pitIndex >= MancalaGame.BIG_PIT_INDEX_1) ||
                (PlayerType.PLAYER_2.equals(game.getCurrentPlayer()) && pitIndex <= MancalaGame.BIG_PIT_INDEX_1)) {
            throw new MancalaRuntimeException("Player can not move stones in the opponent's pits.");
        }
        if(pits.get(pitIndex) == 0) {
            throw new MancalaRuntimeException("There is no stone in the selected pit.");
        }
    }
}
