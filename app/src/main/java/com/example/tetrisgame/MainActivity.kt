package com.example.tetrisgame

import TetrisGame
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tetrisgame.ui.theme.TetrisGameTheme

class MainActivity : AppCompatActivity() {
    lateinit var tetrisGame: TetrisGame  // tetrisGame 객체 선언
    lateinit var tetrisView: TetrisView  // tetrisView 객체 선언
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tetrisView = findViewById(R.id.tetrisView)
        tetrisGame = TetrisGame(this)  // TetrisGame 인스턴스 생성
        tetrisView.tetrisGame = tetrisGame  // TetrisView와 tetrisGame 연결

        // 1초마다 블록이 아래로 이동
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("TetrisGame", "블록이 아래로 이동 중")  // 블록 이동 로그
                tetrisView.tetrisGame.moveDown()
                tetrisView.invalidate()  // 화면 갱신
                handler.postDelayed(this, 1000)  // 1초마다 반복
            }
        }, 1000)

        tetrisView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 화면 상단 1/3을 터치하면 회전
                    if (event.y < tetrisView.height / 3) {
                        tetrisView.tetrisGame.rotateTetromino()  // 회전 함수 호출
                    }  // 화면 하단을 터치하면 블록을 바닥까지 이동
                    else if (event.y > tetrisView.height * 2 / 3) {
                        while (tetrisGame.canMoveDown()) {
                            tetrisGame.moveDown()  // 계속해서 아래로 이동
                        }
                        tetrisGame.lockTetromino()  // 바닥에 닿으면 블록 고정
                        tetrisView.invalidate()  // 화면 갱신
                    } else {
                        // 좌우 이동 처리 (화면 좌우를 터치)
                        if (event.x < tetrisView.width / 2) {
                            tetrisView.tetrisGame.moveLeft()  // 왼쪽 이동
                        } else {
                            tetrisView.tetrisGame.moveRight()  // 오른쪽 이동
                        }
                    }
                    tetrisView.invalidate()  // 화면 갱신
                }
            }
            true
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // 스페이스바를 눌렀을 때 블록을 바닥까지 한 번에 이동
            while (tetrisGame.canMoveDown()) {
                tetrisGame.moveDown()  // 계속해서 아래로 이동
            }
            tetrisGame.lockTetromino()  // 바닥에 닿으면 블록 고정
            tetrisView.invalidate()  // 화면 갱신
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TetrisGameTheme {
        Greeting("Android")
    }
}