package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
class AsteroidsRepository(private val database: AsteroidsDatabase) {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidResponseBody = Network.services.getAsteroids().await()
            val asteroids =
                NetworkAsteroidContainer(parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string())))
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }

    suspend fun deleteAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteLastAsteroids()
        }
    }
}