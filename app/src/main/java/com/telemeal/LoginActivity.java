package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText et_eid;
    private EditText et_name;
    private Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializer();
    }

    private void initializer(){
        et_eid = (EditText) findViewById(R.id.login_et_eid);
        et_name = (EditText) findViewById(R.id.login_et_ename);
        btn_enter = (Button) findViewById(R.id.login_btn_enter);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mngrIntent = new Intent(LoginActivity.this, ManagerOptionActivity.class);
                startActivity(mngrIntent);
            }
        });
    }

}
