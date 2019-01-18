package com.example.android.kidtrackerparent.Parent.Rules;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.BasicClasses.Rule;
import com.example.android.kidtrackerparent.Parent.Kids.KidsListFragment;
import com.example.android.kidtrackerparent.Parent.Kids.KidsRecyclerViewAdapter;
import com.example.android.kidtrackerparent.R;

import java.util.List;

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.ViewHolder>  {

    public static final String TAG = RulesRecyclerViewAdapter.class.getSimpleName();
    private final List<Rule> mRules;

    public RulesRecyclerViewAdapter(List<Rule> rules) {
        mRules = rules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_rules_list_item, viewGroup, false);
        return new RulesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mRule = mRules.get(i);
        viewHolder.mRuleStartTimeTextView.setText(viewHolder.mRule.getFullStartTime());
        viewHolder.mRuleEndTimeTextView.setText(viewHolder.mRule.getFullEndTime());
        viewHolder.mRuleRepetitionTextView.setText(
                viewHolder.context.getString(R.string.rules_repetition_none)
        );
        viewHolder.mAreaNameTextView.setText(viewHolder.mRule.getAreaName());
        viewHolder.mRuleActiveSwitch.setChecked(viewHolder.mRule.isActive());
        viewHolder.mAreaIconImageView.setImageDrawable(
                getDrawableIcon(viewHolder.mRule.getAreaIcon(), viewHolder.context)
        );
        //TODO: add image
    }

    @Override
    public int getItemCount() {
        return mRules.size();
    }

    private Drawable getDrawableIcon(String iconId, Context context) {
        switch (iconId) {
            case Area.ICON_BOOK:
                return context.getResources().getDrawable(R.drawable.book);
            case Area.ICON_BUILDING:
                return context.getResources().getDrawable(R.drawable.building);
            case Area.ICON_WORK:
                return context.getResources().getDrawable(R.drawable.work);
            default:
                return context.getResources().getDrawable(R.drawable.home);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mRuleStartTimeTextView;
        final TextView mRuleEndTimeTextView;
        final TextView mRuleRepetitionTextView;
        final TextView mAreaNameTextView;
        final Switch mRuleActiveSwitch;
        final ImageView mAreaIconImageView;
        Context context;
        Rule mRule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            mRuleStartTimeTextView = itemView.findViewById(R.id.tv_rule_start_date);
            mRuleEndTimeTextView = itemView.findViewById(R.id.tv_rule_end_date);
            mRuleRepetitionTextView = itemView.findViewById(R.id.tv_rule_repetition);
            mAreaNameTextView = itemView.findViewById(R.id.tv_rule_area_name);
            mRuleActiveSwitch = itemView.findViewById(R.id.switch_rule_active);
            mAreaIconImageView = itemView.findViewById(R.id.iv_rule_area_icon);


        }

    }
}
