package com.junho.brandirepos.koinexam

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.junho.brandirepos.di.networkModule
import com.junho.brandirepos.di.repositoryModule
import com.junho.brandirepos.koinexam.di.testModules
import org.junit.Rule
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

open class AbstractKoinTest: KoinTest {
    @get:Rule
    val rule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(networkModule, repositoryModule, testModules)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
}