package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.view.View

class CanvasView (context: Context, val rects: MutableList<Rect>, val labels: MutableList<String>, var bm: Bitmap, val k: Float, val imageWidth: Int, val imageHeight: Int) : View(context) {
    lateinit var paint: Paint
    lateinit var textPaint: Paint
    init{
        paint = Paint()
        paint.style = Paint.Style.FILL

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED

        textPaint = Paint()
        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.RED
        textPaint.textSize = 30f
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //val clearPaint = Paint()
        //clearPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        //canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), clearPaint)
        //canvas.drawRect(100, 100, 500, 500, paint);

        //canvas.drawRect(100, 100, 500, 500, paint);
        //canvas?.drawBitmap(bm, Matrix(), paint)

        for (i in rects.indices) {
            canvas!!.drawRect(
                rects.get(i).left * k,
                rects.get(i).top * k,
                rects.get(i).right * k,
                rects.get(i).bottom * k,
                paint
            )
            canvas!!.drawText(labels.get(i), rects.get(i).left * k, rects.get(i).top * k + 40, textPaint)
        }
        invalidate()
    }
}