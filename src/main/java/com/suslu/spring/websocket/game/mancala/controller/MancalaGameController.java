package com.suslu.spring.websocket.game.mancala.controller;

import com.suslu.spring.websocket.game.mancala.model.Player;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;
import com.suslu.spring.websocket.game.mancala.service.MancalaRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class MancalaGameController {
    @Autowired
    private MancalaRegistryService mancalaRegistryService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/join")
    public ResponseEntity<MancalaGameResponse> joinGame(@RequestBody Player player){
        MancalaGameResponse response = null;
        try {
            response = mancalaRegistryService.joinGame(player);
            if( ! response.isCreatorOfTheGame()) {
                simpMessagingTemplate.convertAndSend(
                        "/topic/game-state/" + response.getGameId(), response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new MancalaGameResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
