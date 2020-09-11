package com.yufeng.kviewgroup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yufeng.kviewgroup.R
import com.yufeng.kviewgroup.view.FlowViewApplyAdapter
import com.yufeng.kviewgroup.view.FlowViewGroup

class MainActivity : AppCompatActivity() {

    private lateinit var flowViewGroup: FlowViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flowViewGroup = findViewById(R.id.flow_view_group)

        val list = mutableListOf<String>()
        list.add("袜子")
        list.add("披萨沙拉")
        list.add("足球比赛")
        list.add("店")
        list.add("星期天")
        list.add("手机保护壳")
        list.add("黑色")
        list.add("电脑")
        list.add("菜市场")
        flowViewGroup.setData(list, object : FlowViewApplyAdapter<String>() {
            override fun getFlowView(t: String): View {
                val view =
                    LayoutInflater.from(this@MainActivity).inflate(R.layout.item_tag_view, null)
                val tv = view.findViewById<TextView>(R.id.item_tag_view_tv)
                tv.text = t
                return view
            }
        })
    }
}