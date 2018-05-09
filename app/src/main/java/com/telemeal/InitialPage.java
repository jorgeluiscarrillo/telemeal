package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;

public class InitialPage extends AppCompatActivity {

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);
        storage = FirebaseStorage.getInstance();


        Button startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(InitialPage.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = (Button) findViewById(R.id.btn_employee);
        loginButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(InitialPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
