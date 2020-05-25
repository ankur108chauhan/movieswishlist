package com.ankur.movienotes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieNote.class}, version = 1)
public abstract class MovieNoteDatabase extends RoomDatabase {

    private static MovieNoteDatabase db;

    public abstract MovieNoteDao movieNoteDao();

    public static synchronized MovieNoteDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                     MovieNoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}
