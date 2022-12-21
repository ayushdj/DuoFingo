package com.example.duofingo;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class Util {

    protected static void updateUserScore(String userName, int score) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Long[] currentUserScore = {0L};
        final String[] id = new String[1];
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (Objects.equals(documentSnapshot.get("userName"), userName)) {
                        id[0] = documentSnapshot.getId();
                        currentUserScore[0] = documentSnapshot.getLong("userScore");
                        break;
                    }
                }
                Log.i("Quiz", id[0]);
                Log.i("Quiz", String.valueOf(score));
                db.collection("users").document(id[0])
                        .update("userScore", currentUserScore[0] + score);
            }
        });
    }
}
