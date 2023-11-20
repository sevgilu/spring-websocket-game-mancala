package com.suslu.spring.websocket.game.mancala.helper;

import com.suslu.spring.websocket.game.mancala.MancalaGameHelperBuilder;
import com.suslu.spring.websocket.game.mancala.enums.GameState;
import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import com.suslu.spring.websocket.game.mancala.model.MancalaGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MancalaGameHelperTest {

    private MancalaGameHelperBuilder helperBuilder = new MancalaGameHelperBuilder();

    @ParameterizedTest
    @MethodSource("opponentPlayerData")
    void shouldGetOpponentPlayer(PlayerType expected, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder.withCurrentPlayer(currentPlayer).build();
        Assertions.assertEquals(expected, gameHelper.getOpponentPlayer());
    }
    static Stream<Arguments> opponentPlayerData(){
        return Stream.of(
                Arguments.of(PlayerType.PLAYER_2, PlayerType.PLAYER_1),
                Arguments.of(PlayerType.PLAYER_1, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("currentPlayersBigPitIndexData")
    void shouldGetCurrentPlayersBigPitIndex(int expectedIndex, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder.withCurrentPlayer(currentPlayer).build();
        Assertions.assertEquals(expectedIndex, gameHelper.getCurrentPlayersBigPitIndex());
    }
    static Stream<Arguments> currentPlayersBigPitIndexData(){
        return Stream.of(
                Arguments.of(6, PlayerType.PLAYER_1),
                Arguments.of(13, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("opponentsBigPitIndexData")
    void shouldGetOpponentsBigPitIndex(int expectedIndex, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder.withCurrentPlayer(currentPlayer).build();
        Assertions.assertEquals(expectedIndex, gameHelper.getOpponentsBigPitIndex());
    }
    static Stream<Arguments> opponentsBigPitIndexData(){
        return Stream.of(
                Arguments.of(13, PlayerType.PLAYER_1),
                Arguments.of(6, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("currentPlayersRemainingStoneCountData")
    void shouldGetCurrentPlayersRemainingStoneCount(
            int expectedCount, List<Integer> pits, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder
                .withCurrentPlayer(currentPlayer)
                .withPits(pits)
                .build();
        Assertions.assertEquals(expectedCount, gameHelper.getCurrentPlayersRemainingStoneCount());
    }
    static Stream<Arguments> currentPlayersRemainingStoneCountData(){
        return Stream.of(
                // when currentPlayer is PLAYER_1
                Arguments.of(0, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0), PlayerType.PLAYER_1),
                Arguments.of(1, Arrays.asList(1,0,0,0,0,0,  3,  0,0,0,0,0,0,  0), PlayerType.PLAYER_1),
                Arguments.of(1, Arrays.asList(1,0,0,0,0,0, 0,  1,1,1,0,0,0,  0), PlayerType.PLAYER_1),
                Arguments.of(21, Arrays.asList(1,2,3,4,5,6,  3,  1,1,1,1,1,1,  0), PlayerType.PLAYER_1),
                Arguments.of(6, Arrays.asList(1,1,1,1,1,1,  0,  1,2,3,4,5,6,  0), PlayerType.PLAYER_1),
                // when currentPlayer is PLAYER_2
                Arguments.of(0, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0), PlayerType.PLAYER_2),
                Arguments.of(1, Arrays.asList(0,0,0,0,0,0,  3,  1,0,0,0,0,0,  3), PlayerType.PLAYER_2),
                Arguments.of(3, Arrays.asList(1,0,0,0,0,0, 0,  1,1,1,0,0,0,  0), PlayerType.PLAYER_2),
                Arguments.of(6, Arrays.asList(1,2,3,4,5,6,  3,  1,1,1,1,1,1,  0), PlayerType.PLAYER_2),
                Arguments.of(21, Arrays.asList(1,1,1,1,1,1,  0,  1,2,3,4,5,6,  0), PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("isCurrentPlayersPitIndexData")
    void shouldGetIfIsCurrentPlayersPitIndexData(
            boolean expected, int index, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder.withCurrentPlayer(currentPlayer).build();
        Assertions.assertEquals(expected, gameHelper.isCurrentPlayersPitIndex(index));
    }
    static Stream<Arguments> isCurrentPlayersPitIndexData(){
        return Stream.of(
                // when currentPlayer is PLAYER_1
                Arguments.of(true, 0, PlayerType.PLAYER_1),
                Arguments.of(true, 1, PlayerType.PLAYER_1),
                Arguments.of(true, 2, PlayerType.PLAYER_1),
                Arguments.of(true, 3, PlayerType.PLAYER_1),
                Arguments.of(true, 4, PlayerType.PLAYER_1),
                Arguments.of(true, 5, PlayerType.PLAYER_1),
                Arguments.of(false, 6, PlayerType.PLAYER_1),
                Arguments.of(false, 7, PlayerType.PLAYER_1),
                Arguments.of(false, 8, PlayerType.PLAYER_1),
                Arguments.of(false, 9, PlayerType.PLAYER_1),
                Arguments.of(false, 10, PlayerType.PLAYER_1),
                Arguments.of(false, 11, PlayerType.PLAYER_1),
                Arguments.of(false, 12, PlayerType.PLAYER_1),
                Arguments.of(false, 13, PlayerType.PLAYER_1),
                // when currentPlayer is PLAYER_2
                Arguments.of(false, 0, PlayerType.PLAYER_2),
                Arguments.of(false, 1, PlayerType.PLAYER_2),
                Arguments.of(false, 2, PlayerType.PLAYER_2),
                Arguments.of(false, 3, PlayerType.PLAYER_2),
                Arguments.of(false, 4, PlayerType.PLAYER_2),
                Arguments.of(false, 5, PlayerType.PLAYER_2),
                Arguments.of(false, 6, PlayerType.PLAYER_2),
                Arguments.of(true, 7, PlayerType.PLAYER_2),
                Arguments.of(true, 8, PlayerType.PLAYER_2),
                Arguments.of(true, 9, PlayerType.PLAYER_2),
                Arguments.of(true, 10, PlayerType.PLAYER_2),
                Arguments.of(true, 11, PlayerType.PLAYER_2),
                Arguments.of(true, 12, PlayerType.PLAYER_2),
                Arguments.of(false, 13, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("moveOpponentsRemainingStonesToBigPitData")
    void shouldMoveOpponentsRemainingStonesToBigPit(
            int[] expectedPits, List<Integer> pits, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder
                .withCurrentPlayer(currentPlayer)
                .withPits(pits)
                .build();
        gameHelper.moveOpponentsRemainingStonesToBigPit();
        int[] actualPits = gameHelper.getGame().getPits()
                .stream().mapToInt(Integer::intValue).toArray();
        Assertions.assertArrayEquals(expectedPits, actualPits);
    }
    static Stream<Arguments> moveOpponentsRemainingStonesToBigPitData(){
        return Stream.of(
                // when currentPlayer is PLAYER_1
                Arguments.of(new int[]{0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0}, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0), PlayerType.PLAYER_1),
                Arguments.of(new int[]{0,0,1,0,0,0,  1,  0,0,0,0,0,0,  6}, Arrays.asList(0,0,1,0,0,0,  1,  1,1,1,1,1,1,  0), PlayerType.PLAYER_1),
                Arguments.of(new int[]{1,1,1,1,1,1,  3,  0,0,0,0,0,0,  12}, Arrays.asList(1,1,1,1,1,1,  3,  1,1,1,1,1,1,  6), PlayerType.PLAYER_1),
                Arguments.of(new int[]{1,1,1,1,1,1,  0,  0,0,0,0,0,0,  28}, Arrays.asList(1,1,1,1,1,1,  0,  1,2,3,4,5,6,  7), PlayerType.PLAYER_1),
                // when currentPlayer is PLAYER_2
                Arguments.of(new int[]{0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0}, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0), PlayerType.PLAYER_2),
                Arguments.of(new int[]{0,0,0,0,0,0,  6,  0,0,1,0,0,0,  1}, Arrays.asList(1,1,1,1,1,1,  0,  0,0,1,0,0,0,  1), PlayerType.PLAYER_2),
                Arguments.of(new int[]{0,0,0,0,0,0,  12,  1,1,1,1,1,1,  3}, Arrays.asList(1,1,1,1,1,1,  6,  1,1,1,1,1,1,  3), PlayerType.PLAYER_2),
                Arguments.of(new int[]{0,0,0,0,0,0,  28,  1,1,1,1,1,1,  0}, Arrays.asList( 1,2,3,4,5,6,  7,  1,1,1,1,1,1,  0), PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("stoneCountInPlayersBigPitData")
    void shouldGetStoneCountInPlayersBigPit(int expectedCount, List<Integer> pits, PlayerType playerType) {
        MancalaGameHelper gameHelper = helperBuilder.withPits(pits).build();
        Assertions.assertEquals(expectedCount, gameHelper.getStoneCountInPlayersBigPit(playerType));
    }
    static Stream<Arguments> stoneCountInPlayersBigPitData(){
        return Stream.of(
                // for PLAYER_1
                Arguments.of(0, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  1), PlayerType.PLAYER_1),
                Arguments.of(0, Arrays.asList(1,1,1,1,1,1,  0,  0,0,0,1,0,0,  0), PlayerType.PLAYER_1),
                Arguments.of(3, Arrays.asList(0,0,0,0,0,0,  3,  0,1,0,0,0,0,  1), PlayerType.PLAYER_1),
                Arguments.of(9, Arrays.asList(1,1,1,1,1,1,  9,  0,0,0,0,0,0,  0), PlayerType.PLAYER_1),
                // for PLAYER_2
                Arguments.of(0, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0), PlayerType.PLAYER_2),
                Arguments.of(0, Arrays.asList(0,0,0,0,0,0,  1,  1,1,1,1,1,1,  0), PlayerType.PLAYER_2),
                Arguments.of(4, Arrays.asList(0,0,0,2,0,0,  5,  0,0,0,0,0,0,  4), PlayerType.PLAYER_2),
                Arguments.of(14, Arrays.asList(0,0,0,2,0,0,  9,  1,2,3,4,0,0,  14), PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("switchCurrentPlayerData")
    void shouldSwitchCurrentPlayer(PlayerType expected, PlayerType currentPlayer) {
        MancalaGameHelper gameHelper = helperBuilder.withCurrentPlayer(currentPlayer).build();
        gameHelper.switchCurrentPlayer();
        Assertions.assertEquals(expected, gameHelper.getGame().getCurrentPlayer());
    }
    static Stream<Arguments> switchCurrentPlayerData(){
        return Stream.of(
                Arguments.of(PlayerType.PLAYER_2, PlayerType.PLAYER_1),
                Arguments.of(PlayerType.PLAYER_1, PlayerType.PLAYER_2)
        );
    }



    @ParameterizedTest
    @MethodSource("finishGameData")
    void shouldFinishGame(int[] expectedPits,
                          PlayerType expectedWinner,
                          int expectedCurrentBigPitCount,
                          int expectedOpponentBigPitCount,
                          PlayerType givenCurrentPlayer,
                          List<Integer> givenPits) {
        MancalaGameHelper gameHelper = helperBuilder
                .withGameState(GameState.ACTIVE)
                .withCurrentPlayer(givenCurrentPlayer)
                .withPits(givenPits)
                .build();

        gameHelper.finishGame();

        MancalaGame actualGame = gameHelper.getGame();
        int[] actualPits = actualGame.getPits()
                .stream().mapToInt(Integer::intValue).toArray();
        Assertions.assertEquals(GameState.FINISHED, actualGame.getGameState());
        Assertions.assertEquals(givenCurrentPlayer, actualGame.getCurrentPlayer());
        Assertions.assertArrayEquals(expectedPits, actualPits);
        Assertions.assertEquals(expectedWinner, actualGame.getWinnerPlayer());
    }
    static Stream<Arguments> finishGameData(){
        return Stream.of(
                // when currentPlayer is PLAYER_1
                Arguments.of(new int[]{0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0}, null, 0, 0, PlayerType.PLAYER_1, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0)),
                Arguments.of(new int[]{0,0,0,0,0,0,  3,  0,0,0,0,0,0,  3}, null, 0, 0, PlayerType.PLAYER_1, Arrays.asList(0,0,0,0,0,0,  3,  0,0,0,0,0,0,  3)),
                Arguments.of(new int[]{1,1,1,0,0,0,  0,  1,0,0,0,0,0,  0}, null, 0, 0, PlayerType.PLAYER_1, Arrays.asList(1,1,1,0,0,0,  0,  1,0,0,0,0,0,  0)),
                Arguments.of(new int[]{0,0,0,0,0,0,  5,  0,0,0,0,0,0,  6}, PlayerType.PLAYER_2, 5, 6, PlayerType.PLAYER_1, Arrays.asList(0,0,0,0,0,0,  5,  0,0,0,0,0,0,  6)),
                Arguments.of(new int[]{0,0,0,0,0,0,  11,  0,0,0,0,0,0,  3}, PlayerType.PLAYER_1, 5, 6, PlayerType.PLAYER_1, Arrays.asList(0,0,0,0,0,0,  11,  0,0,0,0,0,0,  3)),


                // when currentPlayer is PLAYER_2
                Arguments.of(new int[]{0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0}, null, 0, 0, PlayerType.PLAYER_2, Arrays.asList(0,0,0,0,0,0,  0,  0,0,0,0,0,0,  0)),
                Arguments.of(new int[]{0,0,0,0,0,0,  7,  0,0,0,0,0,0,  7}, null, 0, 0, PlayerType.PLAYER_2, Arrays.asList(0,0,0,0,0,0,  7,  0,0,0,0,0,0,  7)),
                Arguments.of(new int[]{1,1,1,0,0,0,  0,  1,0,0,0,0,0,  0}, null, 0, 0, PlayerType.PLAYER_2, Arrays.asList(1,1,1,0,0,0,  0,  1,0,0,0,0,0,  0)),
                Arguments.of(new int[]{0,0,0,0,0,0,  5,  0,0,0,0,0,0,  6}, PlayerType.PLAYER_2, 5, 6, PlayerType.PLAYER_2, Arrays.asList(0,0,0,0,0,0,  5,  0,0,0,0,0,0,  6)),
                Arguments.of(new int[]{0,0,0,0,0,0,  11,  0,0,0,0,0,0,  3}, PlayerType.PLAYER_1, 5, 6, PlayerType.PLAYER_2, Arrays.asList(0,0,0,0,0,0,  11,  0,0,0,0,0,0,  3))

        );
    }

}
