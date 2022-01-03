package app.dealux.orangerestaurant.ui.splashscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.dealux.orangerestaurant.repository.AuthDataStoreRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(val context: Context) : ViewModel() {

    private val repository = AuthDataStoreRepository(context)

    var readFromDataStore = repository.read().asLiveData()

    fun saveToDataStore(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.save(value)
    }


}