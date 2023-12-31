package com.suslu.spring.websocket.game.mancala.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.message.LeftMessage;
import com.suslu.spring.websocket.game.mancala.model.message.SowMessage;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MancalaMessageServiceImpl implements MancalaMessageService {
    @Autowired
    MancalaRegistryService mancalaRegistryService;
    @Autowired
    PlayMancalaGame mancalaPlayGame;

    @Override
    public MancalaGameResponse leftGame(LeftMessage leftMessage) {
        String message = mancalaRegistryService.leftGame(leftMessage.getGameId(), leftMessage.getSenderPlayer());
        return new MancalaGameResponse(message);
    }

    @Override
    public MancalaGameResponse sowStones(SowMessage sowMessage) {
        MancalaGame game = mancalaRegistryService.getActiveMancalaGame(sowMessage.getGameId());

        checkPlayerForTurn(game, sowMessage.getSenderPlayer());
        mancalaPlayGame.sowStones(game, sowMessage.getPitIndex());

        ObjectMapper objectMapper = new ObjectMapper();
        MancalaGameResponse response = objectMapper.convertValue(game, MancalaGameResponse.class);
        response.setResponse("Succeed");

        return response;
    }

    private void checkPlayerForTurn(MancalaGame game, PlayerType senderPlayer) {
        if (!game.getCurrentPlayer().equals(senderPlayer)) {
            throw new MancalaRuntimeException("It is " + game.getCurrentPlayer() + "'s turn. But sender is " + senderPlayer);
        }
    }

}
