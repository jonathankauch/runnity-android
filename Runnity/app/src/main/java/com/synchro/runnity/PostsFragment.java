package com.synchro.runnity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Events;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Singleton;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shirco on 29/02/2016.
 */
public class PostsFragment extends Fragment {
    private Posts posts;
    private Post editPost;

    List<String> paths;

    @BindView(R.id.postsListView)
    ListView listView;

    @BindView(R.id.addPictureButton)
    Button pictureButton;

    @BindView(R.id.commentButton)
    Button commentButton;

    @BindView(R.id.editButton)
    Button editButton;

    @BindView(R.id.groupsButton)
    Button groupsButton;

    @BindView(R.id.postsButton)
    Button postsButton;

    @BindView(R.id.commentsListView)
    ListView commentsListView;

    @BindView(R.id.newPostButton)
    FloatingActionButton fab;

    @BindView(R.id.postsListLayout)
    LinearLayout postsListLayout;

    @BindView(R.id.groupsListLayout)
    LinearLayout groupsListLayout;

    @BindView(R.id.newPostLayout)
    RelativeLayout newPostLayout;

    @BindView(R.id.commentsLayout)
    RelativeLayout commentsLayout;

    @BindView(R.id.confirmPostButton)
    Button confirmPostButton;

    @BindView(R.id.contentNewPost)
    EditText contentNewPost;

    @BindView(R.id.newCommentLayout)
    RelativeLayout newCommentLayout;

    @BindView(R.id.confirmComment)
    Button confirmComment;

    @BindView(R.id.contentNewComment)
    EditText contentComment;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);
        ButterKnife.bind(this, rootView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost = null;
                if (newPostLayout.getVisibility() == View.GONE) {
                    newPostLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupsListLayout.getVisibility() == View.GONE) {
                    groupsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    groupsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    postsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    postsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    postsListLayout.setVisibility(View.GONE);
                    groupsListLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        postsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postsListLayout.getVisibility() == View.GONE) {
                    postsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    postsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    groupsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    groupsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    groupsListLayout.setVisibility(View.GONE);
                    postsListLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newCommentLayout.getVisibility() == View.GONE) {
                    commentsLayout.setVisibility(View.GONE);
                    newCommentLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCommentLayout.setVisibility(View.GONE);
                if (contentComment.getText() != null && contentComment.getText().toString().length() > 0) {
                    final JsonObject json = new JsonObject();
                    json.addProperty("user_token", Singleton.getInstance().getToken());
                    json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                    json.addProperty("content", contentComment.getText().toString());
                    json.addProperty("post_id", editPost.getId());
                    json.addProperty("user_id", Singleton.getInstance().getmUser().getId());

                    String url = getString(R.string.api_url) + "comments";
//                        if (!isNetworkAvailable())
//                            return;


                    Ion.with(getContext())
                            .load(url)
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e != null || result == null) {

                                        Log.d("API RESPONSE", e.toString());
                                        return;
                                    }
                                    Log.d("API RESPONSE", result.toString());
                                    getPosts();

                                }
                            });

                    contentComment.setText("");
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPost != null) {
                    commentsLayout.setVisibility(View.GONE);
                    contentNewPost.setText(editPost.getContent());

                    if (newPostLayout.getVisibility() == View.GONE) {
                        newPostLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector.create(getActivity().getApplicationContext())
                        .showCamera(true) // show camera or not. true by default
                        .count(1) // max select image size, 9 by default. used width #.multi()
                        .start(PostsFragment.this, 0);
           }
        });

        newPostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPostLayout.getVisibility() == View.VISIBLE) {
                    newPostLayout.setVisibility(View.GONE);
                }
            }
        });

        newCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newCommentLayout.getVisibility() == View.VISIBLE) {
                    newCommentLayout.setVisibility(View.GONE);
                }
            }
        });


        commentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentsLayout.getVisibility() == View.VISIBLE) {
                    commentsLayout.setVisibility(View.GONE);
                }
            }
        });

        confirmPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPostLayout.setVisibility(View.GONE);
                if (contentNewPost.getText() != null && contentNewPost.getText().toString().length() > 0) {
                    final JsonObject json = new JsonObject();
                    json.addProperty("user_token", Singleton.getInstance().getToken());
                    json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                    json.addProperty("content", contentNewPost.getText().toString());

                    String url = getString(R.string.api_url) + "posts";
                    String method = "POST";
                    if (editPost != null) {
                        url += "/" + editPost.getId();
                        method = "PUT";
                    }

                    if (paths != null) {

                        String path = paths.get(0);
                        if (path.contains("file:/")) {
                            path = path.replaceFirst("file:/", "");
                        }

                        final String finalPath = path;

                        final File file = new File(finalPath);

                        if (!file.exists()) {
                            return;
                        }


                        Ion.with(getContext())
                                .load(method, url)
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        Log.d("UPDATE POST", "NEXT PICTURE");
                                        if (e != null || result == null) {

                                            Log.d("API RESPONSE", e.toString());
                                            return;
                                        }

                                        JsonElement id = result.get("id");
                                        if (id == null)
                                            return;



                                        Log.d("SENDING PICTURE", finalPath);
                                        String url = getString(R.string.api_url) + "posts/" + id.getAsInt() + "/addpicture";
                                        Log.d("PICTURE URL:", url);
                                        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
                                        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
                                        Ion.with(getContext())
                                                .load(url)
                                                .setHeader(tokenPair, emailPair)
                                                .setMultipartFile("picture", "image/jpg", file)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {
                                                        if (e != null || result == null) {

                                                            Log.d("API RESPONSE PICTURE", e.toString());
                                                            return;
                                                        }
                                                        Log.d("API RESPONSE PICTURE", result.toString());
                                                        getPosts();

                                                    }
                                                });
                                        Log.d("API RESPONSE", result.toString());
                                        getPosts();
                                    }
                                });

                    } else {
                        Log.d("NO PICTURE", "");

                        if (editPost != null) {
                            Ion.with(getContext())
                                    .load("PUT", url)
                                    .setJsonObjectBody(json)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if (e != null || result == null) {

                                                Log.d("API RESPONSE", e.toString());
                                                return;
                                            }
                                            Log.d("API RESPONSE", result.toString());
                                            Post post = new Gson().fromJson(result.toString(), Post.class);

                                            getPosts();
                                        }
                                    });
                        } else {
                            Ion.with(getContext())
                                    .load(url)
                                    .setJsonObjectBody(json)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if (e != null || result == null) {

                                                Log.d("API RESPONSE", e.toString());
                                                return;
                                            }
                                            Log.d("API RESPONSE", result.toString());
                                            getPosts();
                                        }
                                    });
                        }
                    }

                    paths = null;
                    contentNewPost.setText("");


                }
            }
        });

        listView.setAdapter(new PostsListAdapter(getContext()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                editPost = posts.getPosts()[position];

                if (editPost.getUser_id().equals(Singleton.getInstance().getId())) {
                    editButton.setVisibility(View.VISIBLE);
                } else {
                    editButton.setVisibility(View.GONE);
                }
                commentsLayout.setVisibility(View.VISIBLE);

                if (posts.getPosts()[position].getComments() != null && posts.getPosts()[position].getComments().length > 0) {
                    commentsListView.setAdapter(new CommentsListAdapter(getContext(), posts.getPosts()[position].getComments()));
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editPost = posts.getPosts()[position];

                if (editPost.getUser_id().equals(Singleton.getInstance().getId()))
                    AskOption().show();
                else
                    AskOptionOtherPost().show();
                return false;
            }
        });

