package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.api.getToday
import kotlinx.coroutines.flow.Flow

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
@Dao
interface AsteroidDao {

    @Query("SELECT * FROM databaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): Flow<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM databaseAsteroid WHERE closeApproachDate < :today")
    fun deleteLastAsteroids(today: String = getToday()): Int

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByDate(startDate: String, endDate: String): Flow<List<DatabaseAsteroid>>

}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}