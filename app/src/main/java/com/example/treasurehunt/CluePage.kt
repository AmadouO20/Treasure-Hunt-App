/*
Amadou Diallo
OSU
CS 492
 */
package com.example.treasurehunt

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.treasurehunt.data.Clue
import com.example.treasurehunt.model.TreasureHuntModel

class CluePage : AppCompatActivity(), LocationListener {
    private lateinit var timerTextView: TextView
    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private lateinit var locationManager: LocationManager
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private lateinit var treasureHuntModel: TreasureHuntModel
    private lateinit var clueResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clue_page)

        treasureHuntModel = TreasureHuntModel()
        clueResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val nextClue = treasureHuntModel.getNextClue()
                if (nextClue != null) {
                    displayClue(nextClue)
                } else {
                    navigateToTreasureHuntCompleted()
                }
            }
        }

        timerTextView = findViewById(R.id.timerTextView)
        val foundItButton: Button = findViewById(R.id.foundItButton)
        val quitButton: Button = findViewById(R.id.quitButton)
        val hintButton: Button = findViewById(R.id.hintButton)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        requestLocationUpdates()

        startTime = System.currentTimeMillis()
        startTimer()

        foundItButton.setOnClickListener {
            checkLocation()
        }

        quitButton.setOnClickListener {
            finish()
        }

        hintButton.setOnClickListener {
            Toast.makeText(this, treasureHuntModel.getCurrentClue().hint, Toast.LENGTH_LONG).show()
        }

        displayClue(treasureHuntModel.getCurrentClue())
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        runnable = Runnable {
            val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
            timerTextView.text = "Time elapsed: ${elapsedTime}s"
            handler.postDelayed(runnable, 1000)
        }
        handler.post(runnable)
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                5f,
                this
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestLocationUpdates()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
    }

    private fun checkLocation() {
        val currentLocation = Geo(currentLatitude, currentLongitude)
        if (treasureHuntModel.checkLocation(currentLocation)) {
            if (treasureHuntModel.isFinalClue()) {
                navigateToTreasureHuntCompleted()
            } else {
                val intent = Intent(this, ClueSolvedPage::class.java)
                intent.putExtra("elapsedTime", System.currentTimeMillis() - startTime)
                clueResultLauncher.launch(intent)
            }
        } else {
            Toast.makeText(this, "Wrong location. Try again!", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToTreasureHuntCompleted() {
        val intent = Intent(this, TreasureHuntCompletedActivity::class.java)
        intent.putExtra("totalTime", System.currentTimeMillis() - startTime)
        startActivity(intent)
        finish()
    }

    private fun displayClue(clue: Clue) {
        val clueTextView: TextView = findViewById(R.id.clueTextView)
        clueTextView.text = clue.clueText
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
