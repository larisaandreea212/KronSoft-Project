package com.fmi_unitbv2026.kronsoft_frontend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fmi_unitbv2026.kronsoft_frontend.data.models.Evolution

@Composable
fun EvolutionChartComponent(data: List<Evolution>) {
    val lightBorderBlue = Color(0xFFD1E4F5)
    val colorRed = Color(0xFFE53935)
    val colorOrange = Color(0xFFFF9800)
    val colorGreen = Color(0xFF4CAF50)

    var selectedIndex by remember { mutableStateOf(-1) }
    val textMeasurer = rememberTextMeasurer()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, lightBorderBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Evolution History",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = "Risk Level: 0 - Safe, 100 - Critical",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 65.dp, bottom = 60.dp, top = 60.dp, end = 50.dp)
                        .pointerInput(data) {
                            detectTapGestures { offset ->
                                if (data.isNotEmpty()) {
                                    val width = size.width
                                    val spacingX = width / (data.size - 1).coerceAtLeast(1)
                                    val index = ((offset.x + (spacingX / 2)) / spacingX).toInt().coerceIn(0, data.size - 1)
                                    selectedIndex = index
                                }
                            }
                        }
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    if (canvasWidth <= 0f || canvasHeight <= 0f) return@Canvas

                    val maxScore = 100f
                    val spacingX = canvasWidth / (data.size - 1).coerceAtLeast(1)

                    val steps = listOf(0, 25, 50, 75, 100)
                    steps.forEach { step ->
                        val y = canvasHeight - (step * canvasHeight / maxScore)

                        drawLine(
                            color = Color(0xFFF0F0F0),
                            start = Offset(0f, y),
                            end = Offset(canvasWidth, y),
                            strokeWidth = 1.dp.toPx()
                        )

                        drawText(
                            textMeasurer = textMeasurer,
                            text = step.toString(),
                            style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                            topLeft = Offset(-40.dp.toPx(), y - 12.dp.toPx())
                        )
                    }

                    if (data.size >= 2) {
                        val points = data.mapIndexed { index, item ->
                            Offset(
                                x = index * spacingX,
                                y = canvasHeight - (item.score.coerceIn(0, 100).toFloat() / 100f * canvasHeight)
                            )
                        }

                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                        }

                        val riskGradient = Brush.verticalGradient(
                            0.0f to colorRed,
                            0.25f to colorRed,
                            0.45f to colorOrange,
                            0.70f to colorGreen,
                            1.0f to colorGreen
                        )

                        drawPath(
                            path = path,
                            brush = riskGradient,
                            style = Stroke(width = 4.dp.toPx())
                        )

                        points.forEachIndexed { index, point ->
                            val isSelected = index == selectedIndex
                            val score = data[index].score
                            val pColor = when {
                                score <= 40 -> colorGreen
                                score <= 75 -> colorOrange
                                else -> colorRed
                            }

                            drawCircle(
                                color = if (isSelected) Color.Black else pColor,
                                radius = if (isSelected) 7.dp.toPx() else 5.dp.toPx(),
                                center = point
                            )

                            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = point)

                            if (isSelected) {
                                val tooltipText = "${data[index].dateTime}: ${data[index].score}%"
                                val textLayout = textMeasurer.measure(
                                    text = tooltipText,
                                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                )

                                val paddingX = 12f
                                val paddingY = 8f
                                val rectSize = Size(textLayout.size.width + paddingX * 2, textLayout.size.height + paddingY * 2)

                                val xOffset = when (index) {
                                    data.size - 1 -> point.x - rectSize.width
                                    else -> point.x - rectSize.width / 2 
                                }

                                val rectOffset = Offset(xOffset, point.y - rectSize.height - 35f)

                                drawRoundRect(
                                    color = Color.Black.copy(alpha = 0.9f),
                                    topLeft = rectOffset,
                                    size = rectSize,
                                    cornerRadius = CornerRadius(8f)
                                )

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = tooltipText,
                                    style = TextStyle(fontSize = 12.sp, color = Color.White),
                                    topLeft = Offset(rectOffset.x + paddingX, rectOffset.y + paddingY)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EvolutionChartPreview() {
    val testData = listOf(
        Evolution("10 May", 20),
        Evolution("12 May", 35),
        Evolution("15 May", 55),
        Evolution("18 May", 85),
        Evolution("20 May", 45),
        Evolution("22 May", 98)
    )

    MaterialTheme {
        Box(modifier = Modifier.padding(10.dp)) {
            EvolutionChartComponent(data = testData)
        }
    }
}