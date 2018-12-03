package com.example.android.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private Integer mRecipeId;
    private Step mStep;

    private RecipeInstructionsViewModel mViewModel;
    private SimpleExoPlayer mVideoPlayer;
    private static MediaSessionCompat mMediaSession;

    //private OnFragmentInteractionListener mListener;

    @BindView(R.id.step_description) TextView mStepDesc;
    @BindView(R.id.recipe_step_video) PlayerView mVideoPlayerView;



    public RecipeStepFragment() {
        // Required empty public constructor
    }

    public static RecipeStepFragment newInstance(Integer recipeId, Step step) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.RECIPE_ID_EXTRA, recipeId);
        args.putParcelable(Constants.RECIPE_STEP_BUNDLE_EXTRA, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(Constants.RECIPE_STEP_ID_EXTRA);
            mStep = getArguments().getParcelable(Constants.RECIPE_STEP_BUNDLE_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
            mStep = savedInstanceState.getParcelable(Constants.RECIPE_STEP_BUNDLE_EXTRA);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);

        String stepDesc = mStep.getDescription();
        String stepShortDesc = mStep.getShortDescription();

        if (stepDesc == null || stepDesc.equals("")) {
            mStepDesc.setText("No description found");
        } else {
            mStepDesc.setText(stepDesc);
        }

        initVideoPlayer();

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
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
*/
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
        }
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
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    } */

    private void initViewModel() {
        RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(getActivity().getApplication(), mRecipeId);

        mViewModel = ViewModelProviders.of(this, factory).get(RecipeInstructionsViewModel.class);
    }

    /**
     * Simple Exo Player implemented following
     * Udacity Advanced Android Classical Music Quiz
     * and
     * ExoPlayer Documentation @ https://google.github.io/ExoPlayer/guide.html
     */
    private void initVideoPlayer() {
        String mediaUrl = mStep.getVideoURL();
        if (mediaUrl == null || mediaUrl.equals("")) {
           return;
        }

        if (mVideoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

            mVideoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), renderersFactory, trackSelector, loadControl);
            mVideoPlayerView.setPlayer(mVideoPlayer);
            loadMedia(getStepVideoUri());
        }
    }

    private void loadMedia(Uri uri) {
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        // Prepare the player with the source.
        mVideoPlayer.prepare(videoSource);
    }

    private Uri getStepVideoUri() {
        String videoUrl = mStep.getVideoURL();

        Uri uri = null;
        if (videoUrl != null) {
            uri = Uri.parse(videoUrl);
        }
        return uri;
    }
}
