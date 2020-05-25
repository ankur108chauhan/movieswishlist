package com.ankur.movienotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class AddEditActivity extends AppCompatActivity {

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_GENRE = "KEY_GENRE";
    public static final String KEY_YEAR = "KEY_YEAR";
    public static final String KEY_RATING = "KEY_RATING";
    public static final int GALLERY_CODE = 1;

    private TextView titleHeading;
    private EditText enterTitle;
    private EditText enterGenre;
    private EditText enterYear;
    private EditText enterRating;
    private Button saveButton;
    private Vibrator vibrator;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        titleHeading = findViewById(R.id.title_show);
        enterTitle = findViewById(R.id.movie_title);
        enterGenre = findViewById(R.id.movie_genre);
        enterYear = findViewById(R.id.movie_year);
        enterRating = findViewById(R.id.movie_rating);
        saveButton = findViewById(R.id.save_button);
        scrollView = findViewById(R.id.scrollView);

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_ID)) {
            setTitle("");
            titleHeading.setText(R.string.update_note);
            enterTitle.setText(intent.getStringExtra(KEY_TITLE));
            enterGenre.setText(intent.getStringExtra(KEY_GENRE));
            enterYear.setText(String.valueOf(intent.getIntExtra(KEY_YEAR, 1900)));
            enterRating.setText(String.valueOf(intent.getDoubleExtra(KEY_RATING, 0.0)));
        } else {
            setTitle("");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(80);
                saveMovie();
            }
        });

    }

    private void saveMovie() {

        String mTitle = enterTitle.getText().toString().trim();
        String mGenre = enterGenre.getText().toString().trim();
        String mYear = enterYear.getText().toString().trim();
        String mRating = enterRating.getText().toString().trim();

        if (mTitle.isEmpty() || mGenre.isEmpty() || mYear.isEmpty() || mRating.isEmpty()) {
            emptyValuesSnackbar();
            return;
        }

        int year = Integer.parseInt(mYear);
        int currentYear = 2020;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentYear = Calendar.getInstance().get(Calendar.YEAR);
        }

        if(year <= 1850 || year > currentYear + 2) {
            yearSnackbar();
            return;
        }
        double ratings = Double.parseDouble(mRating);

        if(ratings <= 0.0 || ratings > 10.0) {
            ratingSnackbar();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_TITLE, mTitle);
        intent.putExtra(KEY_GENRE, mGenre);
        intent.putExtra(KEY_YEAR, mYear);
        intent.putExtra(KEY_RATING, mRating);

        int id = getIntent().getIntExtra(KEY_ID, -1);
        if (id != -1) {
            intent.putExtra(KEY_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void emptyValuesSnackbar() {
        Snackbar snackbar = Snackbar.make(scrollView,R.string.blank_values,Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.snackbarBackground));
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.snackbarText));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        //set text to center
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //check orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            ScrollView.LayoutParams params=(ScrollView.LayoutParams)snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
        }
        snackbar.show();
    }

    public void ratingSnackbar() {
        Snackbar snackbar = Snackbar.make(scrollView,R.string.correct_rating,Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.snackbarBackground));
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.snackbarText));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        //set text to center
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //check orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            ScrollView.LayoutParams params=(ScrollView.LayoutParams)snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
        }
        snackbar.show();
    }

    public void yearSnackbar() {
        Snackbar snackbar = Snackbar.make(scrollView,R.string.correct_year,Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.snackbarBackground));
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.snackbarText));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        //set text to center
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //check orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            ScrollView.LayoutParams params=(ScrollView.LayoutParams)snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
        }
        snackbar.show();
    }
}
