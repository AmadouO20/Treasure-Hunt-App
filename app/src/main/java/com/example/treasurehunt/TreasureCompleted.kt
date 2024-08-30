/*
Amadou Diallo
OSU
CS 492
 */
package com.example.treasurehunt
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class TreasureHuntCompletedActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treasure_hunt_completed)
        val totalTimeTextView: TextView = findViewById(R.id.totalTimeTextView)
        val homeButton: Button = findViewById(R.id.homeButton)
        val totalTime = intent.getLongExtra("totalTime", 0L)
        totalTimeTextView.text = "Total Time: ${totalTime / 1000}s"
        homeButton.setOnClickListener {
            val intent = Intent(this, StartPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}
