package com.andreasgift.myclock.DI

import android.app.Application
import com.andreasgift.myclock.AlarmData.AlarmROOMDatabase
import com.andreasgift.myclock.AlarmData.AlarmRepository
import com.andreasgift.myclock.AlarmData.AlarmRepositoryImpl
import com.andreasgift.myclock.AlarmData.AlarmViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyClockApplication : Application() {

    val appModule = module {
        // instantiate AlarmDAO
        single { AlarmROOMDatabase.getDatabase(this@MyClockApplication).alarmDao() }

        // instantiate AlarmRepositoryImpl
        single<AlarmRepository> { AlarmRepositoryImpl(get()) }

        // get AlarmViewModel
        viewModel { AlarmViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyClockApplication)
            modules(appModule)
        }
    }
}