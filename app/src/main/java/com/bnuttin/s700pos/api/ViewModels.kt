package com.bnuttin.s700pos.api

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class ConnectionToken(
    val secret: String? = null
)

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    var statusConnectionToken by mutableStateOf("")
    var settingsSaved by mutableStateOf(false)
    var connectionToken: ConnectionToken by mutableStateOf(ConnectionToken())

    @SuppressLint("StaticFieldLeak")
    var context = getApplication<Application>().applicationContext
    val prefRepository = PrefRepository(context)

    fun getConnectionToken(){
        statusConnectionToken = "loading"
        viewModelScope.launch{
            try {
                connectionToken = POSApi.terminal.getConnectionToken()
                statusConnectionToken = "done"
            } catch (e: IOException) {
                statusConnectionToken = "error"
            }
        }
    }

    fun getSellerName(): String{
        return prefRepository.getSellerName()
    }

    fun updateSellerName(name: String){
        prefRepository.setSellerName(name)
    }

    fun getCurrency(): String{
        return prefRepository.getCurrency()
    }

    fun updateCurrency(currency: String){
        prefRepository.setCurrency(currency)
    }

    fun getBackendUrl(): String{
        return prefRepository.getBackendUrl()
    }

    fun updateBackendUrl(url: String){
        prefRepository.setBackendUrl(url)
    }
}
