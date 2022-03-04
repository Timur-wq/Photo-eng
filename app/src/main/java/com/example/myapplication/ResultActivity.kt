package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.w3c.dom.Text

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val size = intent.getIntExtra("size", 0).toFloat()
        val mistakes = intent.getIntExtra("mistakes", 0).toFloat()

        val tvG = findViewById<TextView>(R.id.textView6)
        val tvR = findViewById<TextView>(R.id.textView7)
        tvG.text = (size - mistakes).toInt().toString()
        tvR.text = mistakes.toInt().toString()
        val gP = findViewById<ImageView>(R.id.imageView5)
        val rP = findViewById<ImageView>(R.id.imageView6)

        val tvRes = findViewById<TextView>(R.id.textView5)
        tvRes.text = (100*(size-mistakes)/size).toString() + "%"

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.cpb)
        circularProgressBar.apply {
            progress = 0f
            setProgressWithAnimation(size-mistakes, 1000)
            progressMax = size
            progressBarWidth = 25f // in DP
            backgroundProgressBarWidth = 25f // in DP

            // Other
            //roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT

        }

        val btnDone = findViewById<FloatingActionButton>(R.id.done)
        btnDone.setColorFilter(Color.argb(255,255, 255, 255))
        btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val anim = AnimationUtils.loadAnimation(this, R.anim.scaling)
        gP.startAnimation(anim)
        rP.startAnimation(anim)
        tvG.startAnimation(anim)
        tvR.startAnimation(anim)

    }
}