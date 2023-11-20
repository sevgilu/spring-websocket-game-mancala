package com.suslu.spring.websocket.game.mancala.service;

import com.suslu.spring.websocket.game.mancala.MancalaGameBuilder;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.exception.MancalaRuntimeException;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PlayMancalaGameTest {
    @Autowired
    PlayMancalaGame playMancalaGame;

    MancalaGameBuilder gameBuilder = new MancalaGameBuilder();

    @ParameterizedTest
    @MethodSource("exceptionData")
    void shouldThrowException(
            List<Integer> pits, GameState givenState, PlayerType givenCurrentPlayer, int pitIndex) {
        MancalaGame game = gameBuilder
                .withGameState(givenState)
                .withCurrentPlayer(givenCurrentPlayer)
                .withPits(pits)
                .build();
        Assertions.assertThrows(MancalaRuntimeException.class, () -> playMancalaGame.sowStones(game, pitIndex));
    }

    // it would be better to test with specific error codes or messages but error messaging strategy not implemented
    static Stream<Arguments> exceptionData() {
        return Stream.of(
                // cases for invalid state
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.WAITING_FOR_PLAYER_2, PlayerType.PLAYER_1, 0),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.FINISHED, PlayerType.PLAYER_1, 0),
                // case invalid pit index - not existing index
                Arguments.of(Arrays.asList(6,6,6,0,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 23),
                // case invalid pit index - player1's BigPit
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 6),
                // case invalid pit index - player2's BigPit
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 13),
                // case - try to sow opponents stones Player1
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 7),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 8),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 9),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 10),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 11),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 12),
                // case - try to sow opponents stones Player2
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 0),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 1),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 2),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 3),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 4),
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_2, 5),
                // case for empty pit
                Arguments.of(Arrays.asList(6,6,6,0,6,6, 0, 6,6,6,6,6,6, 0), GameState.ACTIVE, PlayerType.PLAYER_1, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("successData")
    void shouldSowStones(
            List<Integer> pits, PlayerType givenCurrentPlayer, int pitIndex,
            PlayerType expectedCurrentPlayer, int[] expectedPits) {
        MancalaGame game = gameBuilder
                .withGameState(GameState.ACTIVE)
                .withCurrentPlayer(givenCurrentPlayer)
                .withPits(pits)
                .build();

        playMancalaGame.sowStones(game, pitIndex);

        int[] actualPits = game.getPits()
                .stream().mapToInt(Integer::intValue).toArray();
        Assertions.assertEquals(expectedCurrentPlayer, game.getCurrentPlayer());
        Assertions.assertArrayEquals(expectedPits, actualPits);
    }

    static Stream<Arguments> successData() {
        return Stream.of(
                // case - basic - turn in the opponent
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), PlayerType.PLAYER_1, 1,
                        PlayerType.PLAYER_2, new int[]{6,0,7,7,7,7, 1, 7,6,6,6,6,6, 0}),
                // case - last stone in bigPit - turn in the same player again
                Arguments.of(Arrays.asList(6,6,6,6,6,6, 0, 6,6,6,6,6,6, 0), PlayerType.PLAYER_1, 0,
                        PlayerType.PLAYER_1, new int[]{0,7,7,7,7,7, 1, 6,6,6,6,6,6, 0}),
                // case - last stone in current player's empty pit - collect opponent stones from the opposite
                Arguments.of(Arrays.asList(5,6,6,6,6,0, 7, 6,6,6,6,6,6, 0), PlayerType.PLAYER_1, 0,
                        PlayerType.PLAYER_2, new int[]{0,7,7,7,7,0, 14, 0,6,6,6,6,6, 0}),
                // case - last stone in opponent's empty pit - so not collect any stone
                Arguments.of(Arrays.asList(7,6,6,6,6,6, 0, 0,0,8,8,8,8, 2), PlayerType.PLAYER_1, 0,
                        PlayerType.PLAYER_2, new int[]{0,7,7,7,7,7, 1, 1,0,8,8,8,8, 2}),
                // case - basic - skip opponents bigPit
                Arguments.of(Arrays.asList(0,6,6,6,6,12, 0, 6,6,6,6,6,6, 0), PlayerType.PLAYER_1, 5,
                        PlayerType.PLAYER_2, new int[]{1,7,7,7,7,0, 1, 7,7,7,7,7,7, 0}),
                // case - sow 13 stones - end up same also empty pit - collect opponent's stones from the opposite
                Arguments.of(Arrays.asList(0,6,6,6,5,13, 0, 6,6,6,6,6,6, 0), PlayerType.PLAYER_1, 5,
                        PlayerType.PLAYER_2, new int[]{1,7,7,7,6,0, 9, 0,7,7,7,7,7, 0})
        );
    }

    @ParameterizedTest
    @MethodSource("successGameOverData")
    void shouldSowStones_ThenFinishTheGame(
            List<Integer> pits, PlayerType givenCurrentPlayer, int pitIndex, PlayerType expectedWinner, int[] expectedPits) {
        MancalaGame game = gameBuilder
                .withGameState(GameState.ACTIVE)
                .withCurrentPlayer(givenCurrentPlayer)
                .withPits(pits)
                .build();

        playMancalaGame.sowStones(game, pitIndex);

        int[] actualPits = game.getPits()
                .stream().mapToInt(Integer::intValue).toArray();
        Assertions.assertEquals(GameState.FINISHED, game.getGameState());
        Assertions.assertEquals(expectedWinner, game.getWinnerPlayer());
        Assertions.assertEquals(givenCurrentPlayer, game.getCurrentPlayer());
        Assertions.assertArrayEquals(expectedPits, actualPits);
    }

    static Stream<Arguments> successGameOverData() {
        return Stream.of(
                // case - end game - no winner
                Arguments.of(Arrays.asList(0,0,0,0,0,1, 35, 0,0,2,5,0,0, 29), PlayerType.PLAYER_1, 5,
                        null, new int[]{0,0,0,0,0,0, 36, 0,0,0,0,0,0, 36}),
                // case - end game - PLAYER_1 is the winner
                Arguments.of(Arrays.asList(0,0,0,0,0,1, 38, 0,0,2,5,0,0, 26), PlayerType.PLAYER_1, 5,
                        PlayerType.PLAYER_1, new int[]{0,0,0,0,0,0, 39, 0,0,0,0,0,0, 33}),
                // case - end game - PLAYER_2 is the winner
                Arguments.of(Arrays.asList(0,0,0,0,0,1, 32, 0,0,2,5,0,0, 32), PlayerType.PLAYER_1, 5,
                        PlayerType.PLAYER_2, new int[]{0,0,0,0,0,0, 33, 0,0,0,0,0,0, 39})
        );
    }
}
