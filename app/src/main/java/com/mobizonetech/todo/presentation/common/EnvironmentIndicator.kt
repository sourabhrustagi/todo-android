package com.mobizonetech.todo.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.BuildConfig
import com.mobizonetech.todo.R

@Composable
fun EnvironmentIndicator(
    modifier: Modifier = Modifier
) {
    // Only show in debug builds
    if (BuildConfig.DEBUG) {
        val environmentColor = when (BuildConfig.ENVIRONMENT) {
            "MOCK" -> Color(0xFFFF6B35)
            "DEVELOPMENT" -> Color(0xFF2196F3)
            "PRODUCTION" -> Color(0xFF4CAF50)
            else -> Color(0xFF9E9E9E)
        }
        
        val environmentText = when (BuildConfig.ENVIRONMENT) {
            "MOCK" -> "MOCK"
            "DEVELOPMENT" -> "DEV"
            "PRODUCTION" -> "PROD"
            else -> "UNKNOWN"
        }
        
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = environmentColor.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = environmentText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun EnvironmentInfoCard(
    modifier: Modifier = Modifier
) {
    if (BuildConfig.DEBUG) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EnvironmentIndicator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.environment_info),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Base URL: ${BuildConfig.BASE_URL}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Mock API: ${if (BuildConfig.USE_MOCK_API) "Enabled" else "Disabled"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "API Logging: ${if (BuildConfig.ENABLE_API_LOGGING) "Enabled" else "Disabled"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 