package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText et_eid;
    private EditText et_name;
    private EditText et_pos;
    private CheckBox cb_priv;

    private EditText et_ueid;
    private EditText et_uname;
    private EditText et_upos;
    private CheckBox cb_upriv;

    private Button btn_add;
    private Button btn_view;
    private Button btn_edit;
    private Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);
        initializer();
/*
        Intent empIntent = new Intent(ManagerOptionActivity.this, EditEmployeeActivity.class);
        startActivity(empIntent);
*/

    }

    private void initializer(){
        et_eid = (EditText) findViewById(R.id.edemp_et_eid);
        et_name = (EditText) findViewById(R.id.edemp_et_name);
        et_pos = (EditText) findViewById(R.id.edemp_et_pos);
        cb_priv = (CheckBox) findViewById(R.id.edemp_cb_priv);

        et_ueid = (EditText) findViewById(R.id.edemp_et_updateeid);
        et_uname = (EditText) findViewById(R.id.edemp_et_updatename);
        et_upos = (EditText) findViewById(R.id.edemp_et_updatepos);
        cb_upriv = (CheckBox) findViewById(R.id.edemp_cb_updatepriv);

        btn_add = (Button) findViewById(R.id.edemp_btn_add);
        btn_view = (Button) findViewById(R.id.edemp_btn_view);
        btn_edit = (Button) findViewById(R.id.edemp_btn_edit);
        btn_delete = (Button) findViewById(R.id.edemp_btn_delete);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
