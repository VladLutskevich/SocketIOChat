package com.example.chat2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        edittext = (EditText) findViewById(R.id.edittext);

    }

    public void login(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("login", edittext.getText().toString());
        startActivity(intent);

    }

}

