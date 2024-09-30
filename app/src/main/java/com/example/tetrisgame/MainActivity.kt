package com.example.tetrisgame

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
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
    private lateinit var tetrisView: TetrisView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tetrisView = findViewById(R.id.tetrisView)

        // 1초마다 블록이 아래로 이동
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("TetrisGame", "블록이 아래로 이동 중")  // 블록 이동 로그
                tetrisView.tetrisGame.moveDown()
                tetrisView.invalidate()  // 화면 갱신
                handler.postDelayed(this, 1000)  // 1초마다 반복
            }
        }, 1000)

        // 블록을 왼쪽 또는 오른쪽으로 이동하는 사용자 입력 처리
        tetrisView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x < tetrisView.width / 2) {
                        tetrisView.tetrisGame.moveLeft()
                    } else {
                        tetrisView.tetrisGame.moveRight()
                    }
                    tetrisView.invalidate()  // 화면 갱신
                }
            }
            true
        }
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