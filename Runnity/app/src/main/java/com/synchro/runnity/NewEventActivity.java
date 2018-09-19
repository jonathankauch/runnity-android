package com.synchro.runnity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewEventActivity extends AppCompatActivity {

    @BindView(R.id.titleEditView) EditText titleEditView;
    @BindView(R.id.descriptionEditView) EditText descriptionEditView;
    @BindView(R.id.cityEditView) EditText cityEditView;
    @BindView(R.id.distanceEditView) EditText distanceEditView;
    @BindView(R.id.private_switch) Switch private_switch;
    @BindView(R.id.sendButton) Button sendButton;

    String eventId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("event_id") != null) {
            eventId = getIntent().getStringExtra("event_id");
            titleEditView.setText(getIntent().getStringExtra("name"));
            descriptionEditView.setText(getIntent().getStringExtra("description"));
            cityEditView.setText(getIntent().getStringExtra("city"));
            distanceEditView.setText(getIntent().getFloatExtra("distance", 0) + "");
            if (getIntent().getBooleanExtra("private", false)) {
                private_switch.setChecked(true);
            }
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = titleEditView.getText().toString();
                String description = descriptionEditView.getText().toString();
                String city = cityEditView.getText().toString();
                String distance = distanceEditView.getText().toString();
                boolean privateValue = private_switch.isChecked();

                if (name.length() == 0 || description.length() == 0 || city.length() == 0 || distance.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs.", Toast.LENGTH_LONG).show();
                } else {
                    final JsonObject json = new JsonObject();
                    json.addProperty("user_token", Singleton.getInstance().getToken());
                    json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                    json.addProperty("name", name);
                    json.addProperty("description", description);
                    json.addProperty("city", city);
                    json.addProperty("distance", distance);
                    json.addProperty("private_status", privateValue);

                    BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
                    BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());

                    if (eventId != null) {
                        Ion.with(getApplicationContext())
                                .load("PUT", getString(R.string.api_url) + "events/" + eventId)
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
                            }
                        });
                    } else {
                        Ion.with(getApplicationContext())
                                .load(getString(R.string.api_url) + "events")
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
                            }
                        });
                    }

                    onBackPressed();
                }
            }
        });
    }
}
