package com.yufeng.kviewgroup.view

import android.view.View

abstract class FlowViewApplyAdapter<T> {

    private var data: MutableList<T> = mutableListOf()

    fun refreshData(data: MutableList<T>) {
        this.data.clear()
        this.data.addAll(data)
    }

    abstract fun getFlowView(t: T): View
}