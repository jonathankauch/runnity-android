package com.synchro.runnity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Messages;
import com.synchro.runnity.Models.Singleton;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ConversationActivity extends AppCompatActivity {
    private Messages messages;

    @BindView(R.id.messagesListView)
    ListView listView;

    @BindView(R.id.sendButton)
    Button sendButton;

    @BindView(R.id.newMessageEditText)
    EditText newMessageEditTex;

    private static String conversationId;
    private static String friendId;
    private static String title;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        messages = new Messages();
        friendId = getIntent().getStringExtra("friend_id");
        title = getIntent().getStringExtra("title");
        setTitle(title);
        listView.setAdapter(new MessagesListAdapter(this, messages));
        getConversation();

        newMessageEditTex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (listView.getCount() > 0) {
                        listView.setSelection(listView.getCount() - 1);
                    }
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conversationId != null && newMessageEditTex.getText().length() > 0) {
                    String message = newMessageEditTex.getText().toString();
                    newMessageEditTex.setText("");
                    sendNewMessage(message);
                }
            }
        });
    }

    public void sendNewMessage(String message) {
        String url = getString(R.string.api_url) + "conversations/" + conversationId + "/messages";

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());

        JsonObject json = new JsonObject();

        json.addProperty("body", message);
        Log.d("CONVERSATION URL", url);
        Ion.with(this)
                .load("POST", url)
                .setHeader(tokenPair, emailPair)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("CONVERSATION RESPONSE", result.toString());
                        try {
                            getMessages();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }


    public void getConversation() {
        String url = getString(R.string.api_url) + "conversations?recipient_id=" + friendId;

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Log.d("CONVERSATION URL", url);
        Ion.with(this)
                .load(url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("CONVERSATION RESPONSE", result.toString());

                        try {
                            if (result.getAsJsonObject("conversations") != null) {
                                conversationId = result.getAsJsonObject("conversations").get("id").getAsString();
                                timer = new Timer();

                                timer.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        getMessages();
                                    }
                                }, 0, 5000);
                                getMessages();
                            } else {
                                postConversation();
                            }
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    public void postConversation() {
        String url = getString(R.string.api_url) + "conversations/";

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());

        JsonObject json = new JsonObject();

        json.addProperty("recipient_id", friendId);
        Log.d("CONVERSATION URL", url);
        Ion.with(this)
                .load("POST", url)
                .setHeader(tokenPair, emailPair)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("CONVERSATION RESPONSE", result.toString());

                        try {
                            if (result.getAsJsonObject("conversation") != null) {
                                conversationId = result.getAsJsonObject("conversation").get("id").getAsString();
                                timer = new Timer();

                                timer.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        getMessages();
                                    }
                                }, 0, 5000);
                                getMessages();
                            }
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    public void getMessages() {
        String url = getString(R.string.api_url) + "conversations/" + conversationId + "/messages";

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Log.d("CONVERSATION URL", url);
        Ion.with(this)
                .load(url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("CONVERSATION RESPONSE", result.toString());
                        try {
                            messages = new Gson().fromJson(result.toString(), Messages.class);

                            if (messages.getMessages() == null) {
                                return;
                            }
                            Log.d("CONVERSATION SIZE", String.valueOf(messages.getMessages().length));

                            ((MessagesListAdapter)listView.getAdapter()).notifyDataSetChanged(messages);
                            if (listView.getCount() > 0) {
                                listView.setSelection(listView.getCount() - 1);
                            }
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (timer != null)
            timer.cancel();

        super.onBackPressed();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
