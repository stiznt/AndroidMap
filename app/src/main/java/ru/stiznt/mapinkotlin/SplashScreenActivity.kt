package ru.stiznt.mapinkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import ru.stiznt.mapinkotlin.Tutorial.OnBoarding

class SplashScreenActivity : AppCompatActivity() {

    private var splash: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splash = findViewById(R.id.splash)
        splash?.alpha = 0f
        splash?.animate()?.setDuration(1000)?.alpha(1f)?.withEndAction {
            val i = Intent(this, OnBoarding::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}