package com.suslu.spring.websocket.game.mancala;

import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.Player;

import java.util.List;

public class MancalaGameBuilder {
    private String gameId;
    private Player player1;
    private Player player2;
    private GameState gameState;
    private PlayerType currentPlayer;
    private boolean creatorOfTheGame;
    private List<Integer> pits;
    private PlayerType winnerPlayer;

    public MancalaGameBuilder withGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public MancalaGameBuilder withPlayer2(String name) {
        this.player2 = new Player(name);
        return this;
    }

    public MancalaGameBuilder withPlayer1(String name) {
        this.player1 = new Player(name);
        return this;
    }

    public MancalaGameBuilder withGameState(GameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public MancalaGameBuilder withCurrentPlayer(PlayerType currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public MancalaGameBuilder withCreatorOfTheGame(boolean creatorOfTheGame) {
        this.creatorOfTheGame = creatorOfTheGame;
        return this;
    }

    public MancalaGameBuilder withPits(List<Integer> pits) {
        this.pits = pits;
        return this;
    }

    public MancalaGameBuilder withWinnerPlayer(PlayerType winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
        return this;
    }

    public MancalaGame build() {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setGameState(gameState);
        game.setCurrentPlayer(currentPlayer);
        game.setCreatorOfTheGame(creatorOfTheGame);
        game.setPits(pits);
        game.setWinnerPlayer(winnerPlayer);
        return game;
    }

}
