package com.example.moviedb.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviedb.ActivityDetails;
import com.example.moviedb.Const;
import com.example.moviedb.converter.DateConverter;
import com.example.moviedb.R;
import com.example.moviedb.model.Movie;
import com.example.moviedb.model.MovieResponse;
import com.example.moviedb.retrofit.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatingAdapter extends RecyclerView.Adapter<TopRatingAdapter.Holder> {

    List<Movie> movies;
    private Context context;
    LayoutInflater layoutInflater;

    private static final int FOOTER_VIEW = 1;
    int pageNumber;

    public TopRatingAdapter(Context context, List<Movie> movies) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = movies;
        pageNumber = 2;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(v);
            return vh;
        }
        return new Holder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    public class FooterViewHolder extends Holder {
        Button button;

        public FooterViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.button_footer);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "loading more films...", Toast.LENGTH_SHORT).show();
                    Call<MovieResponse> call = ApiClient.getClient().getTopRatedMovies(pageNumber, Const.API_KEY);
                    pageNumber++;
                    call.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            try {
                                movies.addAll(response.body().getResults());
                                updateList(movies);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityDetails.class);
                context.startActivity(intent);
            }
        });
        try {
            Picasso.with(context).load(Const.IMAGE_POSTER_PATH_URL + movies
                    .get(position).getPosterPath()).placeholder(R.drawable.placeholder_item_recycler_view)
                    .resize(140, 170).centerCrop().into(holder.imageView);
            holder.textViewName.setText(movies.get(position).getTitle());
            holder.textViewYear.setText(DateConverter.formateDateFromstring("yyyy-MM-dd", "dd, MMMM, yyy",
                    movies.get(position).getReleaseDate()));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return movies.size() + 1;
    }

    public void updateList(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == movies.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewYear;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_for_item);
            textViewName = (TextView) itemView.findViewById(R.id.item_text_view_name);
            textViewYear = (TextView) itemView.findViewById(R.id.item_text_view_year);
        }
    }
}

