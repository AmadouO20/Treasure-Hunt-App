/*
Amadou Diallo
OSU
CS 492
 */
package com.example.treasurehunt.data
import com.example.treasurehunt.Geo

data class Clue(
    val clueNumber: Int,
    val clueText: String,
    val hint: String,
    val targetLocation: Geo
)

