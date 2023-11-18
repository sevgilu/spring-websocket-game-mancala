package com.suslu.spring.websocket.game.mancala.controller;

import com.suslu.spring.websocket.game.mancala.model.message.SowMessage;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;
import com.suslu.spring.websocket.game.mancala.service.MancalaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MancalaWebsocketMessageController {
    @Autowired
    private MancalaMessageService mancalaMessageService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/game/sow")
    ResponseEntity<MancalaGameResponse> SowStones(SowMessage sowMessage) {
        MancalaGameResponse response = mancalaMessageService.sowStones(sowMessage);
        simpMessagingTemplate.convertAndSend(
                "/topic/game-state/" + sowMessage.getGameId(), response);
        return ResponseEntity.ok(response);
    }
}
