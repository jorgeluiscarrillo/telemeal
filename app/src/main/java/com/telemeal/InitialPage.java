package com.telemeal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class InitialPage extends AppCompatActivity {
    /**
     * Initializes InitialPage
     * @param savedInstanceState save the state of the activity in a bundle for other when activity is accessed again later
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        Button startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Moves into MenuActivity
             * @param view the view the click was held int
             */
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
            /**
             * Goes into the LoginActivity
             * @param view the view the click was held int
             */
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(InitialPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
