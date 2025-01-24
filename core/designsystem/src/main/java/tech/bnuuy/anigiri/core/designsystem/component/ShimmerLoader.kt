package tech.bnuuy.anigiri.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerLoader(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmerInfTrans")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "shimmerValAnim",
    )

    Card(
        shape = RoundedCornerShape(4.dp),
    ) { 
        Box(modifier) {
            val gradient = Brush.horizontalGradient(
                colors = listOf(Color.Gray.copy(alpha = alpha), Color.Gray.copy(alpha = 0.2f))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
//                        .height(50.dp)
//                        .fillMaxWidth()
                        .background(Color(0x00008888))
                )
            }
        }
    }
}
