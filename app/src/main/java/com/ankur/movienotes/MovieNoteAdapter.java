package com.ankur.movienotes;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class MovieNoteAdapter extends ListAdapter<MovieNote, MovieNoteAdapter.MovieNoteViewHolder>{

    private OnItemClickListener listener;
    private Vibrator vibrator;
    private Context context;

    //@NonNull DiffUtil.ItemCallback<MovieNote> diffCallback
    public MovieNoteAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<MovieNote> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MovieNote>() {
        @Override
        public boolean areItemsTheSame(MovieNote oldItem, MovieNote newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(MovieNote oldItem, MovieNote newItem) {
            return  oldItem.getId() == (newItem.getId()) &&
                    oldItem.getMovieTitle().equals(newItem.getMovieTitle()) &&
                    oldItem.getMovieGenre().equals(newItem.getMovieGenre()) &&
                    oldItem.getMovieYear() == newItem.getMovieYear() &&
                    oldItem.getMovieRating() == newItem.getMovieRating();
        }
    };

    @NonNull
    @Override
    public MovieNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return new MovieNoteViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieNoteViewHolder holder, int position) {
        MovieNote currentMovie = getItem(position);
        holder.title.setText(currentMovie.getMovieTitle());
        holder.genre.setText(currentMovie.getMovieGenre());
        holder.year.setText(String.valueOf(currentMovie.getMovieYear()));
        holder.rating.setText("IMDB: " + String.valueOf(currentMovie.getMovieRating()));
    }

    public MovieNote getMovieAt(int position) {
        return  getItem(position);
    }

    public class MovieNoteViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private TextView year;
        private TextView rating;
        public CardView viewForeground;
        public RelativeLayout viewBackground;

        public MovieNoteViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;
            vibrator = (Vibrator)context.getSystemService(VIBRATOR_SERVICE);

            title = view.findViewById(R.id.m_title);
            genre = view.findViewById(R.id.m_genre);
            year = view.findViewById(R.id.m_year);
            rating = view.findViewById(R.id.m_rating);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            viewForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MovieNote movieNote);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
