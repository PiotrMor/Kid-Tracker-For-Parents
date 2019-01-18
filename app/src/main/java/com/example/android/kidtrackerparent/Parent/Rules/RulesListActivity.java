package com.example.android.kidtrackerparent.Parent.Rules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.AsyncTasks.AsyncResponse;
import com.example.android.kidtrackerparent.AsyncTasks.RuleListFromServerAsync;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.BasicClasses.Rule;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;

import java.util.ArrayList;

public class RulesListActivity extends AppCompatActivity implements AsyncResponse<ArrayList<Rule>> {

    private Kid mKid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_list);

        getKidReferenceFromIntent();

        RuleListFromServerAsync ruleListFromServerAsync = new RuleListFromServerAsync(mKid.getId(), this);
        ruleListFromServerAsync.execute(this);
    }

    private void getKidReferenceFromIntent() {
        Intent intent = getIntent();

        if(intent.hasExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID)) {
            mKid = (Kid) intent.getSerializableExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID);
        }
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
    public void onSuccess(ArrayList<Rule> item) {

    }

    @Override
    public void onFailure() {

    }
}
