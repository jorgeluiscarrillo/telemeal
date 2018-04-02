package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        Button guestButton = (Button) findViewById(R.id.button_guest);
        guestButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(InitialPage.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        Button otherButton = (Button) findViewById(R.id.button_other);
        otherButton.setOnClickListener(new View.OnClickListener()
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
