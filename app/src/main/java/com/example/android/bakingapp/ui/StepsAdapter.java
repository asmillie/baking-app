package com.example.android.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private final Context mContext;
    private List<Step> mStepList;
    private final OnStepClickListener mClickListener;

    public interface OnStepClickListener {
        void onStepSelected(Integer stepId);
    }

    public StepsAdapter(Context context, OnStepClickListener clickListener) {
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    public void setStepList(List<Step> stepList) {
        this.mStepList = stepList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepViewHolder holder, int position) {
        holder.mShortDescTV.setText(mStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mStepList != null ? mStepList.size() : 0;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.short_description_tv)
        TextView mShortDescTV;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mShortDescTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mStepList.get(getAdapterPosition()).getId();
            mClickListener.onStepSelected(elementId);
        }
    }
}
