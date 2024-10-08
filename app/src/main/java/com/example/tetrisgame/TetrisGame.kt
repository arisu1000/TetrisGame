import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.tetrisgame.TetrisView
import com.example.tetrisgame.Tetromino
import com.example.tetrisgame.tetrominoShapes

class TetrisGame(val context: Context, val tetrisView: TetrisView) {
    // 그리드의 가로 세로 크기를 정의
    val gridWidth = 10
    val gridHeight = 20
    var grid = Array(gridHeight) { IntArray(gridWidth) { 0 } }  // 0: 빈 셀, 1: 블록 고정

    var score: Int = 0  // 점수를 저장할 변수

    // 현재 블록 모양을 나타내는 2D 배열
    var currentTetrominoShape: Array<IntArray> = tetrominoShapes[Tetromino.I]!! // 블록 모양 초기화
    var positionX = 4
    var positionY = 0

    // 블록을 아래로 이동
    fun moveDown() {
        if (canMoveDown()) {
            positionY++
            Log.d("TetrisGame", "블록이 아래로 이동: positionY=$positionY")
        } else {
            Log.d("TetrisGame", "블록이 바닥에 닿음, 새로운 블록 생성")
            lockTetromino()
            spawnNewTetromino()
        }
    }

    // 블록을 왼쪽으로 이동
    fun moveLeft() {
        if (canMoveLeft()) {
            positionX--
        }
    }

    // 블록을 오른쪽으로 이동
    fun moveRight() {
        if (canMoveRight()) {
            positionX++
        }
    }

    fun canMoveLeft(): Boolean {
        val shape = currentTetrominoShape
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    if (positionX + j - 1 < 0 || grid[positionY + i][positionX + j - 1] == 1) {
                        return false  // 그리드 왼쪽 끝이거나 블록이 있을 경우 이동 불가
                    }
                }
            }
        }
        return true
    }

    fun canMoveRight(): Boolean {
        val shape = currentTetrominoShape
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    if (positionX + j + 1 >= gridWidth || grid[positionY + i][positionX + j + 1] == 1) {
                        return false  // 그리드 오른쪽 끝이거나 블록이 있을 경우 이동 불가
                    }
                }
            }
        }
        return true
    }

    // 블록을 회전 (단순하게 가로 세로 뒤바꾸기)
    fun rotate() {
        // 블록 모양 회전 구현
    }

    // 충돌 감지: 블록이 그리드 아래 경계를 넘는지 확인
    fun canMoveDown(): Boolean {
        val shape = currentTetrominoShape
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    // 그리드 바닥이거나 다른 블록과 충돌하면 이동 불가
                    if (positionY + i + 1 >= gridHeight || grid[positionY + i + 1][positionX + j] == 1) {
                        return false
                    }
                }
            }
        }
        return true
    }

    // 블록을 고정하고 새로운 블록 생성
    fun lockTetromino() {
        val shape = currentTetrominoShape
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    // 그리드에 블록을 고정
                    grid[positionY + i][positionX + j] = 1
                }
            }
        }
        checkAndClearLines()  // 줄이 완성되면 삭제
    }

    fun checkAndClearLines() {
        var linesCleared = 0
        for (y in grid.indices) {
            if (grid[y].all { it == 1 }) {  // 한 줄이 모두 채워졌을 때
                clearLine(y)
                linesCleared++
            }
        }
        score += linesCleared * 100  // 삭제된 줄 수에 따라 점수 증가
    }


    fun clearLine(row: Int) {
        // 해당 줄을 삭제하고 위의 줄을 모두 한 줄씩 아래로 이동
        for (y in row downTo 1) {
            grid[y] = grid[y - 1].clone()
        }
        grid[0] = IntArray(gridWidth) { 0 }  // 최상단 줄은 빈 줄로 초기화
    }

    fun spawnNewTetromino() {
        if (isGameOver) return

        currentTetrominoShape = tetrominoShapes[Tetromino.values().random()]!!
        positionX = gridWidth / 2 - currentTetrominoShape[0].size / 2
        positionY = 0

        // 만약 새로운 블록이 생성될 때 이미 블록이 있는 위치라면 게임 오버 처리
        if (!canMoveDown()) {
            Log.d("TetrisGame", "게임 오버!")
            // 게임 오버 처리 (필요시 추가 로직 구현)
            gameOver()
        }
    }

    var isGameOver = false

    fun gameOver() {
        isGameOver = true
        // 추가 게임 종료 로직 (예: 화면 갱신)
        if (score > loadHighScore()) {
            saveHighScore()
        }

        showRestartDialog()  // 다시 시작 여부 묻기
    }

    fun resetGame() {
        grid = Array(gridHeight) { IntArray(gridWidth) { 0 } }  // 그리드 초기화
        score = 0  // 점수 초기화
        isGameOver = false  // 게임 오버 상태 초기화
        spawnNewTetromino()  // 새로운 블록 생성
    }

    fun showRestartDialog() {
        AlertDialog.Builder(tetrisView.context)
            .setTitle("Game Over")
            .setMessage("게임을 다시 시작하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                resetGame()  // 게임 상태 초기화
                tetrisView.invalidate()  // 화면 갱신
            }
            .setNegativeButton("아니오") { _, _ ->
                (context as Activity).finish()  // 액티비티 종료
            }
            .show()
    }


    // 회전 함수
    fun rotateTetromino() {
        currentTetrominoShape = rotateTetrominoLogic(currentTetrominoShape)
    }

    // 회전 로직 함수
    fun rotateTetrominoLogic(shape: Array<IntArray>): Array<IntArray> {
        val rows = shape.size
        val cols = shape[0].size
        val rotatedShape = Array(cols) { IntArray(rows) }

        for (i in shape.indices) {
            for (j in shape[i].indices) {
                rotatedShape[j][rows - 1 - i] = shape[i][j]
            }
        }
        return rotatedShape
    }

    fun saveHighScore() {
        val sharedPref = context.getSharedPreferences("TetrisGame", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("HIGH_SCORE", score)
            apply()
        }
    }

    fun loadHighScore(): Int {
        val sharedPref = context.getSharedPreferences("TetrisGame", Context.MODE_PRIVATE)
        return sharedPref.getInt("HIGH_SCORE", 0)  // 저장된 최고 점수 반환, 없으면 0 반환
    }
}
