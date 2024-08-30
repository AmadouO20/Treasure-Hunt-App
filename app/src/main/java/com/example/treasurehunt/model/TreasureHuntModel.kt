/*
Amadou Diallo
OSU
CS 492
 */
package com.example.treasurehunt.model

import com.example.treasurehunt.data.Clue
import com.example.treasurehunt.Geo

class TreasureHuntModel {
    private val clues: List<Clue> = listOf(
        Clue(
            clueNumber = 1,
            clueText = "Me and my family go here on weekends to watch football and soccer games",
            hint = "Hint: The tickets here are expensive.",
            targetLocation = Geo(33.7553, -84.4006)
        ),
        Clue(
            clueNumber = 2,
            clueText = "This is a famous place to eat fine dining.",
            hint = "Hint: They specialize in steaks.",
            targetLocation = Geo(33.8388, -84.3791)
        )
    )

    private var currentClueIndex: Int = 0

    fun getCurrentClue(): Clue {
        return clues[currentClueIndex]
    }

    fun getNextClue(): Clue? {
        return if (currentClueIndex < clues.size - 1) {
            clues[++currentClueIndex]
        } else {
            null
        }
    }

    fun isFinalClue(): Boolean {
        return currentClueIndex == clues.size - 1
    }

    fun checkLocation(currentLocation: Geo): Boolean {
        val targetLocation = getCurrentClue().targetLocation
        return currentLocation.haversine(targetLocation) < 0.005
    }

}
