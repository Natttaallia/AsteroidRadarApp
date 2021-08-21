package com.udacity.asteroidradar.main

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getNextWeekDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mDatabase = getDatabase(application)
    private val mAsteroidsRepository = AsteroidsRepository(mDatabase)

    init {
        viewModelScope.launch {
            try {
                mAsteroidsRepository.refreshAsteroids()
                _pictureOfDay.value = getPicture()
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    application.getText(R.string.data_from_cache),
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                onViewAllAsteroidsClicked()
            }
        }
    }

    private var _progressBarVisibility = MutableLiveData<Int>()
    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private suspend fun getPicture(): PictureOfDay {
        lateinit var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = Network.services.getPictureOfDay().await()
        }
        return pictureOfDay
    }

    fun onViewWeekAsteroidsClicked() {
        _progressBarVisibility.value = View.VISIBLE

        viewModelScope.launch {
            mDatabase.asteroidDao.getAsteroidsByDate(getToday(), getNextWeekDay())
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                    _progressBarVisibility.value = View.GONE
                }
        }
    }

    fun onViewTodayAsteroidsClicked() {
        _progressBarVisibility.value = View.VISIBLE

        viewModelScope.launch {
            mDatabase.asteroidDao.getAsteroidsByDate(getToday(), getToday())
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                    _progressBarVisibility.value = View.GONE
                }
        }
    }

    fun onViewAllAsteroidsClicked() {
        _progressBarVisibility.value = View.VISIBLE

        viewModelScope.launch {
            mDatabase.asteroidDao.getAsteroids()
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                    _progressBarVisibility.value = View.GONE
                }
        }
    }
}