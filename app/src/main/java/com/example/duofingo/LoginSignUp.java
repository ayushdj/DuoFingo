package com.example.duofingo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class LoginSignUp extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DB";
    private boolean isLoginSuccessful = false;
    private String dbPassword;
    SharedPreferences sharedPreferences;

    // Unique key store for the user
    private String userKey;
    private String profileKey = "";

    boolean isSignUp;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch loginSignUpSwitch;
    EditText fullName;
    EditText userName;
    EditText password;
    EditText email;
    TextView loginRegisterText;
    TextView loginAdditionalInformation;
    Button loginRegisterButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        // Create SharedPreferences to store fields within a session.
        sharedPreferences = getSharedPreferences("DuoFingo", MODE_PRIVATE);

        isSignUp = false;
        userKey = "";
        loginSignUpSwitch = findViewById(R.id.login_signup_toggle);
        fullName = findViewById(R.id.signup_full_name);
        fullName.setVisibility(View.INVISIBLE);
        userName = findViewById(R.id.signup_username);
        userName.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);

        password = findViewById(R.id.loginPassword);
        email = findViewById(R.id.loginEmail);
        loginRegisterText = findViewById(R.id.loginRegisterText);
        loginAdditionalInformation = findViewById(R.id.loginAdditionalInformation);
        loginRegisterButton = findViewById(R.id.loginOrRegisterButton);

        loginSignUpSwitch.setOnClickListener(v -> {
            isSignUp = !isSignUp;
            if (isSignUp) {
                fullName.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
                loginRegisterText.setText("Register");
                loginRegisterButton.setText("Register");
                loginAdditionalInformation.setText("Create an account!");
            } else {
                fullName.setVisibility(View.INVISIBLE);
                userName.setVisibility(View.INVISIBLE);
                loginRegisterText.setText("Login");
                loginRegisterButton.setText("Login");
                loginAdditionalInformation.setText("Please sign in to continue.");
            }
        });

        loginRegisterButton.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            if (isSignUp) {
                // do signup things
                if (validateUsername() && validateEmail() && validatePassword() && validateFullName()) {
                    Toast.makeText(LoginSignUp.this, "Registering this user", Toast.LENGTH_SHORT).show();
                    // post to the database
                    postToDB(userName.getText().toString(), email.getText().toString(),
                            password.getText().toString(), fullName.getText().toString());
                    openDashboardActivity(0);
                    finish();
                }

            } else {
                if (validateEmail() && validatePassword()) {
                    // if the information is in the database
                    checkLoginCredentials(email.getText().toString(), password.getText().toString(),
                            new FirestoreCallback() {
                                @Override
                                public void onCallBack(boolean isValidCredentials) {
                                    if (isValidCredentials) {
                                        Toast.makeText(LoginSignUp.this, "Login Successful", Toast.LENGTH_SHORT)
                                                .show();
                                        Util.updateUserScore(userName.getText().toString(),
                                                Constants.USER_DAILY_LOGIN_BONUS);
                                        openDashboardActivity(1);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginSignUp.this,
                                                "Unable to login. Check credentials.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        progressBar.setVisibility(View.GONE);

    }

    private void checkLoginCredentials(String email, String password,
            FirestoreCallback callback) {

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (Objects.equals(documentSnapshot.get("email"), email)
                            && Objects.equals(documentSnapshot.get("password"), password)) {

                        userName.setText(documentSnapshot.getString("userName"));
                        fullName.setText(documentSnapshot.getString("fullName"));
                        // Set the user key
                        userKey = documentSnapshot.getId();
                        profileKey = documentSnapshot.getString("profilePictureID");
                        isLoginSuccessful = true;
                        callback.onCallBack(true);
                    }
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onCallBack(boolean isValidCredentials);
    }

    private void postToDB(String userName, String email, String password, String fullName) {
        UserModel user = new UserModel();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);

        // TODO: Check if the email is already present in the

        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                userKey = documentReference.getId();
                Log.i(TAG, "User Successfully pushed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Unable to create user.");
            }
        });
    }

    /**
     * Validate the email address passed in by the user
     *
     * @return true if the user has a valid email, false otherwise
     */
    private boolean validateEmail() {

        String emailString = email.getText().toString();

        // if the email is empty
        if (emailString.equals("")) {
            email.setError("Email cannot be empty");
            return false;
        }
        if (!emailString.contains("@")) {
            email.setError("Invalid email address");
            return false;
        }

        return true;
    }

    /**
     * Method to validate the username
     *
     * @return true if valid username
     */
    private boolean validateUsername() {
        if (userName.getText().toString().equals("")) {
            userName.setError("Cannot have empty username");
            return false;
        }
        return true;
    }

    /**
     * Method to validate the fullName of the user signing up
     *
     * @return true if valid fullName,
     */
    private boolean validateFullName() {
        if (fullName.getText().toString().equals("")) {
            fullName.setError("Cannot have empty name");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().equals("")) {
            password.setError("password cannot be empty");
            return false;
        } else if (password.getText().toString().length() < 8) {
            password.setError("password should be at least 8 characters long.");
            return false;
        }
        return true;
    }

    private void openDashboardActivity(int page) {
        Intent intent = new Intent(this, DashboardActivity.class);

        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("userName", userName.getText().toString());
        editor.putString("fullName", fullName.getText().toString());
        editor.putString("userKey", userKey);
        editor.putString("profileKey", profileKey);
        editor.putInt("pageFrom", page);
        editor.apply();

        startActivity(intent);
    }

}