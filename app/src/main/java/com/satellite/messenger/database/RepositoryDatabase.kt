package com.satellite.messenger.database


interface RepositoryDatabase {
    fun getRoomDatabase():DatabaseDao
    fun getConnectFirebase()
}

