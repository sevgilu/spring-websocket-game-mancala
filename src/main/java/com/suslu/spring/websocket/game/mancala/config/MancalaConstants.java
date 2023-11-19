package com.suslu.spring.websocket.game.mancala.config;

import com.suslu.spring.websocket.game.mancala.enums.PlayerType;

public class MancalaConstants {
    public static final int TOTAL_PIT_COUNT= 14;
    public static final int BIG_PIT_INDEX_1= 6;
    public static final int BIG_PIT_INDEX_2= 13;
    public static final int STONE_COUNT= 6;
    public static final int[] OPPOSITE_INDEX = {12,11,10,9,8,7,6,5,4,3,2,1,0,13};

    public static int[] getPitIndexIntervalOf(PlayerType playerType) {
        int[] indexes = new int[2];
        if( PlayerType.PLAYER_1.equals(playerType) ) {
            indexes[0] = 0;
            indexes[1] = BIG_PIT_INDEX_1 - 1;
        } else {
            indexes[0] = BIG_PIT_INDEX_1 + 1;
            indexes[1] = BIG_PIT_INDEX_2 - 1;
        }
        return indexes;
    }

    public static int getBigPitIndexOf(PlayerType playerType) {
        return PlayerType.PLAYER_1.equals(playerType) ?
                        MancalaConstants.BIG_PIT_INDEX_1 : MancalaConstants.BIG_PIT_INDEX_2;
    }

    public static boolean isBigPitIndex(int index) {
        return MancalaConstants.BIG_PIT_INDEX_1 == index || MancalaConstants.BIG_PIT_INDEX_2 == index;
    }

    public static boolean isPlayersPitIndex(PlayerType playerType, int index) {
        return
            (PlayerType.PLAYER_1.equals(playerType) && index < MancalaConstants.BIG_PIT_INDEX_1)
            ||
            (PlayerType.PLAYER_2.equals(playerType) && index > MancalaConstants.BIG_PIT_INDEX_1 &&
                    index != MancalaConstants.BIG_PIT_INDEX_2);
    }

}
