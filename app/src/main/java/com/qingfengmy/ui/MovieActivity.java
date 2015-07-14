package com.qingfengmy.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingfengmy.R;
import com.qingfengmy.databinding.ActivityMovieBinding;
import com.qingfengmy.databinding.ItemMovieBinding;
import com.qingfengmy.ui.network.entities.Movie;

import java.util.List;

/**
 * Created by Administrator on 2015/7/14.
 */
public class MovieActivity extends BaseActivity {

    private ActivityMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        binding.toolbar.setTitle("电影");
        setSupportActionBar(binding.toolbar);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MovieActivity.this)
                        .title(R.string.search)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (!TextUtils.isEmpty(input)) {
                                    doSearch(input.toString());
                                }
                            }
                        }).show();
            }
        });

        doSearch(getString(R.string.default_search_tag));
    }

    private void doSearch(String keyword) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Movie.searchMovies(keyword, new Movie.IMovieResponse<List<Movie>>() {
            @Override
            public void onData(List<Movie> books) {
                MyAdapter mAdapter = new MyAdapter(MovieActivity.this, books);
                binding.recyclerView.setAdapter(mAdapter);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BindingHolder> {
        //private final int mBackground;
        private List<Movie> mMovies;

        private final TypedValue mTypedValue = new TypedValue();


        public class BindingHolder extends RecyclerView.ViewHolder {
            private ItemMovieBinding binding;

            public BindingHolder(View v) {
                super(v);
            }

            public ItemMovieBinding getBinding() {
                return binding;
            }

            public void setBinding(ItemMovieBinding binding) {
                this.binding = binding;
            }
        }

        public MyAdapter(Context context, List<Movie> movies) {
            mMovies = movies;
        }

        @Override
        public MyAdapter.BindingHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            ItemMovieBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_movie,
                    parent,
                    false);
            BindingHolder holder = new BindingHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            Movie movie = mMovies.get(position);
            Glide.with(MovieActivity.this)
                    .load(movie.getImages().getMedium())
                    .fitCenter()
                    .into(holder.binding.ivMovie);
            holder.binding.setVariable(com.qingfengmy.BR.movie, movie);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }
}
