package com.example.android.kidtrackerparent.Parent.Rules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.kidtrackerparent.AsyncTasks.AreaListFromServerAsync;
import com.example.android.kidtrackerparent.AsyncTasks.AsyncResponse;
import com.example.android.kidtrackerparent.AsyncTasks.RuleListFromServerAsync;
import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.BasicClasses.Rule;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;

import java.util.ArrayList;
import java.util.List;

public class RulesListActivity extends AppCompatActivity implements AsyncResponse{

    public static final String TAG = RulesListActivity.class.getSimpleName();
    private Kid mKid;
    private List<Rule> mRuleList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private FloatingActionButton mFloatingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_list);

        getKidReferenceFromIntent();
        setupRecyclerView();

        getRuleList();
        addRefreshOnSwap();
        addFabOnClick();

    }

    private void addFabOnClick() {
        mFloatingButton = findViewById(R.id.fab_add_rule);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulesListActivity.this, AddRuleActivity.class);
                intent.putExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID, mKid);
                startActivity(intent);
            }
        });
    }

    private void getRuleList() {
        RuleListFromServerAsync ruleListFromServerAsync = new RuleListFromServerAsync(mKid.getId(), this);
        ruleListFromServerAsync.execute(this);
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_rules);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RulesRecyclerViewAdapter(mRuleList));

    }

    private void getKidReferenceFromIntent() {
        Intent intent = getIntent();

        if(intent.hasExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID)) {
            mKid = (Kid) intent.getSerializableExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID);
        }
    }

    private void addRefreshOnSwap() {
        mSwipeRefresh = findViewById(R.id.srl_rules_list);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRuleList();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onSuccess(Object item) {

        ArrayList list = (ArrayList) item;
        if (list.size() > 0) {
            if (list.get(0) instanceof Rule) {
                mRuleList = list;
                AreaListFromServerAsync areaListFromServerAsync = new AreaListFromServerAsync(this);
                areaListFromServerAsync.execute(this);
            } else if (list.get(0) instanceof Area) {
                addAreaInfoToRules(list);
                mRecyclerView.setAdapter(new RulesRecyclerViewAdapter(mRuleList));
                if (mSwipeRefresh != null) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        }
    }

    private void addAreaInfoToRules(ArrayList areas) {
        for (Rule rule : mRuleList) {
            Area area = getAreaById(rule.getAreaId(), areas);
            if (area != null) {
                rule.setAreaName(area.getName());
                rule.setAreaIcon(area.getIconId());
            } else {
                rule.setAreaName("UNKNOWN");
                rule.setAreaIcon("home");
            }
        }
    }

    private Area getAreaById(String areaId, ArrayList areas) {
        for (Object object : areas) {
            Area area = (Area) object;
            if (areaId.equals(area.getId())) {
                Log.d(TAG, "getAreaById: okk");
                return area;
            }
        }
        return null;
    }

    @Override
    public void onFailure() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Rule rule);
    }
}
