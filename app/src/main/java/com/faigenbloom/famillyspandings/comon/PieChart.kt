import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val IndicatorSize = 48.0.dp
val ActiveIndicatorWidth = 4.0.dp

@Composable
fun CircularArc(
    progress: Float,
    offset: Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circularColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
) {
    val coercedOffset = offset.coerceIn(0f, 1f)
    val coercedProgress = progress.coerceIn(0f, 1f)
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx())
    }
    Canvas(
        modifier
            .progressSemantics(coercedProgress)
            .size(IndicatorSize - ActiveIndicatorWidth * 2),
    ) {
        // Start at 12 o'clock
        val startAngle = 270 + coercedOffset * 360f
        val sweep = coercedProgress * 360f
        drawArcIndicator(startAngle, sweep, color, stroke)
    }
}

private fun DrawScope.drawArcIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) {
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke,
    )
}
