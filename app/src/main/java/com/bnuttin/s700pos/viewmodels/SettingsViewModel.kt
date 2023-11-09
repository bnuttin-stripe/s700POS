package com.bnuttin.s700pos.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    var settingsSaved by mutableStateOf(false)

    @SuppressLint("StaticFieldLeak")
    var context = getApplication<Application>().applicationContext
    val prefRepository = PrefRepository(context)

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