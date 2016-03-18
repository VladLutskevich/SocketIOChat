package com.example.chat2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SecondActivity extends AppCompatActivity {
    Socket socket;
    EditText edittext2;
    static LinearLayout ll;
    static LinearLayout.LayoutParams params;
    static Context context;
    String login;
    ScrollView scrollView;
    private static final String SERVER = "http://46.101.96.234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        try {
            socket = IO.socket(SERVER);
        } catch (URISyntaxException e) {
        }
        edittext2 = (EditText) findViewById(R.id.edittext2);
        ll = (LinearLayout) findViewById(R.id.ll);
        context = this;
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        login = getIntent().getStringExtra("login");
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        createTextView("Welcome to Socket.IO Chat");
        socket.connect();
        socket.emit("add user", login.trim());
        socket.on("login", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                SecondActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        int sum;
                        try {
                            sum = data.getInt("numUsers");
                        } catch (JSONException e) {
                            return;
                        }

                        createTextView("There are " + sum + " participant(s)");
                    }
                });
            }
        });
        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                SecondActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String log;
                        String mes;
                        try {
                            log = data.getString("username");
                            mes = data.getString("message");
                        } catch (JSONException e) {
                            return;
                        }


                        mes(mes, log);
                        ToBottom();
                    }
                });
            }
        });
        socket.on("user joined", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                SecondActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String user;
                        int sum;
                        try {
                            user = data.getString("username");
                            sum = data.getInt("numUsers");
                        } catch (JSONException e) {
                            return;
                        }

                        createTextView(user + " joined");
                        createTextView("There are " + sum + " participant(s)");
                        ToBottom();
                    }
                });
            }
        });

        socket.on("user left", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                SecondActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String user;
                        int sum;
                        try {
                            user = data.getString("username");
                            sum = data.getInt("numUsers");
                        } catch (JSONException e) {
                            return;
                        }
                        createTextView(user + " left");
                        createTextView("There are " + sum + " participant(s)");
                        ToBottom();
                    }
                });
            }
        });
        socket.on("typing", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                SecondActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String user;
                        try {
                            user = data.getString("username");
                        } catch (JSONException e) {
                            return;
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                user + " is typing", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void createTextView(String string) {

        TextView textView = new TextView(context);
         textView.setText(string);
        ll.addView(textView, params);
    }


    public void mes(String mes, String login) {
        TextView textView = new TextView(context);
        TextView textView2 = new TextView(context);
        textView.setText(mes);
        textView2.setText(login + ":");
        ll.addView(textView2, params);
        ll.addView(textView, params);


    }

    public void send(View view) {
        String string = edittext2.getText().toString().trim();

        edittext2.setText("");
        socket.emit("new message", string);
        mes(string, login);

    }

    public void ToBottom() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

    }


}
