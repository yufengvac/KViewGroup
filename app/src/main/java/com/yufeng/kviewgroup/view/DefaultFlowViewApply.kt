package com.yufeng.kviewgroup.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.yufeng.kviewgroup.R

class DefaultFlowViewApply constructor(ctx: Context) : FlowViewApplyAdapter<String>() {

    private val context = ctx
    override fun getFlowView(t: String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag_view, null)
        val tv = view.findViewById<TextView>(R.id.item_tag_view_tv)
        tv.text = t
        return view
    }
}