package com.example.tetrisgame

import TetrisGame
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TetrisView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var gridWidth = 10  // 그리드 가로 크기
    private var gridHeight = 20 // 그리드 세로 크기
    private var cellSize: Int = 10  // 셀 크기

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 셀 크기를 화면에 맞추도록 설정
        cellSize = minOf(w / gridWidth, h / gridHeight)
    }

    private val paint = Paint().apply {
        color = Color.RED // 블록을 빨간색으로 설정
        style = Paint.Style.FILL
    }

    var tetrisGame = TetrisGame()  // 게임 상태 관리

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TetrisView", "onDraw 호출됨") // onDraw 호출 확인
        drawGrid(canvas)
        drawTetromino(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        for (x in 0 until gridWidth) {
            for (y in 0 until gridHeight) {
                canvas.drawRect(
                    (x * cellSize).toFloat(),
                    (y * cellSize).toFloat(),
                    ((x + 1) * cellSize).toFloat(),
                    ((y + 1) * cellSize).toFloat(),
                    paint
                )
            }
        }
    }

    // 블록 그리기
    private fun drawTetromino(canvas: Canvas) {
        val shape = tetrominoShapes[tetrisGame.currentTetromino]!!
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val x = (tetrisGame.positionX + j) * cellSize
                    val y = (tetrisGame.positionY + i) * cellSize
                    Log.d("TetrisView", "블록이 그려지는 위치: x=$x, y=$y")  // 블록 위치 확인
                    canvas.drawRect(
                        x.toFloat(),
                        y.toFloat(),
                        (x + cellSize).toFloat(),
                        (y + cellSize).toFloat(),
                        paint
                    )
                }
            }
        }
    }
}
