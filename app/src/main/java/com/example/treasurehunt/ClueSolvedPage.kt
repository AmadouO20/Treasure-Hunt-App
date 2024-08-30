/*
Amadou Diallo
OSU
CS 492
 */
package com.example.treasurehunt
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClueSolvedPage : AppCompatActivity() {
    private var elapsedTime: Long = 0L
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clue_solved_page)
        elapsedTime = intent.getLongExtra("elapsedTime", 0L)
        val elapsedTimeTextView: TextView = findViewById(R.id.elapsedTimeTextView)
        elapsedTimeTextView.text = "Elapsed Time: ${elapsedTime / 1000}s"
        val continueButton: Button = findViewById(R.id.continueButton)
        continueButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("nextClue", 2)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

