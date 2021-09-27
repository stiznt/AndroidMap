package ru.stiznt.mapinkotlin.Tutorial

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ru.stiznt.mapinkotlin.MainActivity
import ru.stiznt.mapinkotlin.R

class OnBoarding : AppCompatActivity() {
    //Variables
    var viewPager: ViewPager? = null
    var dotsLayout: LinearLayout? = null
    var sliderAdapter: SliderAdapter? = null
    var next: Button? = null
    lateinit var dots: Array<TextView?>
    var currentPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        /*val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )*/
        setContentView(R.layout.activity_on_boarding)
        //Hooks
        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dots)
        next = findViewById(R.id.start_btn)

        next?.setOnClickListener(View.OnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        })

        //Call adapter
        sliderAdapter = SliderAdapter(this)
        viewPager?.setAdapter(sliderAdapter)

        //Dots
        addDots(0)
        viewPager?.addOnPageChangeListener(changeListener)
    }

    private fun addDots(position: Int) {
        dots = arrayOfNulls(4)
        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("•")
            dots[i]!!.textSize = 35f
            dotsLayout!!.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[position]!!.setTextColor(resources.getColor(R.color.iconColor))
        }
    }

    var changeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
            currentPos = position
            if (position == 0) {
                next?.setText("Пропустить");
            } else if (position == 1) {
                next?.setText("Пропустить");
            } else if (position == 2) {
                next?.setText("Пропустить");
            } else {
                next?.setText("Начать");
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}