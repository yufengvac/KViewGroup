package com.yufeng.kviewgroup.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.yufeng.kviewgroup.R
import kotlin.math.max

/**
 * 这里的主构造注意下，不能使用一个单context参数作为主构造，否则padding属性不识别
 */
class FlowViewGroup constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    ViewGroup(context, attributeSet, defStyleAttr) {

    private var horizontalSpace = 0//横向间距
    private var verticalSpace = 0//竖向间距
    private val heightList: MutableList<Int> = mutableListOf()//高度集合
    private var viewList: MutableList<View> = mutableListOf()//view的集合
    private var allViewList: MutableList<MutableList<View>> = mutableListOf()
    private lateinit var flowViewApply: FlowViewApplyAdapter<String>//View提供者

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        -1
    ) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.FlowViewGroup)
        horizontalSpace =
            typeArray.getDimensionPixelSize(R.styleable.FlowViewGroup_horizontal_space, 30)
        verticalSpace =
            typeArray.getDimensionPixelSize(R.styleable.FlowViewGroup_vertical_space, 30)
        typeArray.recycle()

        flowViewApply = DefaultFlowViewApply(context)
    }

    fun setData(contentList: MutableList<String>) {
        removeAllViews()
        for (i in 0 until contentList.size) {
            val view = flowViewApply.getFlowView(contentList[i])
            addView(view)
        }
    }

    fun setData(contentList: MutableList<String>, flowViewApply: FlowViewApplyAdapter<String>) {
        removeAllViews()
        for (i in 0 until contentList.size) {
            val view = flowViewApply.getFlowView(contentList[i])
            addView(view)
        }
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var left = paddingLeft
        var top = paddingTop
        for (index in 0 until allViewList.size) {
            val viewList = allViewList[index]

            for (childView in viewList) {

                val right = left + childView.measuredWidth
                val bottom = top + childView.measuredHeight
                childView.layout(
                    left,
                    top,
                    right,
                    bottom
                )

                left = right + horizontalSpace
            }
            top += heightList[index] + verticalSpace
            left = paddingLeft
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewList.clear()
        allViewList.clear()

        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0
        var realWidth = 0
        var realHeight = 0
        for (index in 0 until childCount) {
            val childView = getChildAt(index);
            val childLayoutParams = childView.layoutParams;
            val childWidthMeasureSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight,
                childLayoutParams.width
            )
            val childHeightMeasureSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom,
                childLayoutParams.height
            )

            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            val childWidth =
                childView.measuredWidth
            val childHeight =
                childView.measuredHeight

            height = max(height, childHeight)//找到子view的最大高度，以子view中最大的那个高度为准

            width += (childWidth + horizontalSpace)
            if (width > widthMeasureSize - paddingLeft - paddingRight) {//超过父容器剩余的宽度

                realWidth =
                    maxOf(
                        width - (childWidth + horizontalSpace),
                        realWidth
                    )//此处要减一个 view的宽度和间距，因为多加view的宽度和间距
                realHeight += (height + verticalSpace)
                width = childWidth + horizontalSpace//不能归零，给它赋上当前view的宽度


                heightList.add(height)
                allViewList.add(viewList)
                viewList = mutableListOf()//此处不能调用viewList.clear方法
            }

            viewList.add(childView)
            if (index == childCount - 1) {//处理最后一行高度
                realHeight += childHeight//最后一行不加verticalSpace
                heightList.add(max(height, childHeight))
                allViewList.add(viewList)
            }
        }

        realHeight += (paddingTop + paddingBottom)//处理上下内边距

        setMeasuredDimension(
            if (widthMeasureMode == MeasureSpec.EXACTLY) widthMeasureSize else realWidth,
            if (heightMeasureMode == MeasureSpec.EXACTLY) heightMeasureSize else realHeight
        )
    }
}