package com.example.android.bakingapp.ui;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.example.android.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
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
import butterknife.Unbinder;
/*
Solution to player position on rotation found
@ https://stackoverflow.com/questions/45481775/exoplayer-restore-state-when-resumed?noredirect=1&lq=1
 */
public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private Integer mRecipeId;
    private Integer mStepId;
    private Step mStep;

    private RecipeInstructionsViewModel mViewModel;
    private SimpleExoPlayer mVideoPlayer;
    private long mPlayerPosition;

    @BindView(R.id.step_description) TextView mStepDesc;
    @BindView(R.id.recipe_step_video) PlayerView mVideoPlayerView;
    @BindView(R.id.empty_video_view) TextView mEmptyVideoView;

    private Unbinder unbinder;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    public static RecipeStepFragment newInstance(Integer recipeId) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.RECIPE_ID_EXTRA, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setStepId(Integer stepId) {
        if (mViewModel == null) {
            mStepId = stepId;
            initViewModel();
        } else {
            mViewModel.setStepId(stepId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(Constants.RECIPE_ID_EXTRA);
            initViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
            mPlayerPosition = savedInstanceState.getLong(Constants.PLAYER_POSITION_EXTRA);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoPlayer != null) {
            mPlayerPosition = mVideoPlayer.getCurrentPosition();
            releaseVideoPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        releaseVideoPlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.PLAYER_POSITION_EXTRA, mPlayerPosition);
    }

    private void releaseVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer.stop();
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    private void initViewModel() {
        if (mRecipeId != null) {
            Application application = new Application();
            try {
                application = getActivity().getApplication();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error getting activity application: " + e.toString());
            }

            if (application != null) {
                RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(application, mRecipeId);

                mViewModel = ViewModelProviders.of(getActivity(), factory).get(RecipeInstructionsViewModel.class);

                mViewModel.getStep().observe(this, new Observer<Step>() {
                    @Override
                    public void onChanged(@Nullable Step step) {
                        Log.d(TAG, "observed changed to step");
                        if (step != null) {
                            int currentStepId = mStep.getId();
                            int newStepId = step.getId();
                            if (currentStepId != newStepId) {
                                Log.d(TAG, "Observed step replacing old step");
                                mStep = step;
                                populateUI();
                            }
                        }
                    }
                });
            }
        }
    }

    private void populateUI() {
        Log.d(TAG, "Populating UI");
        if (mStep != null) {
            String stepDesc = mStep.getDescription();
            String stepShortDesc = mStep.getShortDescription();
            String videoUrl = mStep.getVideoURL();

            if (stepDesc == null || stepDesc.equals("")) {
                mStepDesc.setText("No description found");
            } else {
                mStepDesc.setText(stepDesc);
            }

            boolean isConnected = false;
            try {
                if (NetworkUtils.isConnected(getActivity().getApplicationContext())) {
                    isConnected = true;
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "Error checking network connectivity: " + e.toString());
            }

            if (isConnected && videoUrl != null && !videoUrl.equals("")) {
                mEmptyVideoView.setVisibility(View.GONE);
                mVideoPlayerView.setVisibility(View.VISIBLE);

                initVideoPlayer();
                loadMedia(getStepVideoUri(videoUrl));

            } else {
                mVideoPlayerView.setVisibility(View.GONE);
                mEmptyVideoView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Simple Exo Player implemented following
     * Udacity Advanced Android Classical Music Quiz
     * and
     * ExoPlayer Documentation @ https://google.github.io/ExoPlayer/guide.html
     */
    private void initVideoPlayer() {
        Log.d(TAG, "Initializing Video PLayer");
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), renderersFactory, trackSelector, loadControl);
        mVideoPlayerView.setPlayer(mVideoPlayer);
    }

    private void loadMedia(Uri uri) {
        if (mVideoPlayer != null && uri != null) {
            Log.d(TAG, "Loading media into video player");
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingApp"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
            // Prepare the player with the source.
            mVideoPlayer.prepare(videoSource, false, false);

            if (mPlayerPosition >= 0) {
                mVideoPlayer.seekTo(mPlayerPosition);
            }
            mVideoPlayer.setPlayWhenReady(true);
        }
    }

    private Uri getStepVideoUri(String videoUrl) {
        Uri uri = null;
        if (videoUrl != null && !videoUrl.equals("")) {
            uri = Uri.parse(videoUrl);
        }
        return uri;
    }
}
