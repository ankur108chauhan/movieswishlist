package com.ankur.movienotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieNoteRepository {

    private MovieNoteDao movieNoteDao;
    private LiveData<List<MovieNote>> allMovies;

    public MovieNoteRepository(Application application) {
        MovieNoteDatabase db = MovieNoteDatabase.getInstance(application);
        movieNoteDao = db.movieNoteDao();
        allMovies = movieNoteDao.getAllMovies();
    }

    public void insert(MovieNote movieNote) {
        new InsertNoteAsyncTask(movieNoteDao).execute(movieNote);
    }

    public void update(MovieNote movieNote) {
        new UpdateNoteAsyncTask(movieNoteDao).execute(movieNote);
    }

    public void deleteMovie(MovieNote movieNote) {
        new DeleteNoteAsyncTask(movieNoteDao).execute(movieNote);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(movieNoteDao).execute();
    }

    public LiveData<List<MovieNote>> getAllMovies() {
        return allMovies;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<MovieNote, Void, Void> {
        private MovieNoteDao movieNoteDao;

        private InsertNoteAsyncTask(MovieNoteDao movieNoteDao) {
            this.movieNoteDao = movieNoteDao;
        }

        @Override
        protected Void doInBackground(MovieNote... movieNotes) {
            movieNoteDao.insert(movieNotes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<MovieNote, Void, Void> {
        private MovieNoteDao movieNoteDao;

        private UpdateNoteAsyncTask(MovieNoteDao movieNoteDao) {
            this.movieNoteDao = movieNoteDao;
        }

        @Override
        protected Void doInBackground(MovieNote... movieNotes) {
            movieNoteDao.update(movieNotes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<MovieNote, Void, Void> {
        private MovieNoteDao movieNoteDao;

        private DeleteNoteAsyncTask(MovieNoteDao movieNoteDao) {
            this.movieNoteDao = movieNoteDao;
        }

        @Override
        protected Void doInBackground(MovieNote... movieNotes) {
            movieNoteDao.deleteMovie(movieNotes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieNoteDao movieNoteDao;

        private DeleteAllNotesAsyncTask(MovieNoteDao movieNoteDao) {
            this.movieNoteDao = movieNoteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieNoteDao.deleteAllMovies();
            return null;
        }
    }
}
