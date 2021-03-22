package com.junho.brandirepos.di

import com.junho.brandirepos.data.model.main.MainModel
import com.junho.brandirepos.data.model.main.MainModelImpl
import com.junho.brandirepos.data.repository.main.MainRepository
import com.junho.brandirepos.data.repository.main.MainRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MainModel> { MainModelImpl(get()) }
    factory<MainRepository> { MainRepositoryImpl(get()) }
}