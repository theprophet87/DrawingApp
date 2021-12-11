package com.theprophet.kidsdrawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.net.URI
import java.nio.file.*

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

        private var mDrawPath: CustomPath? = null //a variable of CustomPath inner class
        private var mCanvasBitmap: Bitmap? = null //an instance of a Bitmap
        private var mDrawPaint: Paint? = null //Paint holds style and color info about how to draw
        private var mCanvasPaint: Paint? = null //instance of Canvas Paint View
        private var mBrushSize: Float = 0.toFloat() //variable for stroke/brush size
        private var color = Color.BLACK //variable to hold color of stroke
        private var canvas: Canvas? = null
        private val mPaths = ArrayList<CustomPath>()
        private val mUndoPaths = ArrayList<CustomPath>() //store paths for 'undo' operation

        init{
            setUpDrawing()
        }


        //this function will undo the last path that was drawn
        fun onClickUndo(){
            if(mPaths.size > 0){
                mUndoPaths.add(mPaths.removeAt(mPaths.size-1))
                invalidate() //will interally call onDraw method to redraw entire page
            }

        }

    //this function will reset the canvas
    fun onClickReset(){
        if(mPaths.size > 0){
            mPaths.clear()
            invalidate() //will interally call onDraw method to redraw entire page
        }

    }
        private fun setUpDrawing(){
            mDrawPaint = Paint()
            mDrawPath = CustomPath(color, mBrushSize)
            mDrawPaint!!.color = color
            mDrawPaint!!.style = Paint.Style.STROKE
            mDrawPaint!!.strokeJoin = Paint.Join.ROUND
            mDrawPaint!!.strokeCap = Paint.Cap.ROUND
            mCanvasPaint = Paint(Paint.DITHER_FLAG)
           // mBrushSize = 20.toFloat()


        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
            canvas = Canvas(mCanvasBitmap!!)
        }

    // Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path,mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!,mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action) {
            //when pressing the screen
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX,touchY)
                    }
                }


                }

            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)
                    }
                }

                }
            MotionEvent.ACTION_UP -> {
                    mPaths.add(mDrawPath!!)
                    mDrawPath = CustomPath(color, mBrushSize)
             }
            else -> return false
            }

            invalidate()
        return true
    }
        fun setSizeForBrush(newSize: Float){
            mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, resources.displayMetrics
                ) //adjusts proportionate to the size of the screen
            mDrawPaint!!.strokeWidth = mBrushSize
        }

        fun setColor(newColor: String){
            color = Color.parseColor(newColor)
            mDrawPaint!!.color = color


        }


        // An inner class for custom path with two params as color and stroke size.
        internal inner class CustomPath(var color: Int,
                                        var brushThickness: Float) : Path(){
                                            var test = "nothing"

        }



}