//        try {
            getPosts();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return rootView;
    }

    private AlertDialog AskOptionOtherPost()
    {
        String like = "Aimer";

        if (editPost.is_liked()) {
            like = "Ne plus aimer";
        }
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Publication :")

                .setPositiveButton("Commenter", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (newCommentLayout.getVisibility() == View.GONE) {
                            commentsLayout.setVisibility(View.GONE);
                            newCommentLayout.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }

                })

                .setNeutralButton(like, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (editPost.is_liked()) {
                            likePost("dislike");
                        } else
                            likePost("like");
                        dialog.dismiss();

                    }
                })

                .create();
        return myQuittingDialogBox;
    }

    private AlertDialog AskOption()
    {
        String like = "Aimer";
        String likeUrl = "like";

        if (editPost.is_liked()) {
            like = "Ne plus aimer";
            likeUrl = "dislike";
        }

        final String finalLikeUrl = likeUrl;
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Publication :")

                .setPositiveButton("Editer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        if (editPost != null) {
                            commentsLayout.setVisibility(View.GONE);
                            contentNewPost.setText(editPost.getContent());

                            if (newPostLayout.getVisibility() == View.GONE) {
                                newPostLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        dialog.dismiss();
                    }

                })

                .setNeutralButton(like, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        likePost(finalLikeUrl);
                        dialog.dismiss();

                    }
                })

                .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost();
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    public void likePost(String like) {
        String url = getString(R.string.api_url) + "posts";
        if (editPost != null) {
            url += "/" + editPost.getId() + "/" + like;
        } else
            return;

        final JsonObject json = new JsonObject();
        json.addProperty("user_token", Singleton.getInstance().getToken());
        json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());

        Ion.with(getContext())
                .load("POST", url)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null || result == null) {

                            Log.d("API RESPONSE", e.toString());
                            return;
                        }
                        Log.d("API RESPONSE", result.toString());

                        getPosts();
                    }
                });
    }

    public void deletePost() {
        String url = getString(R.string.api_url) + "posts";
        if (editPost != null) {
            url += "/" + editPost.getId();
        } else
            return;

        final JsonObject json = new JsonObject();
        json.addProperty("user_token", Singleton.getInstance().getToken());
        json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());

        Ion.with(getContext())
                .load("DELETE", url)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null || result == null) {

                            Log.d("API RESPONSE", e.toString());
                            return;
                        }
                        Log.d("API RESPONSE", result.toString());
                        Post post = new Gson().fromJson(result.toString(), Post.class);

                        getPosts();
                    }
                });
    }

    public void getPosts() {
        String url = getString(R.string.api_url) + "home";

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Ion.with(getContext())
                .load(url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("POSTS RESPONSE", result.toString());
                        try {
                            posts = new Gson().fromJson(result.toString(), Posts.class);

                            if (posts.getPosts() == null) {
                                return;
                            }
                            Singleton.getInstance().setPosts(posts);

                            Log.d("POSTS SIZE", String.valueOf(posts.getPosts().length));

                            ((PostsListAdapter)listView.getAdapter()).notifyDataSetChanged();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActRes res", resultCode+ "");
        Log.d("OnActRes rq", requestCode + "");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
//                findViewById(R.id.extendedView).setVisibility(View.INVISIBLE);
//                changeCloseButtonText();
                // Get the result list of select image paths
                paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);


//                if (selectedDisplay == null) {
//                    createNewDisplay(paths);
//                } else {
//                    addPicturesToDisplay(paths);
//                }
//
//                checkAddButton();


                Log.d("PATHS", String.valueOf(paths));
            }
        }
    }



}
