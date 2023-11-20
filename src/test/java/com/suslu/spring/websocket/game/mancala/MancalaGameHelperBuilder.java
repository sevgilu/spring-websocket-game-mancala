package com.suslu.spring.websocket.game.mancala;

import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.helper.MancalaGameHelper;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import com.suslu.spring.websocket.game.mancala.model.Player;

import java.util.List;

public class MancalaGameHelperBuilder {
    private String gameId;
    private Player player1;
    private Player player2;
    private GameState gameState;
    private PlayerType currentPlayer;
    private boolean creatorOfTheGame;
    private List<Integer> pits;
    private PlayerType winnerPlayer;

    public MancalaGameHelperBuilder withGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public MancalaGameHelperBuilder withPlayer2(String name) {
        this.player2 = new Player(name);
        return this;
    }

    public MancalaGameHelperBuilder withPlayer1(String name) {
        this.player1 = new Player(name);
        return this;
    }

    public MancalaGameHelperBuilder withGameState(GameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public MancalaGameHelperBuilder withCurrentPlayer(PlayerType currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public MancalaGameHelperBuilder withCreatorOfTheGame(boolean creatorOfTheGame) {
        this.creatorOfTheGame = creatorOfTheGame;
        return this;
    }

    public MancalaGameHelperBuilder withPits(List<Integer> pits) {
        this.pits = pits;
        return this;
    }

    public MancalaGameHelperBuilder withWinnerPlayer(PlayerType winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
        return this;
    }

    public MancalaGameHelper build() {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setGameState(gameState);
        game.setCurrentPlayer(currentPlayer);
        game.setCreatorOfTheGame(creatorOfTheGame);
        game.setPits(pits);
        game.setWinnerPlayer(winnerPlayer);
        return new MancalaGameHelper(game);
    }

}
