/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.service.autofill.Transformation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Update
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*
import com.example.android.trackmysleepquality.formatNights

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application) {

    private val vieModelJob = Job()


    override fun onCleared() {
        super.onCleared()
        vieModelJob.cancel()

    }


 private   var uiScope =  CoroutineScope(Dispatchers.Main + vieModelJob)

private  var tonight = MutableLiveData<SleepNight?>()

    private var nights = database.geAllNight()


    var nightString = Transformations.map(nights){nights ->

        formatNights(nights, application.resources)

    }





    init {
 initializeTonight()
    }



    fun initializeTonight(){
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }





    private suspend fun getTonightFromDatabase(): SleepNight? {
            return withContext(Dispatchers.IO) {
                var night = database.getTonight()

                if (night?.endTimeMilli != night?.startTimeMilli){
                    night = null
                }
                night
            }
    }

    fun onStartTracking(){
        uiScope.launch {
            var newNight = SleepNight()
            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }



   suspend fun  insert(night: SleepNight) {
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }



    fun onStopTracking(){
        uiScope.launch {
            var oldNight = tonight.value ?: return@launch


            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
        }
    }



    suspend fun update(night: SleepNight){
        withContext(Dispatchers.IO){

            database.update(night)

        }
    }


    fun onClear(){
        uiScope.launch {
        clear()
            tonight.value = null
        }
    }

    suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }





}
