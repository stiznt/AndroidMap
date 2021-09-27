package ru.stiznt.mapinkotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import ru.stiznt.mapinkotlin.Tutorial.OnBoarding
import android.view.WindowManager
import android.widget.Toast

class SplashScreenActivity : AppCompatActivity() {

    private var splash: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        splash = findViewById(R.id.splash)
        splash?.alpha = 0f
        splash?.animate()?.setDuration(1000)?.alpha(1f)?.withEndAction {
            val i: Intent
            val sp = getPreferences(Context.MODE_PRIVATE)
            val hasVisited = sp.getBoolean("hasVisited", false)

            if(hasVisited) {
                i = Intent(this, MainActivity::class.java)
            }
            else {
                i = Intent(this, OnBoarding::class.java)
                val e: SharedPreferences.Editor = sp.edit()
                e.putBoolean("hasVisited", true)
                e.commit();
            }

            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}