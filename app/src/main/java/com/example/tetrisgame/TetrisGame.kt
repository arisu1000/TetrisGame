import android.util.Log
import com.example.tetrisgame.Tetromino
import com.example.tetrisgame.tetrominoShapes

class TetrisGame {
    // 그리드의 가로 세로 크기를 정의
    val gridWidth = 10
    val gridHeight = 20
    var grid = Array(gridHeight) { IntArray(gridWidth) { 0 } }  // 0: 빈 셀, 1: 블록 고정


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
        positionX--
    }

    // 블록을 오른쪽으로 이동
    fun moveRight() {
        positionX++
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
        for (y in grid.indices) {
            if (grid[y].all { it == 1 }) {  // 한 줄이 모두 채워졌을 때
                clearLine(y)
            }
        }
    }

    fun clearLine(row: Int) {
        // 해당 줄을 삭제하고 위의 줄을 모두 한 줄씩 아래로 이동
        for (y in row downTo 1) {
            grid[y] = grid[y - 1].clone()
        }
        grid[0] = IntArray(gridWidth) { 0 }  // 최상단 줄은 빈 줄로 초기화
    }

    fun spawnNewTetromino() {
        currentTetrominoShape = tetrominoShapes[Tetromino.values().random()]!!
        positionX = gridWidth / 2 - currentTetrominoShape[0].size / 2
        positionY = 0

        // 만약 새로운 블록이 생성될 때 이미 블록이 있는 위치라면 게임 오버 처리
        if (!canMoveDown()) {
            Log.d("TetrisGame", "게임 오버!")
            // 게임 오버 처리 (필요시 추가 로직 구현)
        }
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

}
