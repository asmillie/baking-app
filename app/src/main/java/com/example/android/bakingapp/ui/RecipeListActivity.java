package com.example.android.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeAndInstructions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

    private RecipeListViewModel mViewModel;
    private List<RecipeAndInstructions> mRecipeList;

    private RecipeListAdapter mAdapter;

    @BindView(R.id.recipe_list_rv)
    RecyclerView mRecipeListRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        initViewModel();
        initUI();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        mViewModel.getRecipeList().observe(this, new Observer<List<RecipeAndInstructions>>() {
            @Override
            public void onChanged(@Nullable List<RecipeAndInstructions> recipeAndInstructions) {
                mRecipeList = recipeAndInstructions;
                mAdapter.setRecipeList(mRecipeList);
            }
        });
    }

    private void initUI() {
        mAdapter = new RecipeListAdapter(mRecipeList, this);

        mRecipeListRV.setHasFixedSize(true);
        mRecipeListRV.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mRecipeListRV.setAdapter(mAdapter);
    }

    private void clearUI() {

    }

    private void populateUI() {

    }

    @Override
    public void onRecipeClick(int recipeId) {
        //TODO intent to open detail view
    }
}
