package com.synchro.runnity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.mailEditView) EditText mailEditText;
    @BindView(R.id.passwordEditView) EditText passwordEditText;
    @BindView(R.id.firstnameEditView) EditText firstnameEditText;
    @BindView(R.id.lastnameEditView) EditText lastnameEditText;
    @BindView(R.id.phoneEditView) EditText phoneEditText;
    @BindView(R.id.registerButton) Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String lastname = lastnameEditText.getText().toString();
                String firstname = firstnameEditText.getText().toString();

                if (mail.length() == 0 || password.length() == 0 || lastname.length() == 0 || firstname.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs.", Toast.LENGTH_LONG).show();
                } else {
                    final JsonObject json = new JsonObject();
                    json.addProperty("firstname", firstname);
                    json.addProperty("lastname", lastname);
                    json.addProperty("password", password);
                    json.addProperty("email", mail);

                    registerButton.setEnabled(false);
                    Ion.with(getApplicationContext())
                            .load(getString(R.string.api_url) + "sign_up")
                            .setJsonObjectBody(json)
                            .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if (e != null || result == null) {
                                Log.d("API RESPONSE", e.toString());
                                return;
                            }

                            if (result.get("user") != null) {
                                Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                registerButton.setEnabled(true);
                                Toast.makeText(getApplicationContext(), "Email invalide", Toast.LENGTH_LONG).show();
                            }
                            Log.d("API RESPONSE", result.toString());
                        }
                    });

                }
            }
        });
    }
}
