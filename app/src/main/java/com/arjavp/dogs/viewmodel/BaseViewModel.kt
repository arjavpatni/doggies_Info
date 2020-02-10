package com.arjavp.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//will implement co-routines and extend ListViewModel from this class.
abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    //difference between ViewModel and AndroidViewModel is passing of "application" context.
    //we need application context here because regular context is very volatile and can be destroyed.
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get()= job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}