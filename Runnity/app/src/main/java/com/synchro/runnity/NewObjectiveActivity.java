package com.synchro.runnity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Singleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewObjectiveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.objectiveTypeSpinner) Spinner objectiveTypeSpinner;
    @BindView(R.id.valueEditView) EditText valueEditView;
    @BindView(R.id.objectiveEditView) EditText objectiveEditView;
    @BindView(R.id.endDateEditView) EditText endDate;
    @BindView(R.id.sendObjective) Button sendButton;

    String type = "KILOMETER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_objective);
        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.objective_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        objectiveTypeSpinner.setAdapter(adapter);
        objectiveTypeSpinner.setOnItemSelectedListener(this);
        objectiveTypeSpinner.setSelection(0);

        endDate.setText("22-02-2018");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date myDate;
                Date myDate2;
                try {
                    myDate = df.parse(endDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner une date valide.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (myDate.compareTo(new Date()) < 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner une date valide.", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d("Type", type);
                if (type.length() < 1 || valueEditView.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    final JsonObject achievement = new JsonObject();
                    Log.d("date", myDate.toString());

                    achievement.addProperty("user_id", Singleton.getInstance().getId());
                    achievement.addProperty("achievement_type", type);
                    achievement.addProperty("value", valueEditView.getText().toString());
                    achievement.addProperty("content", objectiveEditView.getText().toString());
                    achievement.addProperty("start_date", new Date().toString());
                    achievement.addProperty("due_date", myDate.toString());

                    final JsonObject json = new JsonObject();
                    json.addProperty("user_token", Singleton.getInstance().getToken());
                    json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                    json.add("achievement", achievement);

                    BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
                    BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());


                    Ion.with(getApplicationContext())
                            .load(getString(R.string.api_url) + "achievements")
                            .setHeader(tokenPair, emailPair)
                            .setJsonObjectBody(json)
                            .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if (e != null || result == null) {
                                Log.d("API RESPONSE", e.toString());
                                return;
                            }
                            Log.d("API RESPONSE", result.toString());

                            onBackPressed();
                        }
                    });
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("POSITION", "pos : " + pos);
        if (pos == 0)
            type = "KILOMETER";
        if (pos == 1)
            type = "TIME";
        if (pos == 2)
            type = "WEIGHT";
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
