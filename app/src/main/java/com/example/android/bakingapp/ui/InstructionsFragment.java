package com.example.android.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InstructionsFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    private RecipeInstructionsViewModel mViewModel;

    OnStepSelectedListener mStepSelectedListener;

    private Integer mRecipeId;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    private Context mContext;
    private ArrayAdapter<String> mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    @BindView(R.id.ingredient_lv)
    ListView mIngredientsListView;

    @BindView(R.id.steps_rv)
    RecyclerView mStepsRecyclerView;

    private Unbinder unbinder;

    public interface OnStepSelectedListener {
        void onStepSelected(Integer stepId);
    }

    public InstructionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InstructionsFragment.
     */
    public static InstructionsFragment newInstance() {
        return new InstructionsFragment();
    }

    public void setRecipeId(Integer recipeId) {
        this.mRecipeId = recipeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
        }

        if (mRecipeId != null) {
            initViewModel();
        } else {
            Log.e(TAG, "No recipe id set, cannot initialize viewmodel factory");
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.RECIPE_ID_EXTRA, mRecipeId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Creating fragment view");
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = view.getContext();

        mStepsAdapter = new StepsAdapter(mContext, this);

        //mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        );
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mStepSelectedListener = (OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                + " must implement OnStepSelectedListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStepSelected(Integer stepId) {
        mStepSelectedListener.onStepSelected(stepId);
    }

    private void initViewModel() {
        RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(getActivity().getApplication(), mRecipeId);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(RecipeInstructionsViewModel.class);

        mViewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mIngredients = ingredients;
                populateIngredientList();
                Log.d(TAG, "Observed change to ingredients (" + ingredients.size() + " items), list updated. Notified adapter");
            }
        });

        mViewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                mSteps = steps;
                mStepsAdapter.setStepList(steps);
                Log.d(TAG, "Observed change to steps, list updated");
            }
        });
    }

    private void populateIngredientList() {
        List<String> ingredientList = new ArrayList<>();
        if (mIngredients != null && mIngredients.size() > 0) {
            for (Ingredient ingredient: mIngredients) {
                ingredientList.add(ingredient.toString());
            }
            Log.d(TAG, "Ingredients list built and ready for adapter");
        }

        mIngredientsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, ingredientList);
        mIngredientsListView.setAdapter(mIngredientsAdapter);
    }


}
