package com.telemeal;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;

public class EditMenuActivity extends AppCompatActivity {

    private static final String TAG = "EditMenuActivity";
    private static final int PICK_IMAGE_REQUEST_FROM_ADD = 1;
    private static final int PICK_IMAGE_REQUEST_FROM_UPDATE = 2;
    private static final String REQUIRED_FIELD = "Must specify name, price, and category";
    private Uri mImageUrl;
    private String fileName = "test";
    private String name;

    private EditText et_sku;
    private EditText et_name;
    private EditText et_price;
    private EditText et_desc;
    private EditText et_image;
    private Button btn_browse;
    private Spinner spnr_category;

    private TextView tv_usku;
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

    private FirebaseStorage mStorage;
    private DatabaseReference dbFoods;
    private DatabaseReference dbImgMetadata;
    private StorageReference dbImage;

    private ArrayList<Food> foodList = new ArrayList<Food>();
    private ArrayList<UploadImage> imgList = new ArrayList<UploadImage>();
    private EditFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        initializer();
    }

    /**
     * Helper methods which initializes all the member variables
     */
    private void initializer() {
        //initializes all the database and storage references
        //mStorage refers Firebase Storage used for image storage
        mStorage = FirebaseStorage.getInstance();
        //dbFoods refers the "foods" DATABASE table
        dbFoods = FirebaseDatabase.getInstance().getReference("foods");
        //dbImages refers the "uploads" STORAGE table
        dbImage = FirebaseStorage.getInstance().getReference("uploads");
        //dbImgMetadata refers the "image" DATABASE table
        //this table gets filled with metadata when image is uploaded to the "upload" storage
        dbImgMetadata = FirebaseDatabase.getInstance().getReference("image");

        //initialize all the input fields in Add-side
        et_sku = (EditText) findViewById(R.id.edfd_et_sku);
        et_name = (EditText) findViewById(R.id.edfd_et_name);
        et_price = (EditText) findViewById(R.id.edfd_et_price);
        et_desc = (EditText) findViewById(R.id.edfd_et_desc);
        et_image = (EditText) findViewById(R.id.edfd_et_image);
        btn_browse = (Button) findViewById(R.id.edfd_btn_browse);
        spnr_category = (Spinner) findViewById(R.id.edfd_spnr_category);
        spnr_category.setAdapter(new ArrayAdapter<FoodCategory>(this, R.layout.simple_text_layout, FoodCategory.values()));

        //initialize all the input fields in Update-side
        tv_usku = (TextView) findViewById(R.id.edfd_tv_updateSKU);
        spnr_uname = (Spinner) findViewById(R.id.edfd_spnr_updatename);
        et_uprice = (EditText) findViewById(R.id.edfd_et_updateprice);
        et_udesc = (EditText) findViewById(R.id.edfd_et_updatedesc);
        et_uimage = (EditText) findViewById(R.id.edfd_et_updateimage);
        btn_ubrowse = (Button) findViewById(R.id.edfd_btn_updatebrowse);
        spnr_ucategory = (Spinner) findViewById(R.id.edfd_spnr_updatecategory);
        spnr_ucategory.setAdapter(new ArrayAdapter<FoodCategory>(this, R.layout.simple_text_layout, FoodCategory.values()));

        //initialize all the buttons
        btn_add_fd = (Button) findViewById(R.id.edfd_btn_addfooditem);
        btn_view_fd = (Button) findViewById(R.id.edfd_btn_viewfooditem);
        btn_edit_fd = (Button) findViewById(R.id.edfd_btn_editfooditem);
        btn_delete_fd = (Button) findViewById(R.id.edfd_btn_deletefooditem);

        //initialize the adapter
        adapter = new EditFoodAdapter(EditMenuActivity.this, R.layout.simple_text_layout, foodList);
        spnr_uname.setAdapter(adapter);
        //set the item click listener
        //this will auto-set the input fields in Update-side
        spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Food fd = (Food)adapterView.getItemAtPosition(i);

                tv_usku.setText(fd.getSku());
                et_uprice.setText(fd.getPrice().toString());
                et_uimage.setText(fd.getImage());
                et_udesc.setText(fd.getDescription());
                spnr_ucategory.setSelection(fd.getCategory().ordinal());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Add value change listener to the "food" data table
        dbFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Every time when the data has changed, clear local memory and get the snapshow (Structure) of the data table,
                //and re-fill the memory with updated/changed objects
                foodList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                //notify adapter that the data table has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });

        //set the click listener for the add button
        //it will invoke helper method "addFood"
        btn_add_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

        //set the click listener for the view button
        //it will open new activity showing Food data
        btn_view_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewFdIntent = new Intent(EditMenuActivity.this, ViewFoodActivity.class);
                startActivity(viewFdIntent);
            }
        });

        //set the click listener for the update button
        //it will invoke helper method "editFood"
        btn_edit_fd.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        }));

        //set the click listener for the delete button
        //it will invoke helper method "deleteFood"
        btn_delete_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood();
            }
        });

        //set the click listener for the browse button
        //it will invoke help method "openFileChooser"
        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST_FROM_ADD);
            }
        });

        //set the click listener for the browse button
        //it will invoke help method "openFileChooser"
        btn_ubrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST_FROM_UPDATE);
            }
        });
    }

    /**
     * Helper method which opens file chooser for the image
     * @param code code that tells which button called the method
     */
    private void openFileChooser(int code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
    }

    /**
     * Show activity after certain task is done.
     * In this class, it is used for image browser
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK
                && data != null
                && data.getData() != null){
            mImageUrl = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(mImageUrl, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            fileName = returnCursor.getString(nameIndex);
            if(requestCode == PICK_IMAGE_REQUEST_FROM_ADD)
                et_image.setText(returnCursor.getString(nameIndex));
            if(requestCode == PICK_IMAGE_REQUEST_FROM_UPDATE)
                et_uimage.setText(returnCursor.getString(nameIndex));
        }
    }

    /**
     * Extract the extension name from the file
     * @param uri The full path of the file
     * @return The extension name of the file
     */
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    /**
     * Helper method which upload the image to the Storage
     */
    private void uploadImage(){
        //if image is selected,
        if(mImageUrl != null){
            //create instance (folder) with the name as a current time plus extension name
            StorageReference fileRef = dbImage.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUrl));

            //save file into the instance (folder)
            fileRef.putFile(mImageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //print out the message and save two meta data
                            //  1. Name of the image as same as name of the food (easy to refer)
                            //  2. Downloadable Uri (to download the image to display)
                            Toast.makeText(EditMenuActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            UploadImage upload = new UploadImage(name, taskSnapshot.getDownloadUrl().toString());
                            String uploadId = dbImgMetadata.push().getKey();
                            dbImgMetadata.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //print out the message if fails
                            Toast.makeText(EditMenuActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            //release the image from the memory so that when no file is selected right after, it won't come back to this if statement
            mImageUrl = null;
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Helper method which adds food into the database
     */
    private void addFood(){
        //gets the image file selected as a result of file browser
        StorageReference fileRef = dbImage.child(fileName);
        //try upload the image
        uploadImage();

        //Validates inputs
        if(et_name.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Name field is empty." + REQUIRED_FIELD, Toast.LENGTH_LONG).show();
            return;
        }else if(et_price.getText().toString().trim().length() == 0){
            Toast.makeText(this, "Price field is empty." + REQUIRED_FIELD, Toast.LENGTH_LONG).show();
            return;
        }

        //gets all the inputs
        String sku = et_sku.getText().toString();
        name = et_name.getText().toString();
        double price = Double.parseDouble(et_price.getText().toString());
        String desc = et_desc.getText().toString();
        String image = "";
        if(et_image.getText().toString().trim().length() == 0){}
        else
            image = fileRef.getDownloadUrl().toString();
        FoodCategory category = FoodCategory.values()[spnr_category.getSelectedItemPosition()];

        //Save data into the database
        //generate the space for the data to be saved and gets the random id key from it
        String id = dbFoods.push().getKey();

        //create food object
        Food food = new Food(sku, name, price, desc, image, category);

        //set the object into the space where it has matching key
        dbFoods.child(id).setValue(food);

        clearFields();
        Toast.makeText(this, "Food " + name + " added", Toast.LENGTH_LONG).show();
    }

    /**
     * Helper method which change the data in the database
     */
    private void editFood(){
        //Query the data with the name of the food (Consider Food name as a primary key)
        //because we do not know the random key that google has generated.
        dbFoods.orderByChild("name").equalTo(((Food) spnr_uname.getSelectedItem()).getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            //get the food instance
                            Food food = d.getValue(Food.class);

                            //remove image
                            deleteImage();

                            //get updated info
                            name = food.getName();
                            double price = Double.parseDouble(et_uprice.getText().toString());
                            String desc = et_udesc.getText().toString();
                            FoodCategory category = FoodCategory.values()[spnr_ucategory.getSelectedItemPosition()];

                            //modify food instance
                            food.setPrice(price);
                            food.setDescription(desc);
                            StorageReference fileRef = dbImage.child(fileName);
                            if(!fileRef.getDownloadUrl().toString().equals(food.getImage()))
                                food.setImage(fileRef.getDownloadUrl().toString());
                            if(et_uimage.getText().toString().trim().length() == 0)
                                food.setImage("");
                            food.setCategory(category);

                            //upload image if any
                            uploadImage();

                            //upload change in db
                            dbFoods.child(d.getKey()).setValue(food);

                            //notify user
                            Toast.makeText(EditMenuActivity.this, food.getName() + " is updated ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        clearFields();
    }

    /**
     * helper method which clears all the input fields after button click event
     */
    private void clearFields() {
        et_sku.setText(null);
        et_name.setText(null);
        et_desc.setText(null);
        et_price.setText(null);
        et_image.setText(null);

        spnr_uname.setSelection(0);
    }

    /**
     * Helper method which delete the instance in the database
     */
    private void deleteFood(){
        //Like edit, Query data first
        dbFoods.orderByChild("name").equalTo(((Food) spnr_uname.getSelectedItem()).getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            //get the data
                            Food food = d.getValue(Food.class);
                            deleteImage();
                            dbFoods.child(d.getKey()).removeValue();
                            Toast.makeText(EditMenuActivity.this, food.getName() + " is removed ", Toast.LENGTH_LONG).show();
                        }
                        clearFields();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Helper method which delete the image in the storage on item delete/edit
     */
    private void deleteImage(){
        //Query the database for image meta data
        dbImgMetadata.orderByChild("name").equalTo(((Food) spnr_uname.getSelectedItem()).getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            //delete the image file in the storage and the metadata in the database
                            UploadImage img = d.getValue(UploadImage.class);
                            StorageReference ref = mStorage.getReferenceFromUrl(img.getImageUrl());
                            ref.delete();
                            dbImgMetadata.child(d.getKey()).removeValue();
                        }
                        clearFields();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
