/**
 * Created by Shirco on 26/04/2016.
 */
package com.synchro.runnity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Runs;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shirco on 29/02/2016.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.mailEditView) EditText mailEditText;
    @BindView(R.id.firstnameEditView) EditText firstnameEditText;
    @BindView(R.id.lastnameEditView) EditText lastnameEditText;
    @BindView(R.id.phoneEditView) EditText phoneEditText;
    @BindView(R.id.weightEditView) EditText weightEditText;
    @BindView(R.id.addressEditView) EditText addressEditText;
    @BindView(R.id.registerButton) Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        mailEditText.setText(Singleton.getInstance().getmUser().getEmail());
        lastnameEditText.setText(Singleton.getInstance().getmUser().getLastname());
        firstnameEditText.setText(Singleton.getInstance().getmUser().getFirstname());
        phoneEditText.setText(Singleton.getInstance().getmUser().getPhone());
        weightEditText.setText(Singleton.getInstance().getmUser().getWeight());
        addressEditText.setText(Singleton.getInstance().getmUser().getAddress());
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mailEditText.getText().toString();
                String lastname = lastnameEditText.getText().toString();
                String firstname = firstnameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String weight = weightEditText.getText().toString();
                String address = addressEditText.getText().toString();

                final JsonObject json = new JsonObject();
                json.addProperty("firstname", firstname);
                json.addProperty("lastname", lastname);
                json.addProperty("address", address);
                json.addProperty("phone", phone);
                json.addProperty("weight", weight);
                json.addProperty("email", mail);
                json.addProperty("user_token", Singleton.getInstance().getToken());
                json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                Ion.with(getActivity())
                        .load("PUT", getString(R.string.api_url) + "users/" + Singleton.getInstance().getId())
                        .setJsonObjectBody(json)
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null || result == null) {
                            Log.d("API RESPONSE", e.toString());
                            return;
                        }

                        User user = new Gson().fromJson(json, User.class);
                        if (user != null) {
                            Singleton.getInstance().setmUser(user);
                        }

                        Log.d("API RESPONSE", result.toString());
                    }
                });
                Toast.makeText(getContext(), "Vos informations ont bien été modifiées", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
