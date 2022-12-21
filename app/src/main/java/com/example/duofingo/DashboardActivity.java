package com.example.duofingo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class DashboardActivity extends AppCompatActivity
        implements ContinueReadingChapterSelectListener, LocationListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DB_DASHBOARD";

    String userName;
    String fullName;
    int pageFrom = -1;

    Handler textHandler = new Handler();
    Button topicSelect;
    RecyclerView continueReadingRV;
    ArrayList<ContinueReadingDataSource> continueReadingDataSource;

    RecyclerView dashBoardRankingRv;
    ArrayList<CountryRankingDataSourceSet> dashBoardRankingDataSource;

    DashBoardRankingAdapter dashBoardRankingAdapter;


    // For global rank.
    RecyclerView dashBoardRankingGlobalRv;
    ArrayList<DashBoardRankingDataSourceSet> dashBoardRankingGlobalDataSource;

    Button chapterSelect;
    Button dashboardDesign;
    Button quizPlay;
    Button viewDiscussions;
    Button weeklyQuiz;
    Button monthlyQuiz;

    TextView locationText;
    String currentCountry;

    TextView continueReadingTextNoDataView;

    List<Pair> countryRanks = new ArrayList<>();

    Chip heyUsername;

    ProgressBar progressBar;

    String[] fineLocation = {Manifest.permission.ACCESS_FINE_LOCATION};

    LocationManager lm;

    TextView scoreView;
    int myScore = 0;

    ImageView profileImage;
    ImageView profileImageBig;

    public static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private SharedPreferences sharedPreferences;
    String userKey;
    String profilePicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        sharedPreferences = getApplicationContext().getSharedPreferences(
                "DuoFingo", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");
        fullName = sharedPreferences.getString("fullName", "");
        userKey = sharedPreferences.getString("userKey", "");
        pageFrom = sharedPreferences.getInt("pageFrom", -1);
        profilePicKey = sharedPreferences.getString("profileKey", "");

        if(pageFrom == 0)
            this.openTopicSelectActivity();

//        db.collection("users").document(userKey).get()
        progressBar = findViewById(R.id.rankingsProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        heyUsername = findViewById(R.id.chipForProfile);
        scoreView = findViewById(R.id.chipForLevel);
        viewDiscussions = findViewById(R.id.view_discussions);
        weeklyQuiz = findViewById(R.id.weeklyQuizButton);
        monthlyQuiz = findViewById(R.id.monthlyQuizButton);

        continueReadingTextNoDataView = findViewById(R.id.continueReadingTextNoData);

        weeklyQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,
                        QuizStartActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("quizType", QuestionType.WEEKLY);
                startActivity(intent);
                // finish();
            }
        });

        monthlyQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,
                        QuizStartActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("quizType", QuestionType.MONTHLY);
                startActivity(intent);
                // finish();
            }
        });

        viewDiscussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DiscussionBoard.class);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });

        scoreView = findViewById(R.id.chipForLevel);
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (Objects.equals(documentSnapshot.get("userName"), userName)) {
                        heyUsername.setText(documentSnapshot.getString("userName"));
                        scoreView.setText(documentSnapshot.get("userScore").toString());
                        currentCountry = documentSnapshot.getString("country");
                        Log.d(TAG, "MY COUNTRY" + currentCountry);
                        break;
                    }
                    // documentReference[0] = db.collection("users").document()
                }
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://duofingo-58001.appspot.com/");

        if(!profilePicKey.equals("")) {
            StorageReference childRefNew = storageReference.child(profilePicKey);

            try {
                File localfile = File.createTempFile("tempfile", ".jpg");
                childRefNew.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
//                                profileImage.setImageBitmap(bitmap);
                                Drawable d = new BitmapDrawable(getResources(), bitmap);
                                heyUsername.setChipIcon(d);

//                                profileImageBig.setImageBitmap(bitmap);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Recycler View populate for continue Reading
        continueReadingDataSource = new ArrayList<>();
        this.getContinueReadingData(userName);

        // Recycler view data for global rank

        dashBoardRankingGlobalDataSource = new ArrayList<>();
        this.getGlobalRankingData(userName);

        heyUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(DashboardActivity.this, profileViewDesign.class);
                intentProfile.putExtra("username", userName);
                intentProfile.putExtra("userKey", userKey);
                intentProfile.putExtra("profilePicKey", profilePicKey);

                startActivity(intentProfile);
            }
        });

        // Recycle View Data for Ranking
        dashBoardRankingDataSource = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    fineLocation, 100);

        }
        getLocation();
        dashBoardRankingRv = findViewById(R.id.dashBoardRankingRecycleView);
        dashBoardRankingRv.setHasFixedSize(true);
        dashBoardRankingRv.setLayoutManager(new LinearLayoutManager(this));
        dashBoardRankingRv.setAdapter(new DashBoardRankingAdapter(dashBoardRankingDataSource, this, fullName));
        locationText = findViewById(R.id.location);

        topicSelect = findViewById(R.id.topic_selection);
        topicSelect.setOnClickListener(v -> openTopicSelectActivity());

        runRankingThread();

        // dashboardDesign = findViewById(R.id.dashboard_design);
        // dashboardDesign.setOnClickListener(v -> openDashboardDesignActivity());
        //
        // quizPlay = findViewById(R.id.quiz_play);
        // quizPlay.setOnClickListener(v -> openQuizPlayActivity());

        /*
         * Getting permissions from the users
         */

    }


    private void pickImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Log.d("Tag in cam uri", imageUri.toString());
                Log.d("Tag in cam", imageStream.toString());
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profileImage.setImageBitmap(selectedImage);

                StorageReference childRef = storageReference.child(UUID.randomUUID().toString());

                // uploading the image
                UploadTask uploadTask = childRef.putFile(imageUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(DashboardActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DashboardActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestStoragePermission() {
        requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                100);
    }

    private boolean checkStoragePermission() {
        boolean perm2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return perm2;
    }

    private boolean checkCameraPermission() {

        boolean perm1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean perm2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return perm1 && perm2;
    }

    private void getGlobalRankingData(String userId) {

        Log.i(TAG, "Getting global ranking data");
        db.collection("users")
                .orderBy("userScore", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int rankIndex = 1;
                            for (DocumentSnapshot document : task.getResult()) {

                                dashBoardRankingGlobalDataSource.add(
                                        new DashBoardRankingDataSourceSet(
                                                document.getString("fullName"),
                                                Integer.toString(rankIndex),
                                                document.getString("country")));
                                rankIndex++;

                            }
                            dashBoardRankingGlobalRv = findViewById(R.id.dashBoardRankingRecycleView_global);
                            dashBoardRankingGlobalRv.setHasFixedSize(true);
                            dashBoardRankingGlobalRv.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                            dashBoardRankingGlobalRv.setAdapter(new DashBoardRankingGlobalAdapter(
                                    dashBoardRankingGlobalDataSource, DashboardActivity.this, fullName));

                        } else {
                            Log.d(TAG, "Error getting documents for global rank: ", task.getException());
                        }
                    }
                });

    }

    public void openTopicSelectActivity() {
        Intent intent = new Intent(this, TopicSelectionActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    public void openChaptersSelectActivity(String topicName, String userName) {
        Intent intent = new Intent(this, ChapterSelectionActivity.class);
        intent.putExtra("topic", topicName);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    public void openDashboardDesignActivity(View view) {
        Intent intent = new Intent(this, DashDesignActivity.class);
        startActivity(intent);
    }

    private void getLocation() {
        try {
            lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the
                // documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, DashboardActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "DuoFingo", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");
        fullName = sharedPreferences.getString("fullName", "");
        userKey = sharedPreferences.getString("userKey", "");
        pageFrom = sharedPreferences.getInt("pageFrom", -1);
        profilePicKey = sharedPreferences.getString("profileKey", "");

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (Objects.equals(documentSnapshot.get("userName"), userName)) {
                        heyUsername.setText("Hello " + documentSnapshot.getString("userName"));
                        scoreView.setText(documentSnapshot.get("userScore").toString());
                        break;
                    }
                    //documentReference[0] = db.collection("users").document()
                }
            }

        });

        // Getting the reference of the storage for firebase store.
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://duofingo-58001.appspot.com/");

        // Passing the profile key to pick the image file reference.
        if(!Objects.equals(profilePicKey, "")) {

            StorageReference childRefNew = storageReference.child(profilePicKey);

            try {
                File localfile = File.createTempFile("tempfile", ".jpg");
                childRefNew.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Convert the file to bitmap.
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                // Convert the bitmap image to drawable -
                                // Only drawable is accepted for chip image.
                                Drawable d = new BitmapDrawable(getResources(), bitmap);
                                heyUsername.setChipIcon(d);

                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSelectChapter(ContinueReadingDataSource continueReadingData) {
        this.openChaptersSelectActivity(continueReadingData.getTopicName(), continueReadingData.getUserName());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            // System.out.println("THIS IS THE CURRENT USER ADDRESS" +
            // addressList.get(0).getAddressLine(0));
            // locationText.setText(addressList.get(0).getAddressLine(0));
            Address addy = addressList.get(0);

            currentCountry = addy.getCountryName();
            final String[] Id = new String[1];
            final Long[] currentUserScore = new Long[1];
            // Update the current user's country
            db.collection("users").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (Objects.equals(documentSnapshot.get("userName"), userName)) {
                            Id[0] = documentSnapshot.getId();
                            currentUserScore[0] = (Long) documentSnapshot.get("userScore");
                            break;
                        }
                        // documentReference[0] = db.collection("users").document()
                    }

                    DocumentReference dr = db.collection("users").document(Id[0]);
                    dr.update("country", addy.getCountryName());
                    dr.update("city", addy.getLocality());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        runRankingThread();
    }

    public void runRankingThread() {
        DetermineRankings determineRankings = new DetermineRankings();
        new Thread(determineRankings).start();
    }

//    class DetermineRankings implements Runnable {
//
//        @Override
//        public void run() {
//
//            try {
//                textHandler.post(() -> {
//                    countryRanks.clear();
//                    dashBoardRankingDataSource.clear();
//                    dashBoardRankingRv.getAdapter().notifyDataSetChanged();
//                    progressBar.setVisibility(View.VISIBLE);
//                });
//
//                textHandler.post(() -> {
//                    progressBar.setVisibility(View.VISIBLE);
//                });
//                Thread.sleep(700);
//                db.collection("users").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            Log.d(TAG, "THIS IS THE CURRENT COUNTRY: " + currentCountry);
//                            if (Objects.equals(documentSnapshot.get("country"), currentCountry)) {
//                                // dashBoardRankingDataSource.add(new DashBoardRankingDataSourceSet("Jai", "1",
//                                // "12"))
//                                // Log.d(TAG, "THIS IS THE CURRENT USER'S FULL NAME: " +
//                                // documentSnapshot.getString("fullName"));
//                                // Log.d(TAG, "THIS IS THE CURRENT USER'S FULL SCORE: " +
//                                // documentSnapshot.getLong("userScore"));
//                                countryRanks.add(new Pair((String) documentSnapshot.getString("fullName"),
//                                        (Long) documentSnapshot.getLong("userScore")));
//                            }
//                        }
//                    }
//
//                    Log.d(TAG, "CURRENT LIST: " + countryRanks);
//
//                    Collections.sort(countryRanks, (a, b) -> b.score.compareTo(a.score));
//                    for (Integer i = 0; i < countryRanks.size(); i++) {
//                        Pair current = countryRanks.get(i);
//                        Integer rank = i + 1;
//
//                        dashBoardRankingDataSource.add(new CountryRankingDataSourceSet(current.name, rank.toString()));
//                    }
//                    textHandler.post(() -> dashBoardRankingRv.getAdapter().notifyDataSetChanged());
//
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            textHandler.post(() -> {
//                dashBoardRankingRv.getAdapter().notifyDataSetChanged();
//                progressBar.setVisibility(View.INVISIBLE);
//            });
//        }
//    }
    class DetermineRankings implements Runnable {

        @Override
        public void run() {

            try {
                textHandler.post(() -> {
                    countryRanks.clear();
                    dashBoardRankingDataSource.clear();
                    dashBoardRankingGlobalDataSource.clear();
                    if (dashBoardRankingRv != null)
                        dashBoardRankingRv.getAdapter().notifyDataSetChanged();
                    if (dashBoardRankingGlobalRv != null)
                        dashBoardRankingGlobalRv.getAdapter().notifyDataSetChanged();
                    progressBar.setVisibility(View.VISIBLE);
                });

                textHandler.post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                });
                Thread.sleep(700);
                db.collection("users").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.d(TAG, "THIS IS THE CURRENT COUNTRY: " + currentCountry);
                            if (Objects.equals(documentSnapshot.get("country"), currentCountry)) {
                                // dashBoardRankingDataSource.add(new DashBoardRankingDataSourceSet("Jai", "1",
                                // "12"))
                                // Log.d(TAG, "THIS IS THE CURRENT USER'S FULL NAME: " +
                                // documentSnapshot.getString("fullName"));
                                // Log.d(TAG, "THIS IS THE CURRENT USER'S FULL SCORE: " +
                                // documentSnapshot.getLong("userScore"));
                                countryRanks.add(new Pair((String) documentSnapshot.getString("fullName"),
                                        (Long) documentSnapshot.getLong("userScore")));
                            }
                        }
                    }

                    Log.d(TAG, "CURRENT LIST: " + countryRanks);

                    Collections.sort(countryRanks, (a, b) -> b.score.compareTo(a.score));
                    for (Integer i = 0; i < countryRanks.size(); i++) {
                        Pair current = countryRanks.get(i);
                        Integer rank = i + 1;

                        dashBoardRankingDataSource.add(new CountryRankingDataSourceSet(current.name, rank.toString()));
                    }

                    db.collection("users")
                            .orderBy("userScore", Query.Direction.DESCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int rankIndex = 1;
                                        for (DocumentSnapshot document : task.getResult()) {

                                            dashBoardRankingGlobalDataSource.add(
                                                    new DashBoardRankingDataSourceSet(
                                                            document.getString("fullName"),
                                                            Integer.toString(rankIndex),
                                                            document.getString("country")));
                                            rankIndex++;

                                        }
                                        dashBoardRankingGlobalRv = findViewById(R.id.dashBoardRankingRecycleView_global);
                                        dashBoardRankingGlobalRv.setHasFixedSize(true);
                                        dashBoardRankingGlobalRv.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                                        dashBoardRankingGlobalRv.setAdapter(new DashBoardRankingGlobalAdapter(
                                                dashBoardRankingGlobalDataSource, DashboardActivity.this, fullName));
                                        textHandler.post(() -> dashBoardRankingGlobalRv.getAdapter().notifyDataSetChanged());

                                    } else {
                                        Log.d(TAG, "Error getting documents for global rank: ", task.getException());
                                    }
                                }
                            });

                    textHandler.post(() -> dashBoardRankingRv.getAdapter().notifyDataSetChanged());
                    //textHandler.post(() -> dashBoardRankingGlobalRv.getAdapter().notifyDataSetChanged());


                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            textHandler.post(() -> {
                dashBoardRankingRv.getAdapter().notifyDataSetChanged();
                dashBoardRankingGlobalRv.getAdapter().notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            });
        }
    }

    private void getContinueReadingData(String userId) {
        Log.i(TAG, "Getting continue reading data");
        db.collection("user_topics").whereEqualTo("userID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                continueReadingDataSource.add(
                                        new ContinueReadingDataSource(
                                                document.get("chapterID").toString(),
                                                document.getString("topicName"),
                                                document.get("chapterID").toString(),
                                                document.get("total_chapters").toString(),
                                                document.get("userID").toString()));
                            }
                            if(continueReadingDataSource.size() == 0) {
                                continueReadingTextNoDataView.setVisibility(View.VISIBLE);
                            }
                            else{
                               continueReadingTextNoDataView.setVisibility(View.GONE);
                            }

                            continueReadingRV = findViewById(R.id.continueReadingRecycleView);
                            continueReadingRV.setHasFixedSize(true);
                            continueReadingRV.setLayoutManager(new LinearLayoutManager(
                                    DashboardActivity.this, LinearLayoutManager.HORIZONTAL,
                                    false));
                            continueReadingRV.setAdapter(new MyContinueReadingAdapter(continueReadingDataSource,
                                    DashboardActivity.this, DashboardActivity.this));

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    @Override
    public void onRestart() {
        super.onRestart();
        runRankingThread();

        continueReadingDataSource.clear();
        continueReadingRV.getAdapter().notifyDataSetChanged();
        getContinueReadingData(userName);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                "DuoFingo", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");
        fullName = sharedPreferences.getString("fullName", "");
        userKey = sharedPreferences.getString("userKey", "");
        pageFrom = sharedPreferences.getInt("pageFrom", -1);
        profilePicKey = sharedPreferences.getString("profileKey", "");

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (Objects.equals(documentSnapshot.get("userName"), userName)) {
                        heyUsername.setText("Hello " + documentSnapshot.getString("userName"));
                        scoreView.setText(documentSnapshot.get("userScore").toString());
                        break;
                    }
                    //documentReference[0] = db.collection("users").document()
                }
            }

        });

        // Getting the reference of the storage for firebase store.
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://duofingo-58001.appspot.com/");

        // Passing the profile key to pick the image file reference.
        if(!Objects.equals(profilePicKey, "")) {

            StorageReference childRefNew = storageReference.child(profilePicKey);

            try {
                File localfile = File.createTempFile("tempfile", ".jpg");
                childRefNew.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Convert the file to bitmap.
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                // Convert the bitmap image to drawable -
                                // Only drawable is accepted for chip image.
                                Drawable d = new BitmapDrawable(getResources(), bitmap);
                                heyUsername.setChipIcon(d);

                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }
}