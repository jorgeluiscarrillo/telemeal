package com.telemeal;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;

public class EditMenuActivity extends AppCompatActivity {

    private static final String TAG = "EditMenuActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUrl;

    private EditText et_sku;
    private EditText et_name;
    private EditText et_price;
    private EditText et_desc;
    private EditText et_image;
    private Button btn_browse;
    private Spinner spnr_category;

    private Spinner spnr_uname;
    private EditText et_uprice;
    private EditText et_udesc;
    private EditText et_uimage;
    private Button btn_ubrowse;
    private Spinner spnr_ucategory;

    private Button btn_add_fd;
    private Button btn_view_fd;
    private Button btn_edit_fd;
    private Button btn_delete_fd;

    private DatabaseReference dbFoods;
    private StorageReference dbImage;

    private ArrayList<Food> foodList = new ArrayList<Food>();
    private EditFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        initializer();
    }

    private void initializer() {
        dbFoods = FirebaseDatabase.getInstance().getReference("foods");
        dbImage = FirebaseStorage.getInstance().getReference("images");

        et_sku = (EditText) findViewById(R.id.edfd_et_sku);
        et_name = (android.widget.EditText) findViewById(R.id.edfd_et_name);
        et_price = (EditText) findViewById(R.id.edfd_et_price);
        et_desc = (EditText) findViewById(R.id.edfd_et_desc);
        et_image = (EditText) findViewById(R.id.edfd_et_image);
        btn_browse = (Button) findViewById(R.id.edfd_btn_browse);
        spnr_category = (Spinner) findViewById(R.id.edfd_spnr_category);
        spnr_category.setAdapter(new ArrayAdapter<FoodCategory>(this, R.layout.simple_text_layout, FoodCategory.values()));

        spnr_uname = (Spinner) findViewById(R.id.edfd_spnr_updatename);
        et_uprice = (EditText) findViewById(R.id.edfd_et_updateprice);
        et_udesc = (EditText) findViewById(R.id.edfd_et_updatedesc);
        et_uimage = (EditText) findViewById(R.id.edfd_et_updateimage);
        btn_ubrowse = (Button) findViewById(R.id.edfd_btn_updatebrowse);
        spnr_ucategory = (Spinner) findViewById(R.id.edfd_spnr_updatecategory);
        spnr_ucategory.setAdapter(new ArrayAdapter<FoodCategory>(this, R.layout.simple_text_layout, FoodCategory.values()));

        btn_add_fd = (Button) findViewById(R.id.edfd_btn_addfooditem);
        btn_view_fd = (Button) findViewById(R.id.edfd_btn_viewfooditem);
        btn_edit_fd = (Button) findViewById(R.id.edfd_btn_editfooditem);
        btn_delete_fd = (Button) findViewById(R.id.edfd_btn_deletefooditem);

        dbFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foodList.add(food);
                }

                adapter = new EditFoodAdapter(EditMenuActivity.this, R.layout.simple_text_layout, foodList);
                spnr_uname.setAdapter(adapter);
                spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Food fd = (Food)adapterView.getItemAtPosition(i);

                        et_uprice.setText(fd.getPrice().toString());
                        et_uimage.setText(fd.getImage());
                        et_udesc.setText(fd.getDescription());
                        spnr_ucategory.setSelection(fd.getCategory().ordinal());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });

        adapter = new EditFoodAdapter(EditMenuActivity.this, android.R.layout.simple_spinner_item, foodList);
        spnr_uname.setAdapter(adapter);
        spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = adapter.getItem(i);
                et_uprice.setText(food.getPrice().toString());
                et_udesc.setText(food.getDescription());
                et_uimage.setText(food.getImage());
                spnr_ucategory.setSelection(food.getCategory().ordinal());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_add_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

        btn_view_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewFdIntent = new Intent(EditMenuActivity.this, ViewFoodActivity.class);
                startActivity(viewFdIntent);
            }
        });

        btn_edit_fd.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        }));

        btn_delete_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood();
            }
        });

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btn_ubrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){
            mImageUrl = data.getData();

            et_image.setText(mImageUrl.toString());
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void selectImage(){
        if(mImageUrl != null){
            StorageReference fileRef = dbImage.child(System.currentTimeMillis()
            + "." + getExtension(mImageUrl));

            //fileRef.putFile(mImageUrl)
            //        .addOnSuccessListener(new )
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

    private void addFood(){
        String sku = et_sku.getText().toString();
        String name = et_name.getText().toString();
        double price = Double.parseDouble(et_price.getText().toString());
        String desc = et_desc.getText().toString();
        String image = et_image.getText().toString();
        FoodCategory category = FoodCategory.values()[spnr_category.getSelectedItemPosition()];

        if(!TextUtils.isEmpty(name)){
            String id = dbFoods.push().getKey();

            Food food = new Food(sku, name, price, desc, image, category);

            dbFoods.child(id).setValue(food);
            foodList.clear();
            Toast.makeText(this, "Food " + name + " added", Toast.LENGTH_LONG).show();
        }
    }

    private void editFood(){

    }

    private void deleteFood(){
        dbFoods.orderByChild("name").equalTo(((Food) spnr_uname.getSelectedItem()).getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            Food food = d.getValue(Food.class);
                            dbFoods.child(d.getKey()).removeValue();
                            foodList.clear();
                            Toast.makeText(EditMenuActivity.this, food.getName() + " is removed ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
