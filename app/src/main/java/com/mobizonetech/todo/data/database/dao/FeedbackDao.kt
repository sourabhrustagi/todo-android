package com.mobizonetech.todo.data.database.dao

import androidx.room.*
import com.mobizonetech.todo.data.database.entities.FeedbackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {
    @Query("SELECT * FROM feedback ORDER BY createdAt DESC")
    fun getAllFeedback(): Flow<List<FeedbackEntity>>

    @Query("SELECT * FROM feedback WHERE id = :feedbackId")
    suspend fun getFeedbackById(feedbackId: String): FeedbackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: FeedbackEntity)

    @Update
    suspend fun updateFeedback(feedback: FeedbackEntity)

    @Delete
    suspend fun deleteFeedback(feedback: FeedbackEntity)
} 