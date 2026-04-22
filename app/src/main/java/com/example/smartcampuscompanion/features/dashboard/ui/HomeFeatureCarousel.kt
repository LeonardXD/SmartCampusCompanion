package com.example.smartcampuscompanion.features.dashboard.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.R
import com.example.smartcampuscompanion.core.ui.theme.AppSpacing
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

private data class FeatureSlide(
    val drawableRes: Int,
    val contentDescription: String,
    val title: String,
    val description: String
)

@Composable
fun HomeFeatureCarousel(
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val autoSlideIntervalMillis = 4500L
    val slides = remember {
        listOf(
            FeatureSlide(
                drawableRes = R.drawable.slide1,
                contentDescription = "Smart Campus Companion introduction",
                title = "Smart Campus Companion",
                description = "A smarter way to manage your campus life."
            ),
            FeatureSlide(
                drawableRes = R.drawable.slide2,
                contentDescription = "Tasks feature highlight",
                title = "Tasks",
                description = "Stay on top of assignments and important deadlines."
            ),
            FeatureSlide(
                drawableRes = R.drawable.slide3,
                contentDescription = "Updates feature highlight",
                title = "Updates",
                description = "Get the latest campus announcements and alerts."
            ),
            FeatureSlide(
                drawableRes = R.drawable.slide4,
                contentDescription = "Campus information feature highlight",
                title = "Campus Info",
                description = "Explore departments, contacts, and campus services."
            )
        )
    }
    val pagerState = rememberPagerState(pageCount = { slides.size })

    LaunchedEffect(pagerState, slides.size) {
        if (slides.size <= 1) return@LaunchedEffect
        while (true) {
            delay(autoSlideIntervalMillis)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % slides.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            pageSpacing = AppSpacing.Small
        ) { page ->
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
            val fraction = 1f - pageOffset.coerceIn(0f, 1f)
            val slide = slides[page]

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val scale = 0.92f + (0.08f * fraction)
                        scaleX = scale
                        scaleY = scale
                        alpha = 0.65f + (0.35f * fraction)
                    },
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                colors = CardDefaults.cardColors(containerColor = scheme.surface)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = slide.drawableRes),
                        contentDescription = slide.contentDescription,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth(),
                        color = scheme.surface.copy(alpha = 0.88f),
                        tonalElevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = AppSpacing.Large,
                                vertical = AppSpacing.Medium
                            ),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = slide.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = scheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = slide.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = scheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(slides.size) { index ->
                val isSelected = pagerState.currentPage == index
                val dotColor by animateColorAsState(
                    targetValue = if (isSelected) scheme.primary else scheme.outline.copy(alpha = 0.35f),
                    animationSpec = tween(durationMillis = 220),
                    label = "carouselDotColor"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 9.dp else 7.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }
    }
}
