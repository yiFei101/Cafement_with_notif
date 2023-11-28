    package com.example.loginauthentication.MerchantPanel;

    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.loginauthentication.R;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.material.textfield.TextInputLayout;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;

    import java.io.IOException;
    import java.util.UUID;

    public class Merchant_post_dish extends AppCompatActivity {
        private Button btnChoose;
        private ImageView imageView;
        private Button post; // Add Post button
        private Uri filePath;
        private final int PICK_IMAGE_REQUEST = 71;
        private StorageReference storageReference;
        private DatabaseReference databaseReference;
        private FirebaseAuth mAuth;
        Spinner Dishes;
        String dishes;

        // Define other input fields
        TextInputLayout desc, qty, pri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_merchant_post_dish);

            // Initialize Views
            btnChoose = findViewById(R.id.btnChoose);
            post = findViewById(R.id.post); // Initialize Post button
            imageView = findViewById(R.id.imgView);
            desc = findViewById(R.id.description);
            qty = findViewById(R.id.Quantity);
            pri = findViewById(R.id.price);
            Dishes = (Spinner) findViewById(R.id.dishes);

            // Initialize Firebase Authentication
            mAuth = FirebaseAuth.getInstance();

            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            // Initialize Firebase Realtime Database
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("FoodDetails");

            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            // Add a click listener for the Post button
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadImageAndData(); // Call the uploadImageAndData function when the Post button is clicked
                }
            });
        }

        private void chooseImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                    // You can choose to upload immediately here or when the Post button is clicked
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void uploadImageAndData() {
            if (filePath != null) {
                // Generate a random UID for this data entry
                final String randomUID = UUID.randomUUID().toString();

                // Get the current user's ID (Merchant ID)
                String merchantId = mAuth.getCurrentUser().getUid();

                // Get data from input fields
                dishes = Dishes.getSelectedItem().toString().trim();

                String description = desc.getEditText().getText().toString().trim();
                String quantity = qty.getEditText().getText().toString().trim();
                String price = pri.getEditText().getText().toString().trim();

                // Define a reference to the Firebase Storage
                StorageReference ref = storageReference.child("images/" + randomUID);

                // Upload the image to Firebase Storage
                UploadTask uploadTask = ref.putFile(filePath);

                // Register observers to listen for when the upload is done or if it fails
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL for the uploaded image
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Create a data object with the image URL and other data
                                String imageURL = uri.toString(); // Get the actual URL of the uploaded image

                                UpdateDishModel dishModel = new UpdateDishModel();
                                String selectedDish = Dishes.getSelectedItem().toString();
                                dishModel.setDishes(selectedDish);
                                dishModel.setDescription(description);
                                dishModel.setQuantity(quantity);
                                dishModel.setPrice(price);
                                dishModel.setImageURL(imageURL);
                                dishModel.setRandomUID(randomUID);
                                dishModel.setMerchantId(merchantId);

                                // Upload the data to Firebase Realtime Database
                                databaseReference.child(merchantId).child(randomUID).setValue(dishModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Merchant_post_dish.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Merchant_post_dish.this, "Data Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Merchant_post_dish.this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
