package com.suslu.spring.websocket.game.mancala.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.Player;
import com.suslu.spring.websocket.game.mancala.model.response.MancalaGameResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MancalaRegistryServiceImpl implements MancalaRegistryService {
    Set<Player> registeredPlayers= new HashSet<>();
    private Map<String, MancalaGame> waitingGames = new HashMap<>();
    private Map<String, MancalaGame> activeGames = new HashMap<>();

    @Override
    public MancalaGame getActiveMancalaGame(String gameId) {
        MancalaGame mancalaGame = activeGames.get(gameId);
        if(mancalaGame == null) {
            throw  new MancalaRuntimeException("There is no active game with gameId:" + gameId);
        }
        return mancalaGame;
    }

    @Override
    public MancalaGameResponse joinGame(Player player) {
        checkForRegisteredPlayer(player);
        registeredPlayers.add(player);

        MancalaGame mancalaGame = addToAnAvailableGameOrInitializeNewGame(player);

        ObjectMapper objectMapper = new ObjectMapper();
        MancalaGameResponse response = objectMapper.convertValue(mancalaGame, MancalaGameResponse.class);
        response.setResponse("Succeed");
        
        return response;
    }

    private void checkForRegisteredPlayer(Player player) {
        if( registeredPlayers.contains(player) ) {
            throw new MancalaRuntimeException("Player already registered with name " + player.getName());
        }
    }

    private MancalaGame addToAnAvailableGameOrInitializeNewGame(Player player) {
        String gameId = waitingGames.keySet().stream().findFirst().orElse(null);
        MancalaGame mancalaGame = null ;

        if(gameId == null) {
            // means player is the player1 who is initializing a game
            mancalaGame = initializeNewMancalaGame(player);
        } else {
            // means player is the player2 who is attending an existing game
            mancalaGame = addPlayerToAvailableGame(player, gameId);
        }

        return mancalaGame;
    }

    private MancalaGame initializeNewMancalaGame(Player player) {
        String gameId = UUID.randomUUID().toString();
        MancalaGame mancalaGame = new MancalaGame(gameId, player);
        waitingGames.put(gameId, mancalaGame);

        return mancalaGame;
    }

    private MancalaGame addPlayerToAvailableGame(Player player, String gameId) {
        MancalaGame mancalaGame = waitingGames.get(gameId);
        mancalaGame.setPlayer2(player);
        mancalaGame.setGameState(GameState.ACTIVE);
        mancalaGame.setCreatorOfTheGame(false);

        activeGames.put(gameId, mancalaGame);
        waitingGames.remove(gameId);

        return mancalaGame;
    }

    @Override
    public String leftGame(String gameId, PlayerType senderPlayer) {
        String message;
        MancalaGame game = waitingGames.get(gameId);

        if(game == null) {
            message = leftActiveGame(gameId, senderPlayer);
        } else {
            waitingGames.remove(gameId);
            registeredPlayers.remove(game.getPlayer1());
            message = game.getPlayer1().getName() + " left the game";
        }

        return message;
    }

    private String leftActiveGame(String gameId, PlayerType senderPlayer) {
        MancalaGame game = getActiveMancalaGame(gameId);

        activeGames.remove(gameId);
        registeredPlayers.remove(game.getPlayer1());
        registeredPlayers.remove(game.getPlayer2());

        Player leavingPlayer = PlayerType.PLAYER_1.equals(senderPlayer) ? game.getPlayer1() : game.getPlayer2();
        return leavingPlayer.getName() + " left the game";
    }


}
