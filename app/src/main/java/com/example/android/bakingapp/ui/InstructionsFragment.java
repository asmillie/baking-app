package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstructionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructionsFragment extends Fragment {

    private static final String INGREDIENT_BUNDLE_ARG = "ingredients";
    private static final String STEP_BUNDLE_ARG = "steps";
    private static final String RECIPE_ID_BUNDLE_ARG = "recipe-id";

    private RecipeInstructionsViewModel mViewModel;

    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    private OnFragmentInteractionListener mListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: Rebuild lists into string arrays and then use simple arrayadapter to populate lists

        List<String> ingredientList = new ArrayList<>();
        if (mIngredients != null && mIngredients.size() > 0) {
            for (Ingredient ingredient: mIngredients) {
                ingredientList.add(ingredient.toString());
            }
        }

        List<String> stepsList = new ArrayList<>();
        if (mSteps != null && mSteps.size() > 0) {
            for (Step step: mSteps) {
                stepsList.add(step.toString());
            }
        }



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(getActivity()).get(RecipeInstructionsViewModel.class);

        mViewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mIngredients = ingredients;
            }
        });

        mViewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                mSteps = steps;
            }
        });
    }
}
