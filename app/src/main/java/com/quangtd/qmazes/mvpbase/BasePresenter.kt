package com.quangtd.qstudio.mvpbase

import android.content.Context
import io.reactivex.disposables.Disposable

/**
 * Created by QuangTD
 * on 1/12/2018.
 */
abstract class BasePresenter<V : IBaseView> {
    private var iView: V? = null
    protected var mListDisposable = ArrayList<Disposable?>()

    open fun getIView(): V? {
        return iView
    }

    fun attachView(v: V) {
        iView = v
    }

    fun detachView() {
        iView = null
    }

    open fun onDestroy() {
        for (disposable in mListDisposable) {
            disposable?.let {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }

        }
    }

    fun getContext(): Context? {
        return this.iView?.getViewContext()
    }

    abstract fun onInit()

}