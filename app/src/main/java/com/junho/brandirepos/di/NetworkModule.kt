package com.junho.brandirepos.di

import com.junho.brandirepos.data.model.main.MainModel
import com.junho.brandirepos.data.model.main.MainModelImpl
import com.junho.brandirepos.data.model.main.service.MainService.Companion.SERVER_URL
import com.junho.brandirepos.data.repository.main.MainRepository
import com.junho.brandirepos.data.repository.main.MainRepositoryImpl
import com.junho.brandirepos.utils.RetrofitClient
import org.koin.dsl.module

val networkModule = module {
    fun provideRetrofitClient(): RetrofitClient = RetrofitClient(SERVER_URL)

    single { provideRetrofitClient().createMainService() }
    single<MainModel> { MainModelImpl(get()) }
    factory<MainRepository> { MainRepositoryImpl(get()) }
}