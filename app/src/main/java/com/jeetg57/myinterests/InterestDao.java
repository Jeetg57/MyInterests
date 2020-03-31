package com.jeetg57.myinterests;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InterestDao {

    @Query("SELECT * FROM interest3")
    List<Interest> getAllInterests();


    @Query("SELECT * FROM interest3 WHERE interest_name LIKE :interest")
    Interest findByName(String interest);

    @Query("SELECT * FROM interest3 WHERE _id=:id")
    Interest findById(int id);

    @Insert
    void addInterest(Interest interest);

    @Delete
    void deleteInterest(Interest interest);

    @Update
    void updateInterest(Interest interest);


    @Query("SELECT * FROM interest3")
    Cursor getAllInterestsCursor();

}
