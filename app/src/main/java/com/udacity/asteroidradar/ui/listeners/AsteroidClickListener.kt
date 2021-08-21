package com.udacity.asteroidradar.ui.listeners

import com.udacity.asteroidradar.Asteroid

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
class AsteroidClickListener(val listener: (Asteroid) -> Unit) {
    fun onClick(data: Asteroid) = listener(data)
}