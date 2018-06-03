package com.example.mihubahaqueen.minimalistc;

/**
 * Created by IBM on 2018/5/22.
 */


public class init {
    //棋子编号
    static final int PIECE_KING = 0;
    static final int PIECE_ADVISOR = 1;
    static final int PIECE_BISHOP = 2;
    static final int PIECE_KNIGHT = 3;
    static final int PIECE_ROOK = 4;
    static final int PIECE_CANNON = 5;
    static final int PIECE_PAWN = 6;

    public int[] mvs = new int[128];
    // 其他常数
    static final int MAX_GEN_MOVES = 128; // 最大的生成走法数
    static final int LIMIT_DEPTH = 32;    // 最大的搜索深度
    static final int MATE_VALUE = 10000;  // 最高分值，即将死的分值
    static final int WIN_VALUE = MATE_VALUE - 100; // 搜索出胜负的分值界限，超出此值就说明已经搜索出杀棋了
    static final int ADVANCED_VALUE = 3;  // 先行权分值

    static int sdPlayer;                   // 轮到谁走，0=红方，1=黑方
    static byte[] ucpcSquares = new byte[256];       // 棋盘上的棋子

    // 判断棋子是否在棋盘中的数组
    static final byte[] ccInBoard = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    // 判断棋子是否在九宫的数组
    static final byte[] ccInFort = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    // 判断步长是否符合特定走法的数组，1=帅(将)，2=仕(士)，3=相(象)
    static final byte[] ccLegalSpan = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0
    };

    // 根据步长判断马是否蹩腿的数组
    static final byte[] ccKnightPin = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -16, 0, -16, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0
    };

    static final int cucvlPiecePos[][] = {
        { // 帅(将)
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  1,  1,  1,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  2,  2,  2,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0, 11, 15, 11,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 仕(士)
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0, 20,  0, 20,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0, 23,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0, 20,  0, 20,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 相(象)
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0, 20,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0, 18,  0,  0,  0, 23,  0,  0,  0, 18,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0, 20,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 马
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0, 90, 90, 90, 96, 90, 96, 90, 90, 90,  0,  0,  0,  0,
                    0,  0,  0, 90, 96,103, 97, 94, 97,103, 96, 90,  0,  0,  0,  0,
                    0,  0,  0, 92, 98, 99,103, 99,103, 99, 98, 92,  0,  0,  0,  0,
                    0,  0,  0, 93,108,100,107,100,107,100,108, 93,  0,  0,  0,  0,
                    0,  0,  0, 90,100, 99,103,104,103, 99,100, 90,  0,  0,  0,  0,
                    0,  0,  0, 90, 98,101,102,103,102,101, 98, 90,  0,  0,  0,  0,
                    0,  0,  0, 92, 94, 98, 95, 98, 95, 98, 94, 92,  0,  0,  0,  0,
                    0,  0,  0, 93, 92, 94, 95, 92, 95, 94, 92, 93,  0,  0,  0,  0,
                    0,  0,  0, 85, 90, 92, 93, 78, 93, 92, 90, 85,  0,  0,  0,  0,
                    0,  0,  0, 88, 85, 90, 88, 90, 88, 90, 85, 88,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 车
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,206,208,207,213,214,213,207,208,206,  0,  0,  0,  0,
                    0,  0,  0,206,212,209,216,233,216,209,212,206,  0,  0,  0,  0,
                    0,  0,  0,206,208,207,214,216,214,207,208,206,  0,  0,  0,  0,
                    0,  0,  0,206,213,213,216,216,216,213,213,206,  0,  0,  0,  0,
                    0,  0,  0,208,211,211,214,215,214,211,211,208,  0,  0,  0,  0,
                    0,  0,  0,208,212,212,214,215,214,212,212,208,  0,  0,  0,  0,
                    0,  0,  0,204,209,204,212,214,212,204,209,204,  0,  0,  0,  0,
                    0,  0,  0,198,208,204,212,212,212,204,208,198,  0,  0,  0,  0,
                    0,  0,  0,200,208,206,212,200,212,206,208,200,  0,  0,  0,  0,
                    0,  0,  0,194,206,204,212,200,212,204,206,194,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 炮
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,100,100, 96, 91, 90, 91, 96,100,100,  0,  0,  0,  0,
                    0,  0,  0, 98, 98, 96, 92, 89, 92, 96, 98, 98,  0,  0,  0,  0,
                    0,  0,  0, 97, 97, 96, 91, 92, 91, 96, 97, 97,  0,  0,  0,  0,
                    0,  0,  0, 96, 99, 99, 98,100, 98, 99, 99, 96,  0,  0,  0,  0,
                    0,  0,  0, 96, 96, 96, 96,100, 96, 96, 96, 96,  0,  0,  0,  0,
                    0,  0,  0, 95, 96, 99, 96,100, 96, 99, 96, 95,  0,  0,  0,  0,
                    0,  0,  0, 96, 96, 96, 96, 96, 96, 96, 96, 96,  0,  0,  0,  0,
                    0,  0,  0, 97, 96,100, 99,101, 99,100, 96, 97,  0,  0,  0,  0,
                    0,  0,  0, 96, 97, 98, 98, 98, 98, 98, 97, 96,  0,  0,  0,  0,
                    0,  0,  0, 96, 96, 97, 99, 99, 99, 97, 96, 96,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }, { // 兵(卒)
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  9,  9,  9, 11, 13, 11,  9,  9,  9,  0,  0,  0,  0,
                    0,  0,  0, 19, 24, 34, 42, 44, 42, 34, 24, 19,  0,  0,  0,  0,
                    0,  0,  0, 19, 24, 32, 37, 37, 37, 32, 24, 19,  0,  0,  0,  0,
                    0,  0,  0, 19, 23, 27, 29, 30, 29, 27, 23, 19,  0,  0,  0,  0,
                    0,  0,  0, 14, 18, 20, 27, 29, 27, 20, 18, 14,  0,  0,  0,  0,
                    0,  0,  0,  7,  0, 13,  0, 16,  0, 13,  0,  7,  0,  0,  0,  0,
                    0,  0,  0,  7,  0,  7,  0, 15,  0,  7,  0,  7,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
        }
    };

    // 帅(将)的步长
    static final byte[] ccKingDelta = {-16, -1, 1, 16};
    // 仕(士)的步长
    static final byte[] ccAdvisorDelta = {-17, -15, 15, 17};
    // 马的步长，以帅(将)的步长作为马腿
    static final byte[][] ccKnightDelta = {{-33, -31}, {-18, 14}, {-14, 18}, {31, 33}};
    // 马被将军的步长，以仕(士)的步长作为马腿
    static final byte[][] ccKnightCheckDelta = {{-33, -18}, {-31, -14}, {14, 31}, {18, 33}};


    // 棋盘初始设置
    static byte[] cucpcStartup = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 20, 19, 18, 17, 16, 17, 18, 19, 20, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0,
            0, 0, 0, 22, 0, 22, 0, 22, 0, 22, 0, 22, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 14, 0, 14, 0, 14, 0, 14, 0, 14, 0, 0, 0, 0,
            0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 12, 11, 10, 9, 8, 9, 10, 11, 12, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    // 判断棋子是否在棋盘中
    static final boolean IN_BOARD(int sq) {
        return ccInBoard[sq] != 0;
    }

    // 判断棋子是否在九宫中
    static final boolean IN_FORT(int sq) {
        return ccInFort[sq] != 0;
    }

    // 获得格子的横坐标
    static final int RANK_Y(int sq) {
        return sq >> 4;
    }

    // 获得格子的纵坐标
    static final int FILE_X(int sq) {
        return sq & 15;
    }

    // 根据纵坐标和横坐标获得格子
    static final int COORD_XY(int x, int y) {
        return x + (y << 4);
    }

    // 翻转格子
    static final int SQUARE_FLIP(int sq) {
        return 254 - sq;
    }

    // 纵坐标水平镜像
    static final int FILE_FLIP(int x) {
        return 14 - x;
    }

    // 横坐标垂直镜像
    static final int RANK_FLIP(int y) {
        return 15 - y;
    }

    // 格子水平镜像
    static final int MIRROR_SQUARE(int sq) {
        return COORD_XY(FILE_FLIP(FILE_X(sq)), RANK_Y(sq));
    }

    // 格子水平镜像
    static final int SQUARE_FORWARD(int sq, int sd) {
        return sq - 16 + (sd << 5);
    }

    // 走法是否符合帅(将)的步长
    static final boolean KING_SPAN(int sqSrc, int sqDst) {
        return ccLegalSpan[sqDst - sqSrc + 256] == 1;
    }

    // 走法是否符合仕(士)的步长
    static final boolean ADVISOR_SPAN(int sqSrc, int sqDst) {
        return ccLegalSpan[sqDst - sqSrc + 256] == 2;
    }

    // 走法是否符合相(象)的步长
    static final boolean BISHOP_SPAN(int sqSrc, int sqDst) {
        return ccLegalSpan[sqDst - sqSrc + 256] == 3;
    }

    // 相(象)眼的位置
    static final int BISHOP_PIN(int sqSrc, int sqDst) {
        return (sqSrc + sqDst) >> 1;
    }

    // 马腿的位置
    static final int KNIGHT_PIN(int sqSrc, int sqDst) {
        return sqSrc + ccKnightPin[sqDst - sqSrc + 256];
    }

    // 是否未过河
    static final boolean HOME_HALF(int sq, int sd) {
        return (sq & 0x80) != (sd << 7);
    }

    // 是否已过河
    static final boolean AWAY_HALF(int sq, int sd) {
        return (sq & 0x80) == (sd << 7);
    }

    // 是否在河的同一边
    static final boolean SAME_HALF(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0x80) == 0;
    }

    // 是否在同一行
    static final boolean SAME_RANK(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0xf0) == 0;
    }

    // 是否在同一列
    static final boolean SAME_FILE(int sqSrc, int sqDst) {
        return ((sqSrc ^ sqDst) & 0x0f) == 0;
    }

    // 获得红黑标记(红子是8，黑子是16)
    static final int SIDE_TAG(int sd) {
        return 8 + (sd << 3);
    }

    // 获得对方红黑标记
    static final int OPP_SIDE_TAG(int sd) {
        return 16 - (sd << 3);
    }

    // 获得走法的起点
    static final int SRC(int mv) {
        return mv & 255;
    }

    // 获得走法的终点
    static final int DST(int mv) {
        return mv >> 8;
    }

    // 根据起点和终点获得走法
    static final int MOVE(int sqSrc, int sqDst) {
        return sqSrc + sqDst * 256;
    }

    // 走法水平镜像
    static final int MIRROR_MOVE(int mv) {
        return MOVE(MIRROR_SQUARE(SRC(mv)), MIRROR_SQUARE(DST(mv)));
    }

    // 局面结构


    void Startup() {            // 初始化棋盘
        sdPlayer = 0;
        ucpcSquares = cucpcStartup;
    }

    void ChangeSide() {         // 交换走子方
        sdPlayer = 1 - sdPlayer;
    }

    void AddPiece(int sq, byte pc) { // 在棋盘上放一枚棋子
        ucpcSquares[sq] = pc;
    }

    void DelPiece(int sq) {         // 从棋盘上拿走一枚棋子
        ucpcSquares[sq] = 0;
    }

    byte MovePiece(int mv) {         // 走一步棋的棋子
        int sqSrc, sqDst;
        byte pc, pcCaptured;
        sqSrc = SRC(mv);
        sqDst = DST(mv);
        pcCaptured = ucpcSquares[sqDst];
        DelPiece(sqDst);
        pc = ucpcSquares[sqSrc];
        DelPiece(sqSrc);
        AddPiece(sqDst, pc);
        return pcCaptured;
    }


    void UndoMovePiece(int mv, byte pcCaptured) {  // 撤销走一步棋的棋子
        int sqSrc, sqDst;
        byte pc;
        sqSrc = SRC(mv);
        sqDst = DST(mv);
        pc = ucpcSquares[sqDst];
        DelPiece(sqDst);
        AddPiece(sqSrc, pc);
        AddPiece(sqDst, pcCaptured);
    }

    public byte pcretract;

    boolean MakeMove(int mv) {         // 走一步棋

        pcretract = MovePiece(mv);
        ChangeSide();
        return true;
    }

    // 生成所有走法
    int GenerateMoves(int mvs[]) {
        int i, j, nGenMoves, nDelta, sqSrc, sqDst;
        int pcSelfSide, pcOppSide, pcSrc, pcDst;
        // 生成所有走法，需要经过以下几个步骤：

        nGenMoves = 0;
        pcSelfSide = SIDE_TAG(sdPlayer);
        pcOppSide = OPP_SIDE_TAG(sdPlayer);
        for (sqSrc = 0; sqSrc < 256; sqSrc++) {

            // 1. 找到一个本方棋子，再做以下判断：
            pcSrc = ucpcSquares[sqSrc];
            if ((pcSrc & pcSelfSide) == 0) {
                continue;
            }

            // 2. 根据棋子确定走法
            switch (pcSrc - pcSelfSide) {
                case PIECE_KING:
                    for (i = 0; i < 4; i++) {
                        sqDst = sqSrc + ccKingDelta[i];
                        if (!IN_FORT(sqDst)) {
                            continue;
                        }
                        pcDst = ucpcSquares[sqDst];
                        if ((pcDst & pcSelfSide) == 0) {
                            mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                            nGenMoves++;
                        }
                    }
                    break;
                case PIECE_ADVISOR:
                    for (i = 0; i < 4; i++) {
                        sqDst = sqSrc + ccAdvisorDelta[i];
                        if (!IN_FORT(sqDst)) {
                            continue;
                        }
                        pcDst = ucpcSquares[sqDst];
                        if ((pcDst & pcSelfSide) == 0) {
                            mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                            nGenMoves++;
                        }
                    }
                    break;
                case PIECE_BISHOP:
                    for (i = 0; i < 4; i++) {
                        sqDst = sqSrc + ccAdvisorDelta[i];
                        if (!(IN_BOARD(sqDst) && HOME_HALF(sqDst, sdPlayer) && ucpcSquares[sqDst] == 0)) {
                            continue;
                        }
                        sqDst += ccAdvisorDelta[i];
                        pcDst = ucpcSquares[sqDst];
                        if ((pcDst & pcSelfSide) == 0) {
                            mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                            nGenMoves++;
                        }
                    }
                    break;
                case PIECE_KNIGHT:
                    for (i = 0; i < 4; i++) {
                        sqDst = sqSrc + ccKingDelta[i];
                        if (ucpcSquares[sqDst] != 0) {
                            continue;
                        }
                        for (j = 0; j < 2; j++) {
                            sqDst = sqSrc + ccKnightDelta[i][j];
                            if (!IN_BOARD(sqDst)) {
                                continue;
                            }
                            pcDst = ucpcSquares[sqDst];
                            if ((pcDst & pcSelfSide) == 0) {
                                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                nGenMoves++;
                            }
                        }
                    }
                    break;
                case PIECE_ROOK:
                    for (i = 0; i < 4; i++) {
                        nDelta = ccKingDelta[i];
                        sqDst = sqSrc + nDelta;
                        while (IN_BOARD(sqDst)) {
                            pcDst = ucpcSquares[sqDst];
                            if (pcDst == 0) {
                                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                nGenMoves++;
                            } else {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                    nGenMoves++;
                                }
                                break;
                            }
                            sqDst += nDelta;
                        }
                    }
                    break;
                case PIECE_CANNON:
                    for (i = 0; i < 4; i++) {
                        nDelta = ccKingDelta[i];
                        sqDst = sqSrc + nDelta;
                        while (IN_BOARD(sqDst)) {
                            pcDst = ucpcSquares[sqDst];
                            if (pcDst == 0) {
                                mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                nGenMoves++;
                            } else {
                                break;
                            }
                            sqDst += nDelta;
                        }
                        sqDst += nDelta;
                        while (IN_BOARD(sqDst)) {
                            pcDst = ucpcSquares[sqDst];
                            if (pcDst != 0) {
                                if ((pcDst & pcOppSide) != 0) {
                                    mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                    nGenMoves++;
                                }
                                break;
                            }
                            sqDst += nDelta;
                        }
                    }
                    break;
                case PIECE_PAWN:
                    sqDst = SQUARE_FORWARD(sqSrc, sdPlayer);
                    if (IN_BOARD(sqDst)) {
                        pcDst = ucpcSquares[sqDst];
                        if ((pcDst & pcSelfSide) == 0) {
                            mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                            nGenMoves++;
                        }
                    }
                    if (AWAY_HALF(sqSrc, sdPlayer)) {
                        for (nDelta = -1; nDelta <= 1; nDelta += 2) {
                            sqDst = sqSrc + nDelta;
                            if (IN_BOARD(sqDst)) {
                                pcDst = ucpcSquares[sqDst];
                                if ((pcDst & pcSelfSide) == 0) {
                                    mvs[nGenMoves] = MOVE(sqSrc, sqDst);
                                    nGenMoves++;
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return nGenMoves;
    }

    // 判断走法是否合理
    boolean LegalMove(int mv) {
        int sqSrc, sqDst, sqPin;
        int pcSelfSide, pcSrc, pcDst, nDelta;
        // 判断走法是否合法，需要经过以下的判断过程：

        // 1. 判断起始格是否有自己的棋子
        sqSrc = SRC(mv);
        pcSrc = ucpcSquares[sqSrc];
        pcSelfSide = SIDE_TAG(sdPlayer);
        if ((pcSrc & pcSelfSide) == 0) {
            return false;
        }

        // 2. 判断目标格是否有自己的棋子
        sqDst = DST(mv);
        pcDst = ucpcSquares[sqDst];
        if ((pcDst & pcSelfSide) != 0) {
            System.out.println("2");
            return false;
        }

        // 3. 根据棋子的类型检查走法是否合理
        switch (pcSrc - pcSelfSide) {
            case PIECE_KING:
                return IN_FORT(sqDst) && KING_SPAN(sqSrc, sqDst);
            case PIECE_ADVISOR:
                return IN_FORT(sqDst) && ADVISOR_SPAN(sqSrc, sqDst);
            case PIECE_BISHOP:
                return SAME_HALF(sqSrc, sqDst) && BISHOP_SPAN(sqSrc, sqDst) &&
                        ucpcSquares[BISHOP_PIN(sqSrc, sqDst)] == 0;
            case PIECE_KNIGHT:
                sqPin = KNIGHT_PIN(sqSrc, sqDst);
                return sqPin != sqSrc && ucpcSquares[sqPin] == 0;
            case PIECE_ROOK:
            case PIECE_CANNON:
                if (SAME_RANK(sqSrc, sqDst)) {
                    nDelta = (sqDst < sqSrc ? -1 : 1);
                } else if (SAME_FILE(sqSrc, sqDst)) {
                    nDelta = (sqDst < sqSrc ? -16 : 16);
                } else {
                    return false;
                }
                sqPin = sqSrc + nDelta;
                while (sqPin != sqDst && ucpcSquares[sqPin] == 0) {
                    sqPin += nDelta;
                }
                if (sqPin == sqDst) {
                    return pcDst == 0 || pcSrc - pcSelfSide == PIECE_ROOK;
                } else if (pcDst != 0 && pcSrc - pcSelfSide == PIECE_CANNON) {
                    sqPin += nDelta;
                    while (sqPin != sqDst && ucpcSquares[sqPin] == 0) {
                        sqPin += nDelta;
                    }
                    return sqPin == sqDst;
                } else {
                    return false;
                }
            case PIECE_PAWN:
                if (AWAY_HALF(sqDst, sdPlayer) && (sqDst == sqSrc - 1 || sqDst == sqSrc + 1)) {
                    return true;
                }
                return sqDst == SQUARE_FORWARD(sqSrc, sdPlayer);
            default:
                return true;
        }
    }

    // 判断是否被将军
    boolean Checked() {
        int i, j, sqSrc, sqDst;
        int pcSelfSide, pcOppSide, pcDst, nDelta;
        pcSelfSide = SIDE_TAG(sdPlayer);
        pcOppSide = OPP_SIDE_TAG(sdPlayer);
        // 找到棋盘上的帅(将)，再做以下判断：

        for (sqSrc = 0; sqSrc < 256; sqSrc++) {
            if (ucpcSquares[sqSrc] != pcSelfSide + PIECE_KING) {
                continue;
            }

            // 1. 判断是否被对方的兵(卒)将军
            if (ucpcSquares[SQUARE_FORWARD(sqSrc, sdPlayer)] == pcOppSide + PIECE_PAWN) {
                return true;
            }
            for (nDelta = -1; nDelta <= 1; nDelta += 2) {
                if (ucpcSquares[sqSrc + nDelta] == pcOppSide + PIECE_PAWN) {
                    return true;
                }
            }

            // 2. 判断是否被对方的马将军(以仕(士)的步长当作马腿)
            for (i = 0; i < 4; i++) {
                if (ucpcSquares[sqSrc + ccAdvisorDelta[i]] != 0) {
                    continue;
                }
                for (j = 0; j < 2; j++) {
                    pcDst = ucpcSquares[sqSrc + ccKnightCheckDelta[i][j]];
                    if (pcDst == pcOppSide + PIECE_KNIGHT) {
                        return true;
                    }
                }
            }

            // 3. 判断是否被对方的车或炮将军(包括将帅对脸)
            for (i = 0; i < 4; i++) {
                nDelta = ccKingDelta[i];
                sqDst = sqSrc + nDelta;
                while (IN_BOARD(sqDst)) {
                    pcDst = ucpcSquares[sqDst];
                    if (pcDst != 0) {
                        if (pcDst == pcOppSide + PIECE_ROOK || pcDst == pcOppSide + PIECE_KING) {
                            return true;
                        }
                        break;
                    }
                    sqDst += nDelta;
                }
                sqDst += nDelta;
                while (IN_BOARD(sqDst)) {
                    pcDst = ucpcSquares[sqDst];
                    if (pcDst != 0) {
                        if (pcDst == pcOppSide + PIECE_CANNON) {
                            return true;
                        }
                        break;
                    }
                    sqDst += nDelta;
                }
            }
            return false;
        }
        return false;
    }
}

