package com.three.diet_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        // 비회원 로그인 시도
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 비회원 로그인 성공
                    Toast.makeText(this, "비회원 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                    // 플래시 화면 3초 후, 메인 화면으로 전환
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }, 3000)
                } else {
                    // 비회원 로그인 실패
                    Toast.makeText(this, "비회원 로그인에 실패하였습니다. 회원가입 해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }
}