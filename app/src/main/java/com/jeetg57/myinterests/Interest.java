package com.jeetg57.myinterests;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "interest3")
public class Interest {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int interestId;
    @ColumnInfo(name="interest_name")
    public String interestName;
    @ColumnInfo(name = "interest_desc")
    public String interestDescription;
    @ColumnInfo(name = "hours_per_week")
    public int hoursPerWeek;
    @ColumnInfo(name = "mins_per_week")
    public int minsPerWeek;
    @ColumnInfo(name = "hours_completed")
    public int hoursCompleted;
    @ColumnInfo(name = "mins_completed")
    public int minsCompleted;
    @ColumnInfo(name = "created_at")
    public long createdAt;
    @ColumnInfo(name = "total_time_completed_in_mins")
    public int totalTime;
}
