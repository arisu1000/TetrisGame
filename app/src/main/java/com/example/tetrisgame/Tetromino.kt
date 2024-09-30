package com.example.tetrisgame

enum class Tetromino {
    I, O, T, L, Z
}

val tetrominoShapes = mapOf(
    Tetromino.I to arrayOf(
        intArrayOf(1, 1, 1, 1)
    ),
    Tetromino.O to arrayOf(
        intArrayOf(1, 1),
        intArrayOf(1, 1)
    ),
    Tetromino.T to arrayOf(
        intArrayOf(0, 1, 0),
        intArrayOf(1, 1, 1)
    ),
    Tetromino.L to arrayOf(
        intArrayOf(1, 0),
        intArrayOf(1, 0),
        intArrayOf(1, 1)
    ),
    Tetromino.Z to arrayOf(
        intArrayOf(1, 1, 0),
        intArrayOf(0, 1, 1)
    )
)
