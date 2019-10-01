package com.amin.sample.ui.imgur

import androidx.lifecycle.MutableLiveData
import com.amin.sample.base.BaseResponseShutterStock
import com.amin.sample.base.BaseViewModel
import com.amin.sample.base.MyMaybeObserverShutter
import com.amin.sample.network.ImgurApi
import com.amin.sample.utils.LDR
import com.amin.sample.utils.androidThread
import com.amin.sample.utils.ioThread
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ImgurViewModel : BaseViewModel() {

    @Inject
    lateinit var api: ImgurApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val apiLiveData: MutableLiveData<LDR<BaseResponseShutterStock>> = MutableLiveData()

    private var subscription: Disposable? = null
    fun loadImages(
        page: Int = 1,
        section: ImgurFragAdapter.SectionType,
        sort: String = "viral"
    ) {
        api.getGalley(section.name, sort, page)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe(MyMaybeObserverShutter(loadingVisibility, apiLiveData))
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}