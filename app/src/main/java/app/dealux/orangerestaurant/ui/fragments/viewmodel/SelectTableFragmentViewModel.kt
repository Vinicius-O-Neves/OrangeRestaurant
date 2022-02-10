package app.dealux.orangerestaurant.ui.fragments.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dealux.orangerestaurant.repository.TableNumberDataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectTableFragmentViewModel @Inject constructor(context: Context) : ViewModel() {

    private val repository = TableNumberDataStoreRepository(context)

    fun saveTableNumber(tableNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save(tableNumber)
        }
    }

}