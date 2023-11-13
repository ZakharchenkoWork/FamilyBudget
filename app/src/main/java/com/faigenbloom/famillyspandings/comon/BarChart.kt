import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    sideLabel: @Composable (index: Int) -> Unit,
    topLabel: @Composable () -> Unit,
    bottomRow: @Composable (rememberScrollState: LazyListState) -> Unit,
    rememberScrollState: LazyListState = rememberLazyListState(),
    barWidth: Dp = 80.dp,
    barLeftPadding: Dp = 16.dp,
    linesColor: Color = Color.Blue,
    bars: List<BarData>,
) {
    val linesNumber = 20
    var canvasHeight by remember { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    val canvasHeightDp = with(LocalDensity.current) { canvasHeight.toDp() }
    val rememberScrollStateInner: LazyListState = rememberLazyListState()
    val barWidthPx = with(LocalDensity.current) { barWidth.toPx() }
    val barLeftPaddingPx = with(LocalDensity.current) { barLeftPadding.toPx() }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned {
                    boxHeight = it.size.height.toFloat()
                },
        ) {
            Box {
                topLabel()
            }
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .height(canvasHeightDp)
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.End,
            ) {
                for (index in 10 downTo 0) {
                    if (index != 0 && index != 10) {
                        Box(
                            modifier = Modifier.height(38.5.dp),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            sideLabel(index)
                        }
                    }
                }
            }
            DrawBack(modifier, linesColor, linesNumber) {
                canvasHeight = it
            }

            LazyRow(
                modifier = Modifier
                    .height(canvasHeightDp)
                    .padding(start = 31.5.dp, top = 30.dp)
                    .syncronizer(
                        scope = rememberCoroutineScope(),
                        fromState = rememberScrollStateInner,
                        toState = rememberScrollState,
                    ),
                state = rememberScrollStateInner,
            ) {
                item {
                    Spacer(Modifier.width(40.dp))
                    Canvas(
                        modifier = Modifier
                            .width(
                                with(LocalDensity.current) {
                                    (((barLeftPaddingPx + barWidthPx) * bars.size) - barWidthPx / 2 + barLeftPaddingPx).toDp()
                                },
                            )
                            .fillMaxHeight(),
                    ) {
                        for ((index, bar) in bars.withIndex()) {
                            drawBar(
                                start = canvasHeight,
                                height = canvasHeight * bar.value,
                                width = barWidthPx,
                                leftOffset = barLeftPaddingPx + (barLeftPaddingPx + barWidthPx) * index,
                                color = bar.color,
                            )
                        }
                    }
                }
            }
        }
        bottomRow(
            rememberScrollStateInner,
        )
    }
}

@Composable
private fun DrawBack(
    modifier: Modifier,
    linesColor: Color,
    linesNumber: Int,
    onCanvasHeightChanged: (Float) -> Unit,
) {
    Canvas(
        modifier = modifier
            .padding(start = 60.dp, top = 30.dp)
            .onGloballyPositioned {
                onCanvasHeightChanged(it.size.height.toFloat())
            },
    ) {
        drawBack(
            size = size,
            linesColor = linesColor,
            linesNumber = linesNumber,
        )
    }
}

fun Modifier.syncronizer(
    scope: CoroutineScope,
    fromState: LazyListState,
    toState: LazyListState,
) = this.then(
    this.onGloballyPositioned {
        scope.launch {
            if (toState.firstVisibleItemScrollOffset != fromState.firstVisibleItemScrollOffset) {
                toState.scrollToItem(
                    0,
                    fromState.firstVisibleItemScrollOffset,
                )
            }
        }
    },
)

private fun DrawScope.drawBack(
    size: Size,
    linesColor: Color,
    linesNumber: Int,
) {
    drawLine(
        color = linesColor,
        strokeWidth = 8f,
        start = point(DELTA, 0f),
        end = point(DELTA, size.height),
    )
    drawLine(
        color = linesColor,
        strokeWidth = 8f,
        start = point(0f, size.height),
        end = point(MAX, size.height),
    )
    for (index in 0 until linesNumber) {
        val y = (size.height / linesNumber) * index.toFloat()

        drawLine(
            color = if (index % 2 == 0) linesColor else linesColor.copy(alpha = 0.5f),
            strokeWidth = 4f,
            start = point(0f, y),
            end = point(MAX, y),
        )
    }
}

private fun DrawScope.drawBar(
    start: Float,
    height: Float,
    width: Float,
    leftOffset: Float = 20f,
    color: Color,
) {
    drawLine(
        color = color,
        strokeWidth = width,
        start = point(leftOffset, start - height + DELTA),
        end = point(leftOffset, start - DELTA),
    )
}

private const val MAX = 3000f
private const val DELTA = 4f
fun point(x: Float, y: Float) = Offset.Zero.copy(x, y)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun BarPreview() = Scaffold { p ->
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        bars = listOf(
            BarData(1f, Color.Red),
            BarData(0.5f, Color.Blue),
            BarData(0.7f, Color.Green),
            BarData(0.1f, Color.Yellow),
            BarData(0.3f, Color.Gray),
            BarData(0.5f, Color.Cyan),
            BarData(0.2f, Color.Black),
            BarData(0.9f, Color.Magenta),
        ),
        topLabel = {
            Text(text = "$")
        },
        sideLabel = {
            Text(text = "$it")
        },
        bottomRow = {
            LazyRow(
                content = {
                    items(10) {
                        Text(text = "$it")
                    }
                },
            )
        },
    )
}

data class BarData(val value: Float, val color: Color)
