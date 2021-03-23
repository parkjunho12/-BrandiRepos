package com.junho.brandirepos.koinexam.di


import com.junho.brandirepos.koinexam.usecase.MainUseCase
import org.koin.dsl.module

val testModules = module {
    factory<MainUseCase> { MainUseCase(get()) }
}