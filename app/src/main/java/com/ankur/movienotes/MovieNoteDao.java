package com.ankur.movienotes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieNoteDao {

    @Insert
    void insert(MovieNote movieNote);

    @Update
    void update(MovieNote movieNote);

    @Delete
    void deleteMovie(MovieNote movieNote);

    @Query("DELETE FROM movie_table")
    void deleteAllMovies();

    @Query("SELECT * FROM movie_table ORDER BY movieRating DESC")
    LiveData<List<MovieNote>> getAllMovies();
}
