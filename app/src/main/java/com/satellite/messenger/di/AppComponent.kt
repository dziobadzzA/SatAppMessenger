package com.satellite.messenger.di

import dagger.Component

@Component(modules = [ViewModelModule::class])
interface AppComponent {
    fun viewFactory():ViewModelFactory
}