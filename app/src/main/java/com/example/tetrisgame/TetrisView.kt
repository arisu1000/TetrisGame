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
    private var cellSize: Int = 0  // 셀 크기

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 셀 크기를 화면에 맞추도록 설정
        cellSize = w / gridWidth
        Log.d("TetrisView", "cellsize $cellSize")
    }

    // 배경 색상
    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY  // 배경 색상 설정
        style = Paint.Style.FILL
    }

    // 블록 색상
    private val blockPaint = Paint().apply {
        color = Color.BLUE  // 블록 색상 설정
        style = Paint.Style.FILL
    }

    var tetrisGame = TetrisGame(context)  // 게임 상태 관리

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TetrisView", "onDraw 호출됨") // onDraw 호출 확인

        // 배경을 그리기
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        // 그리드에 고정된 블록들을 그리기
        drawGrid(canvas)

        // 블록을 그리기
        drawTetromino(canvas)

        if (tetrisGame.isGameOver) {
            val paint = Paint().apply {
                color = Color.RED
                textSize = 100f  // 텍스트 크기 설정
                textAlign = Paint.Align.CENTER
            }
            val xPos = width / 2
            val yPos = height / 2 - ((paint.descent() + paint.ascent()) / 2)
            canvas.drawText("Game Over", xPos.toFloat(), yPos.toFloat(), paint)
        }

    }

    fun drawGrid(canvas: Canvas) {
        for (y in tetrisGame.grid.indices) {
            for (x in tetrisGame.grid[y].indices) {
                if (tetrisGame.grid[y][x] == 1) {
                    canvas.drawRect(
                        (x * cellSize).toFloat(),
                        (y * cellSize).toFloat(),
                        ((x + 1) * cellSize).toFloat(),
                        ((y + 1) * cellSize).toFloat(),
                        blockPaint  // 고정된 블록 색상 적용
                    )
                }
            }
        }
    }


    // 블록 그리기
    private fun drawTetromino(canvas: Canvas) {
        val shape = tetrisGame.currentTetrominoShape  // 블록 모양을 currentTetrominoShape에서 가져옴
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
                        blockPaint  // 블록 색상 적용
                    )
                }
            }
        }
    }



}
