package com.ankur.movienotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private MovieNoteViewModel movieNoteViewModel;
    private RecyclerView recyclerView;
    private MovieNoteAdapter movieNoteAdapter;
    private Vibrator vibrator;
    private LayoutInflater inflater;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private LiveData<List<MovieNote>> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        movieNoteAdapter = new MovieNoteAdapter(MainActivity.this);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(movieNoteAdapter);

        movieNoteViewModel = new ViewModelProvider(MainActivity.this).get(MovieNoteViewModel.class);
        movieNoteViewModel.getAllMovies().observe(this, new Observer<List<MovieNote>>() {
            @Override
            public void onChanged(@Nullable List<MovieNote> allMovies) {

                movieNoteAdapter.submitList(allMovies);
            }
        });

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(80);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        swipeDelete();

        movieNoteAdapter.setOnItemClickListener(new MovieNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieNote movieNote) {
                vibrator.vibrate(80);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.KEY_ID, movieNote.getId());
                intent.putExtra(AddEditActivity.KEY_TITLE, movieNote.getMovieTitle());
                intent.putExtra(AddEditActivity.KEY_GENRE, movieNote.getMovieGenre());
                intent.putExtra(AddEditActivity.KEY_YEAR, movieNote.getMovieYear());
                intent.putExtra(AddEditActivity.KEY_RATING, movieNote.getMovieRating());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    private void swipeDelete() {

        //to swipe delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    final View foregroundView = ((MovieNoteAdapter.MovieNoteViewHolder) viewHolder)
                            .viewForeground;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((MovieNoteAdapter.MovieNoteViewHolder) viewHolder)
                        .viewForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((MovieNoteAdapter.MovieNoteViewHolder) viewHolder)
                        .viewForeground;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((MovieNoteAdapter.MovieNoteViewHolder) viewHolder)
                        .viewForeground;

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                vibrator.vibrate(100);
                MovieNote movieNote = movieNoteAdapter.getMovieAt(viewHolder.getAdapterPosition());
                movieNoteViewModel.deleteMovie(movieNoteAdapter.getMovieAt(viewHolder.getAdapterPosition()));
                getDeleteSnackbar(movieNote);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            String mTitle = data.getStringExtra(AddEditActivity.KEY_TITLE);
            String mGenre = data.getStringExtra(AddEditActivity.KEY_GENRE);
            String mYear = data.getStringExtra(AddEditActivity.KEY_YEAR);
            String mRating = data.getStringExtra(AddEditActivity.KEY_RATING);

            MovieNote movieNote = new MovieNote(mTitle, mGenre, Integer.parseInt(mYear),
                                  Double.parseDouble(mRating));
            movieNoteViewModel.insert(movieNote);
            getSavedSnackbar(mTitle);
        }
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditActivity.KEY_ID, -1);
            if (id == -1) {
                getNotUpdateSnackbar();
                return;
            }

            String mTitle = data.getStringExtra(AddEditActivity.KEY_TITLE);
            String mGenre = data.getStringExtra(AddEditActivity.KEY_GENRE);
            String mYear = data.getStringExtra(AddEditActivity.KEY_YEAR);
            String mRating = data.getStringExtra(AddEditActivity.KEY_RATING);

            MovieNote movieNote = new MovieNote(mTitle, mGenre, Integer.parseInt(mYear),
                                  Double.parseDouble(mRating));
            movieNote.setId(id);
            movieNoteViewModel.update(movieNote);

            getUpdateSnackbar();
        }

        else {
            getDiscardSnackbar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                vibrator.vibrate(80);
                movieNoteViewModel.deleteAllNotes();
                deleteAllNotesSnackbar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getUpdateSnackbar() {
        snackbar = Snackbar.make(coordinatorLayout,R.string.note_updated,Snackbar.LENGTH_LONG);
        snackbarCustomization(snackbar, R.color.snackbarBackground);
    }

    public void getNotUpdateSnackbar() {
        snackbar = Snackbar.make(coordinatorLayout,"Note Can't Be Updated",Snackbar.LENGTH_LONG);
        snackbarCustomization(snackbar, R.color.snackbarBackground);
    }

    public void getDiscardSnackbar() {
        snackbar = Snackbar.make(coordinatorLayout,R.string.note_discarded,Snackbar.LENGTH_SHORT);
        snackbarCustomization(snackbar, R.color.snackbarBackground);
    }

    public void getSavedSnackbar(String movieTitle) {
        snackbar = Snackbar.make(coordinatorLayout,movieTitle + " " + "Added",Snackbar.LENGTH_LONG);
        snackbarCustomization(snackbar, R.color.snackbarBackground);
    }

    public void getDeleteSnackbar(final MovieNote movieNote) {
        snackbar = Snackbar.make(coordinatorLayout,movieNote.getMovieTitle() + " " + "Removed",Snackbar.LENGTH_LONG)
                  .setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                movieNoteViewModel.insert(movieNote);
                Snackbar snackbar1 = Snackbar.make(coordinatorLayout,R.string.undo_text,Snackbar.LENGTH_SHORT);
                snackbarCustomization(snackbar1, R.color.snackbarBackground);
            }
        });

        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.bg_row_background));
        TextView snackbarText = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(16);
        snackbarText.setTextColor(getResources().getColor(R.color.white));
        snackbarText.setTypeface(snackbarText.getTypeface(), Typeface.BOLD);

        TextView snackbarActionText = snackView.findViewById(com.google.android.material.R.id.snackbar_action);
        snackbarActionText.setTextSize(16);
        snackbarActionText.setTextColor(getResources().getColor(R.color.IMDBColor));
        snackbarActionText.setTypeface(snackbarActionText.getTypeface(), Typeface.BOLD);
        snackbar.show();
    }

    public void deleteAllNotesSnackbar() {
        snackbar = Snackbar.make(coordinatorLayout,R.string.all_movies_deleted,Snackbar.LENGTH_LONG);
        snackbarCustomization(snackbar, R.color.bg_row_background);
    }

    private void snackbarCustomization(Snackbar snackbar, int p) {
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(p));
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.snackbarText));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        //set text to center
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
}
