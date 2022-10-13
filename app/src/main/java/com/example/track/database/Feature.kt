package com.example.track.database

import androidx.room.*

@Entity(tableName = "features")
data class Feature(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "userId") var userId: String?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "feature") var floatArray: String?
)