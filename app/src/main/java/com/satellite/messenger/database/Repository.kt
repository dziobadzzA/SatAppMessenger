package com.satellite.messenger.database

import android.app.Application
import javax.inject.Inject

class Repository @Inject constructor():RepositoryDatabase {

    private lateinit var application:Application
    private lateinit var source:DatabaseDao

    fun setApp(application: Application) {
        this.application = application
        source = Database.getInstance(application.applicationContext).databaseDao
    }

    override fun getRoomDatabase(): DatabaseDao = source

    override fun getConnectFirebase() {
        TODO("Not yet implemented")
    }
}