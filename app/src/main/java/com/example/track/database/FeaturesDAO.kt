package com.example.track.database

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FeaturesDAO {
    @Query("SELECT * FROM features")
    suspend fun getFeatures(): List<Feature>

    @Insert(onConflict = IGNORE)
    fun insertFeature(feature: Feature)

    @Update(onConflict = REPLACE)
    suspend fun updateFeature(feature: Feature)

    @Delete
    suspend fun deleteFeature(feature: Feature)

    @Query("SELECT * FROM features WHERE id =:id")
    fun isFavorite(id: Long): Feature
}