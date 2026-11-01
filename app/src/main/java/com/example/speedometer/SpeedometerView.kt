package com.example.speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val cyan = Color.parseColor("#5ED9FF")
    private val cyanSoft = Color.parseColor("#D8F5FF")
    private val dark = Color.parseColor("#07151D")
    private val red = Color.parseColor("#FF4D4D")

    private var speedValue = 0f
    private var speedLimit = 50
    private var gear = "N"
    private val arcRect = RectF()

    private val darkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dark
        style = Paint.Style.FILL
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 12f
        color = cyan
        setShadowLayer(20f, 0f, 0f, cyan)
    }

    private val innerArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = cyan
        alpha = 160
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2.5f
        strokeCap = Paint.Cap.ROUND
        color = cyan
    }

    private val tickMajorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
        color = cyan
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cyanSoft
        textAlign = Paint.Align.CENTER
        typeface = Typeface.SANS_SERIF
        textSize = 28f
    }

    private val speedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = 154f
    }

    private val unitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cyanSoft
        textAlign = Paint.Align.CENTER
        typeface = Typeface.SANS_SERIF
        textSize = 42f
    }

    private val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = red
        style = Paint.Style.FILL
    }

    private val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = 36f
    }

    private val gearTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = 38f
    }

    fun setSpeed(value: Float) {
        speedValue = value.coerceIn(0f, 200f)
        invalidate()
    }

    fun setSpeedLimit(value: Int) {
        speedLimit = value
        invalidate()
    }

    fun setGear(value: String) {
        gear = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f + 20f
        val radius = (minOf(w, h) * 0.9f) / 2f

        canvas.drawColor(Color.BLACK)

        val arcRadius = radius * 0.92f
        arcRect.set(
            cx - arcRadius,
            cy - arcRadius,
            cx + arcRadius,
            cy + arcRadius,
        )

        canvas.drawArc(arcRect, 150f, 240f, false, glowPaint)
        canvas.drawArc(arcRect, 150f, 240f, false, innerArcPaint)

        val startAngle = 150f
        val sweepAngle = 240f
        val maxValue = 200f

        for (i in 0..200) {
            val ratio = i / maxValue
            val angle = startAngle + sweepAngle * ratio
            val rad = Math.toRadians(angle.toDouble())
            val innerR = arcRadius - 28f
            val outerR = arcRadius + 2f

            val x1 = cx + cos(rad) * innerR
            val y1 = cy + sin(rad) * innerR
            val x2 = cx + cos(rad) * outerR
            val y2 = cy + sin(rad) * outerR

            val major = i % 20 == 0
            val localPaint = if (major) tickMajorPaint else tickPaint
            localPaint.strokeWidth = if (major) 4f else 2.5f
            localPaint.color = if (i <= speedLimit) cyan else red

            canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), localPaint)
        }

        for (i in 0..10) {
            val value = i * 20
            val ratio = value / maxValue
            val angle = startAngle + sweepAngle * ratio
            val rad = Math.toRadians(angle.toDouble())
            val labelR = arcRadius * 0.74f

            val lx = cx + cos(rad) * labelR
            val ly = cy + sin(rad) * labelR + 12f

            textPaint.color = cyanSoft
            textPaint.textSize = 28f
            canvas.drawText(value.toString(), lx.toFloat(), ly.toFloat(), textPaint)
        }

        val indicatorRatio = speedValue / maxValue
        val indicatorAngle = startAngle + sweepAngle * indicatorRatio
        val needleLen = arcRadius * 0.72f
        val needleRad = Math.toRadians(indicatorAngle.toDouble())
        val needleX = cx + cos(needleRad) * needleLen
        val needleY = cy + sin(needleRad) * needleLen

        val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(cx, cy, needleX.toFloat(), needleY.toFloat(), needlePaint)

        val centerRadius = arcRadius * 0.66f
        canvas.drawCircle(cx, cy, centerRadius, darkPaint)

        val speedText = speedValue.roundToInt().toString()
        speedPaint.textSize = 154f
        canvas.drawText(speedText, cx, cy + 20f, speedPaint)

        unitPaint.textSize = 42f
        canvas.drawText("km/h", cx, cy + 76f, unitPaint)

        val badgeRadius = 52f
        val badgeCx = cx + arcRadius * 0.82f
        val badgeCy = cy + arcRadius * 0.46f
        canvas.drawCircle(badgeCx, badgeCy, badgeRadius, badgePaint)
        badgeTextPaint.textSize = 36f
        canvas.drawText(speedLimit.toString(), badgeCx, badgeCy + 12f, badgeTextPaint)

        val gearY = h - 90f
        val gearLeft = cx - 110f
        val gearBoxW = 72f
        val gearBoxH = 52f
        val gap = 16f

        val gearOptions = listOf("R", "N", "D")
        val selectedIndex = when (gear.uppercase()) {
            "R" -> 0
            "N" -> 1
            "D" -> 2
            else -> 1
        }

        for ((index, label) in gearOptions.withIndex()) {
            val x = gearLeft + index * (gearBoxW + gap)
            val box = RectF(x, gearY - gearBoxH, x + gearBoxW, gearY)

            val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = if (index == selectedIndex) cyan else Color.parseColor("#0E2C39")
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(box, 12f, 12f, boxPaint)

            val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = if (index == selectedIndex) cyan else Color.parseColor("#5A8390")
                style = Paint.Style.STROKE
                strokeWidth = 2f
            }
            canvas.drawRoundRect(box, 12f, 12f, border)

            gearTextPaint.color = if (index == selectedIndex) Color.WHITE else Color.parseColor("#B8E9F7")
            canvas.drawText(label, x + gearBoxW / 2f, gearY - 12f, gearTextPaint)
        }

        val parkingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            textSize = 36f
        }
        canvas.drawText("P", cx - 160f, gearY - 12f, parkingPaint)
    }
}
