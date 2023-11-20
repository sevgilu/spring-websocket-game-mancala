package com.suslu.spring.websocket.game.mancala.helper;

import com.suslu.spring.websocket.game.mancala.enums.PlayerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MancalaConstantsTest {

    @ParameterizedTest
    @MethodSource("pitIndexIntervalOfData")
    void shouldGetPitIndexIntervalOfGivenPlayerType(int[] expectedIndexes, PlayerType givenPlayerType) {
        Assertions.assertArrayEquals(expectedIndexes, MancalaConstants.getPitIndexIntervalOf(givenPlayerType));
    }
    static Stream<Arguments> pitIndexIntervalOfData(){
        return Stream.of(
                Arguments.of(new int[]{0,5}, PlayerType.PLAYER_1),
                Arguments.of(new int[]{7,12}, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("bigPitIndexOfData")
    void shouldGetBigPitIndexOf(int index, PlayerType givenPlayerType) {
        Assertions.assertEquals(index, MancalaConstants.getBigPitIndexOf(givenPlayerType));
    }
    static Stream<Arguments> bigPitIndexOfData(){
        return Stream.of(
                Arguments.of(6, PlayerType.PLAYER_1),
                Arguments.of(13, PlayerType.PLAYER_2)
        );
    }

    @ParameterizedTest
    @MethodSource("isBigPitIndexData")
    void shouldCheckIfIsBigPitIndex(boolean expected, int givenIndex) {
        Assertions.assertEquals(expected, MancalaConstants.isBigPitIndex(givenIndex));
    }
    static Stream<Arguments> isBigPitIndexData(){
        return Stream.of(
                Arguments.of(false, 0),
                Arguments.of(false, 1),
                Arguments.of(false, 2),
                Arguments.of(false, 3),
                Arguments.of(false, 4),
                Arguments.of(false, 5),
                Arguments.of(true, 6),
                Arguments.of(false, 7),
                Arguments.of(false, 8),
                Arguments.of(false, 9),
                Arguments.of(false, 10),
                Arguments.of(false, 11),
                Arguments.of(false, 12),
                Arguments.of(true, 13)
        );
    }


    @ParameterizedTest
    @MethodSource("isPlayersPitIndexData")
    void shouldCheckIfIsPlayersPitIndex(boolean expected, int givenIndex, PlayerType givenPlayerType) {
        Assertions.assertEquals(expected, MancalaConstants.isPlayersPitIndex(givenPlayerType, givenIndex));
    }
    static Stream<Arguments> isPlayersPitIndexData(){
        return Stream.of(
                // for PLAYER_1
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
                // for PLAYER_2
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
}
