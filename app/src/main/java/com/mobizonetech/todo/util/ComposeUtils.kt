package com.mobizonetech.todo.util

import androidx.compose.runtime.Composable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun LoadingContent(
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = onRefresh,
        content = content
    )
}