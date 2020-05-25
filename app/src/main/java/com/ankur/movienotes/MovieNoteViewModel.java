package com.ankur.movienotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieNoteViewModel extends AndroidViewModel {

    private MovieNoteRepository movieNoteRepository;
    private LiveData<List<MovieNote>> allMovies;

    public MovieNoteViewModel(@NonNull Application application) {
        super(application);
        movieNoteRepository = new MovieNoteRepository(application);
        allMovies = movieNoteRepository.getAllMovies();
    }

    public void insert(MovieNote movieNote) {
        movieNoteRepository.insert(movieNote);
    }

    public void update(MovieNote movieNote) {
        movieNoteRepository.update(movieNote);
    }

    public void deleteMovie(MovieNote movieNote) {
        movieNoteRepository.deleteMovie(movieNote);
    }

    public void deleteAllNotes() {
        movieNoteRepository.deleteAllNotes();
    }

    public LiveData<List<MovieNote>> getAllMovies() {
        return allMovies;
    }
}
