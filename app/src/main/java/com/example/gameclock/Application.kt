package com.example.gameclock

//import android.app.Application
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.preferencesDataStore
//import java.util.prefs.Preferences
//
//private const val USER_PREFERENCE_NAME = "user_preferences"
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCE_NAME)
//
//class Application : Application() {
//
//
//    lateinit var container: AppContainer
//
//    lateinit var userPreferencesRepository: UserPreferencesRepository
//
//    override fun onCreate() {
//        super.onCreate()
//        container = AppDataContainer(this)
//        userPreferencesRepository = UserPreferencesRepository(dataStore)
//
//    }
//}