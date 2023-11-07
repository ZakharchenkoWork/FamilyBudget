package com.faigenbloom.famillyspandings.comon

import CircularArc
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    chartData: List<PieChartData>,
    central: @Composable () -> Unit,
    label: @Composable (PaddingValues, Float) -> Unit,
) {
    var offset = 0f
    var size by remember { mutableStateOf(0) }
    for (data in chartData) {
        data.offset = offset
        offset = data.getPieValue()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.width
            },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            central()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(60.dp),
        ) {
            for (dataIndex in (chartData.size - 1) downTo 0) {
                CircleIndicator(
                    data = chartData[dataIndex],
                )
            }
        }
        if (size != 0) {
            for (dataIndex in (chartData.size - 1) downTo 0) {
                Label(
                    size,
                    chartData[dataIndex].getPieValuePercent(),
                    chartData[dataIndex].valuePercent,
                    label,
                )
            }
        }
    }
}

@Composable
fun CircleIndicator(
    data: PieChartData,
) {
    var currentProgress by remember { mutableStateOf(data.getRealValue()) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    if (currentProgress < data.getRealValue()) {
        LaunchedEffect(Unit) {
            scope.launch {
                loadProgress(currentProgress, data.getRealValue()) { progress ->
                    currentProgress = progress
                }
            }
        }
    }

    CircularArc(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        progress = currentProgress,
        offset = data.offset,
        strokeWidth = 20.dp,
        color = data.color.copy(alpha = 0.6f),
    )
    CircularArc(
        modifier = Modifier.fillMaxSize(),
        progress = currentProgress,
        offset = data.offset,
        strokeWidth = 40.dp,
        color = data.color,
    )
}

@Composable
fun Label(
    size: Int,
    sum: Float,
    currentSlice: Float,
    label: @Composable (PaddingValues, Float) -> Unit,
) {
    val hypo = size.toDouble() - 140.0
    if (hypo > 0) {
        val angle = getAngle(sum - currentSlice / 2)
        val x = abs(hypo * sin(angle * 0.0174533))
        val y = abs(hypo * cos(angle * 0.0174533))
        var startPadding = 0.0
        var topPadding = 0.0
        var endPadding = 0.0
        var bottomPadding = 0.0
        if (angle < 90) {
            startPadding = x
            bottomPadding = y
        } else if (angle >= 90 && angle < 180) {
            startPadding = x
            topPadding = y
        } else if (angle >= 180 && angle < 270) {
            topPadding = y
            endPadding = x
        } else if (angle >= 270 && angle < 360) {
            endPadding = x
            bottomPadding = y
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            label(
                PaddingValues(
                    start = startPadding.toDP(),
                    end = endPadding.toDP(),
                    top = topPadding.toDP(),
                    bottom = bottomPadding.toDP(),
                ),
                currentSlice,
            )
        }
    }
}

@Composable
fun Double.toDP(): Dp {
    return (this / LocalDensity.current.density).dp
}

fun getAngle(percent: Float): Float {
    return 3.6f * percent
}

/** Iterate the progress value */
suspend fun loadProgress(from: Float, to: Float, updateProgress: (Float) -> Unit) {
    var progress = from
    while (progress < to) {
        progress += 0.01f
        if (progress > to) {
            progress = to
        }
        updateProgress(progress)
        delay(33)
    }
}

data class PieChartData(val valuePercent: Float, val color: Color) {
    var offset = 0f
    fun getOffsetPercent(): Float {
        return offset * 100
    }

    fun getPieValue(): Float {
        return offset + valuePercent / 100f
    }

    fun getRealValue(): Float {
        return valuePercent / 100f
    }

    fun getPieValuePercent(): Float {
        return getOffsetPercent() + valuePercent
    }
}

@Preview
@Composable
fun PieChartPreview() = Surface {
    FamillySpandingsTheme {
        PieChart(
            chartData = listOf(
                PieChartData(30f, Color.Red),
                PieChartData(40f, Color.Blue),
                PieChartData(20f, Color.Green),
                PieChartData(10f, Color.Yellow),
            ),
            central = {
                Image(
                    modifier = Modifier.size(90.dp),
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "",
                )
            },
            label = { paddings, value ->
                Text(
                    modifier = Modifier
                        .padding(
                            paddings,
                        ),
                    text = "${value.toInt()}%",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            },
        )
    }
}
