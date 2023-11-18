package com.suslu.spring.websocket.game.mancala.service;

import com.suslu.spring.websocket.game.mancala.model.message.MoveMessage;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;

public interface MancalaMessageService {
    MancalaGameResponse makeMove(MoveMessage moveMessage);
}
