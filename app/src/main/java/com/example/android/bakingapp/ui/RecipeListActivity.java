package com.example.android.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

    private CountingIdlingResource mActivityCountingIdlingResource;

    private RecipeListViewModel mViewModel;

    private List<Recipe> mRecipeList;
    private RecipeListAdapter mAdapter;

    @BindView(R.id.recipe_list_rv)
    RecyclerView mRecipeListRV;

    @BindView(R.id.empty_view) TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mActivityCountingIdlingResource = new CountingIdlingResource("RecipeListActivity");

        ButterKnife.bind(this);

        initViewModel();
        initUI();
    }

    /**
     * Solution to determining screen density found
     * @ https://stackoverflow.com/questions/6465680/how-to-determine-the-screen-width-in-terms-of-dp-or-dip-at-runtime-in-android
     */
    private boolean isLargeScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth >= 600;
    }

    @VisibleForTesting
    public CountingIdlingResource getRepositoryCountingIdlingResource() {
        if (mViewModel == null) {
            initViewModel();
        }
        return mViewModel.getRepositoryCountingIdlingResource();
    }

    @VisibleForTesting
    public CountingIdlingResource getActivityCountingIdlingResource() {
        return mActivityCountingIdlingResource;
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        mViewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipeList = recipes;
                if (recipes == null || recipes.size() == 0) {
                    showEmptyView();
                } else {
                    mAdapter.setRecipeList(recipes);
                    showRecipeList();
                }
            }
        });
    }

    private void initUI() {
        mAdapter = new RecipeListAdapter(mRecipeList, this);

        mRecipeListRV.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager;
        if (isLargeScreen()) {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }

        mRecipeListRV.setLayoutManager(layoutManager);
        mRecipeListRV.setAdapter(mAdapter);
    }

    private void showEmptyView() {
        mRecipeListRV.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showRecipeList() {
        mRecipeListRV.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeClick(int recipeId) {
        Intent intent = new Intent(this, RecipeInstructionsActivity.class);
        intent.putExtra(Constants.RECIPE_ID_EXTRA, recipeId);
        startActivity(intent);
    }
}
