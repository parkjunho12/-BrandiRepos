package com.junho.brandirepos.koinexam.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.junho.brandirepos.data.model.main.service.MainService
import com.junho.brandirepos.data.repository.main.MainRepository
import com.junho.brandirepos.koinexam.AbstractKoinTest
import com.junho.brandirepos.koinexam.data.FakeData
import com.junho.brandirepos.ui.main.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import junit.framework.Assert.assertEquals
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertThat
import org.junit.Test
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainCaseTest: AbstractKoinTest(){


    val mainUseCase: MainUseCase by inject()

    @Test
    fun getImagesData() {
        val testScheduler = TestScheduler()
        val testObserver = TestObserver<Long>()
        val minTicker = Observable.interval(1, TimeUnit.MINUTES, testScheduler)
        minTicker.subscribe(testObserver)
        mainUseCase.getImageDatas("하나남수", MainService.Companion.Sort.ACCURACY, 1)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .awaitTerminalEvent()
    }

}