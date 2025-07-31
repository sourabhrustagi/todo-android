package com.mobizonetech.todo.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.mobizonetech.todo.domain.models.Task
import kotlin.random.Random

@Composable
fun RandomLogo(
    modifier: Modifier = Modifier,
    size: Int = 120,
    showAppName: Boolean = true
) {
    val context = LocalContext.current
    val randomSeed = remember { Random.nextInt(1, 1000) }
    
    // Use multiple image services for better reliability
    val imageUrls = remember {
        listOf(
            "https://picsum.photos/seed/$randomSeed/400/400",
            "https://source.unsplash.com/400x400/?nature,abstract&sig=$randomSeed",
            "https://api.lorem.space/image/album?w=400&h=400&hash=$randomSeed"
        )
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Container
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            var currentImageIndex by remember { mutableStateOf(0) }
            var imageLoadError by remember { mutableStateOf(false) }
            
            if (!imageLoadError) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrls[currentImageIndex])
                        .crossfade(true)
                        .build(),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                            // Try next image service or show fallback
                            if (currentImageIndex < imageUrls.size - 1) {
                                currentImageIndex++
                            } else {
                                imageLoadError = true
                            }
                        }
                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            } else {
                // Fallback to icon if all image services fail
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        if (showAppName) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Todo App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Stay organized, stay productive",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnimatedRandomLogo(
    modifier: Modifier = Modifier,
    size: Int = 120,
    showAppName: Boolean = true
) {
    var currentSeed by remember { mutableStateOf(Random.nextInt(1, 1000)) }
    
    LaunchedEffect(Unit) {
        // Change logo every 5 seconds for a dynamic effect
        while (true) {
            kotlinx.coroutines.delay(5000)
            currentSeed = Random.nextInt(1, 1000)
        }
    }
    
    val imageUrls = remember(currentSeed) {
        listOf(
            "https://picsum.photos/seed/$currentSeed/400/400",
            "https://source.unsplash.com/400x400/?nature,abstract&sig=$currentSeed",
            "https://api.lorem.space/image/album?w=400&h=400&hash=$currentSeed"
        )
    }
    
    val context = LocalContext.current
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Container
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            var currentImageIndex by remember { mutableStateOf(0) }
            var imageLoadError by remember { mutableStateOf(false) }
            
            if (!imageLoadError) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrls[currentImageIndex])
                        .crossfade(true)
                        .build(),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                            // Try next image service or show fallback
                            if (currentImageIndex < imageUrls.size - 1) {
                                currentImageIndex++
                            } else {
                                imageLoadError = true
                            }
                        }
                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            } else {
                // Fallback to icon if all image services fail
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        if (showAppName) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Todo App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Stay organized, stay productive",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SimpleRandomLogo(
    modifier: Modifier = Modifier,
    size: Int = 120,
    showAppName: Boolean = true
) {
    val context = LocalContext.current
    val randomSeed = remember { Random.nextInt(1, 1000) }
    val imageUrl = remember { "https://picsum.photos/seed/$randomSeed/400/400" }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Container
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        // Fallback to icon if image fails to load
                                        Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }
        
        if (showAppName) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Todo App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Stay organized, stay productive",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
} 