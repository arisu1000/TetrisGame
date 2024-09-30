import android.util.Log
import com.example.tetrisgame.Tetromino
import com.example.tetrisgame.tetrominoShapes

class TetrisGame {
    // 그리드의 가로 세로 크기를 정의
    val gridWidth = 10
    val gridHeight = 20

    var currentTetromino = Tetromino.I // 현재 블록
    var positionX = 4  // 블록의 X 위치 (그리드 중앙에서 시작)
    var positionY = 0  // 블록의 Y 위치 (상단에서 시작)

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
        val shape = tetrominoShapes[currentTetromino]!!
        return positionY + shape.size < gridHeight
    }

    // 블록을 고정하고 새로운 블록 생성
    fun lockTetromino() {
        // 그리드에 현재 블록을 고정하는 로직
        // 새로운 블록을 생성
        currentTetromino = Tetromino.values().random()
        positionX = 4
        positionY = 0
    }

    fun spawnNewTetromino() {
        // 무작위로 새로운 블록 모양 선택 (Tetromino 타입)
        currentTetromino = Tetromino.values().random()

        // 새로운 블록의 초기 위치 설정
        positionX = (gridWidth / 2) - (tetrominoShapes[currentTetromino]!![0].size / 2)
        positionY = 0  // 블록은 맨 위에서부터 떨어짐

        // 만약 새로운 블록이 생성될 때 이미 블록이 있는 위치라면 게임 오버 처리
        if (!canMoveDown()) {
            Log.d("TetrisGame", "게임 오버!")
            // 게임 오버 처리 (필요시 추가 로직 구현)
        }
    }

}
