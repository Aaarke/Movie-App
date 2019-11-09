package com.example.mvvmbaseproject.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.mvvmbaseproject.R

class FixedImageView : ImageView {

    private var aspectRatio = 0f
    private var aspectRatioSource: AspectRatioSource? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.FixedImageView)
        aspectRatio = a.getFloat(R.styleable.FixedImageView_imageAspectRatio, 0f)
        a.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var localRatio = aspectRatio

        if (localRatio.toDouble() == 0.0 && aspectRatioSource != null
            && aspectRatioSource!!.height > 0) {
            localRatio = aspectRatioSource!!.width.toFloat() / aspectRatioSource!!.height.toFloat()
        }

        if (localRatio.toDouble() == 0.0) {
            super.onMeasure(widthSpec, heightSpec)
        } else {
            var lockedWidth = MeasureSpec.getSize(widthSpec)
            var lockedHeight = MeasureSpec.getSize(heightSpec)

            require(!(lockedWidth == 0 && lockedHeight == 0)) { "Both width and height cannot be zero -- watch out for scrollable containers" }

            // Get the padding of the border background.
            val hPadding = paddingLeft + paddingRight
            val vPadding = paddingTop + paddingBottom

            // Resize the preview frame with correct aspect ratio.
            lockedWidth -= hPadding
            lockedHeight -= vPadding

            if (lockedHeight > 0 && lockedWidth > lockedHeight * localRatio) {
                lockedWidth = (lockedHeight * localRatio + .5).toInt()
            } else {
                lockedHeight = (lockedWidth / localRatio + .5).toInt()
            }

            // Add the padding of the border.
            lockedWidth += hPadding
            lockedHeight += vPadding

            // Ask children to follow the new preview dimension.
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(lockedWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(lockedHeight, MeasureSpec.EXACTLY))
        }
    }



    interface AspectRatioSource {
        val width: Int

        val height: Int
    }


}
