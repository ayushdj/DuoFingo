package com.example.duofingo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class AdaptableQuizContent {

    private final QuestionType quizType;
    private final String userName;
    private final String topicName;

    // get questions from the DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<QuestionType> stdQuestionTypes = new ArrayList<>(Arrays.asList(QuestionType.MONTHLY,
            QuestionType.WEEKLY));

    ArrayList<String> userQuestions = new ArrayList<>();
    ArrayList<String> easyQuestions = new ArrayList<>();
    ArrayList<String> mediumQuestions = new ArrayList<>();
    ArrayList<String> hardQuestions = new ArrayList<>();

    public AdaptableQuizContent(QuestionType quizType, String userName, String topicName) {
        this.quizType = quizType;
        this.userName = userName;
        this.topicName = topicName;
    }

    private interface UserScoreCallBack{
        void onCallBack(int userScore);
    }

    private interface UserChapterCallBack{
        void onCallBack(int chapterID);
    }

    private interface QuestionsCallBack{
        void onUserListCallBack(ArrayList<String> questions);
    }

    private void getUserScore(UserScoreCallBack callBack, UserChapterCallBack chapterCallBack) {
        db.collection("users").whereEqualTo("userName", userName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.get("userScore") != null) {
                                    callBack.onCallBack(
                                            ((Long)documentSnapshot.get("userScore")).intValue());
                                    getUserChapterID(callBack);
                                }
                                else {
                                    Log.i("Adaptable", "User Score null: "
                                            + documentSnapshot.toString());
                                }
                            }
                        }
                        else {
                            Log.d("Adaptable", "Cached get failed: ", task.getException());
                        }
                    }
                });

        // TODO: Change userID to userName as model has changed.
        db.collection("user_topics").whereEqualTo("userID", userName)
                .whereEqualTo("topicName", topicName).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (documentSnapshot.get("chapterID") != null) {
                                Log.i("Adaptable", documentSnapshot.toString());
                                chapterCallBack.onCallBack(
                                        ((Long)documentSnapshot.get("chapterID")).intValue());
                            }
                            else {
                                Log.i("Adaptable", "User Score null: "
                                        + documentSnapshot.toString());
                            }
                        }
                    }
                });
    }

    private void getUserChapterID(UserScoreCallBack callBack) {
        // TODO: Change userID to userName as model has changed.
        db.collection("user_topics").whereEqualTo("userID", userName)
                .whereEqualTo("topicName", topicName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot.get("chapterID") != null) {
                        callBack.onCallBack(
                                ((Long)documentSnapshot.get("chapterID")).intValue());
                    }
                    else {
                        Log.i("Adaptable", "User Score null: "
                                + documentSnapshot.toString());
                    }
                }
            }
        });
    }

    private void getUserQuestions(UserScoreCallBack callBack, UserChapterCallBack chapterCallBack) {

        getUserScore(callBack, chapterCallBack);

        db.collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (Objects.equals(documentSnapshot.get("tag"), String.valueOf(quizType))) {
                            if (stdQuestionTypes.contains(quizType)) {
                                // These are WEEKLY and MONTHLY Quizzes.
                                if (Objects.equals(documentSnapshot.getString("difficulty"),
                                        String.valueOf(QuestionDifficulty.EASY))) {
                                    easyQuestions.add(documentSnapshot.getString("question"));
                                } else if (Objects.equals(documentSnapshot.getString("difficulty"),
                                        String.valueOf(QuestionDifficulty.MEDIUM))) {
                                    mediumQuestions.add(documentSnapshot.getString("question"));

                                } else if (Objects.equals(documentSnapshot.getString("difficulty"),
                                        String.valueOf(QuestionDifficulty.HARD))) {
                                    hardQuestions.add(documentSnapshot.getString("question"));
                                }
                            }
                            else {
                                // These are chapter wise quizzes.
                                if (1==1/* TODO: Add condition to match the chapterID*/) {
                                    if (Objects.equals(documentSnapshot.getString("difficulty"),
                                            String.valueOf(QuestionDifficulty.EASY))) {
                                        easyQuestions.add(documentSnapshot.getString("question"));
                                    } else if (Objects.equals(documentSnapshot.getString("difficulty"),
                                            String.valueOf(QuestionDifficulty.MEDIUM))) {
                                        mediumQuestions.add(documentSnapshot.getString("question"));
                                    } else if (Objects.equals(documentSnapshot.getString("difficulty"),
                                            String.valueOf(QuestionDifficulty.HARD))) {
                                        hardQuestions.add(documentSnapshot.getString("question"));
                                    }
                                }
                            }
                        }
                    }
                    int dummyUserScore = 20;

                    // Process all questions and get adaptable questions for users based on their user-score.
                    if (dummyUserScore <= UserType.NOVICE.getMaxUserScore()) {
                        // 6 EASY, 3 MEDIUM, 1 HARD
                        userQuestions.addAll(getRandomElements(easyQuestions,
                                UserType.NOVICE.getNumEasyQuestions()));
                        userQuestions.addAll(getRandomElements(mediumQuestions,
                                UserType.NOVICE.getNumMediumQuestions()));
                        userQuestions.addAll(getRandomElements(hardQuestions,
                                UserType.NOVICE.getNumHardQuestions()));
                    }
                    else if (dummyUserScore <= UserType.INTERMEDIATE.getMaxUserScore()) {
                        // 4 EASY, 3 MEDIUM, 3 HARD
                        userQuestions.addAll(getRandomElements(easyQuestions,
                                UserType.INTERMEDIATE.getNumEasyQuestions()));
                        userQuestions.addAll(getRandomElements(mediumQuestions,
                                UserType.INTERMEDIATE.getNumMediumQuestions()));
                        userQuestions.addAll(getRandomElements(hardQuestions,
                                UserType.INTERMEDIATE.getNumHardQuestions()));
                    }
                    else if (dummyUserScore <= UserType.EXPERT.getMaxUserScore()) {
                        // 2 EASY, 3 MEDIUM, 5 HARD
                        userQuestions.addAll(getRandomElements(easyQuestions,
                                UserType.EXPERT.getNumEasyQuestions()));
                        userQuestions.addAll(getRandomElements(mediumQuestions,
                                UserType.EXPERT.getNumMediumQuestions()));
                        userQuestions.addAll(getRandomElements(hardQuestions,
                                UserType.EXPERT.getNumHardQuestions()));
                    }
//                    callBack.onUserListCallBack(userQuestions);
                }
            }
        });
    }


    /**
     * This function is used to get the list of questions from the DB based on the type of the quiz
     * and user's progress in chapters. This function also takes care of the user score as a factor
     * before assigning the user with list of questions.
     */
    public ArrayList<String> getUserAdaptableQuizzes() {

        // Get user score
        int[] userScore = {-1};
        int[] userCompletedChapter = {-1};
        getUserScore(new UserScoreCallBack() {
            @Override
            public void onCallBack(int score) {
                userScore[0] = score;
                Log.i("Adaptable", "" + score);
            }
        }, new UserChapterCallBack() {
            @Override
            public void onCallBack(int chapterID) {
                userCompletedChapter[0] = chapterID;
                Log.i("Adaptable chapter", "" + chapterID);
            }
        });

        // Get adaptable user quizzes
        getUserQuestions(new UserScoreCallBack() {
            @Override
            public void onCallBack(int userScore) {
                Log.i("Adaptable getUsers' score", String.valueOf(userScore));
            }
        }, new UserChapterCallBack() {
            @Override
            public void onCallBack(int chapterID) {
                Log.i("Adaptable getUsers' chapter", String.valueOf(chapterID));

            }
        });
        return userQuestions;
    }

    public ArrayList<String> getRandomElements(ArrayList<String> list, int totalItems)
    {
        Random rand = new Random();
        ArrayList<String> newList = new ArrayList<>();

        if (list.size() == 0) {
            // TODO: Handle this case when questions aren't enough.
            return list;
        }

        if (list.size() < totalItems) {
            for (int i = 0; i < totalItems; i++) {
                // take a random index between 0 to size of given List
                int randomIndex = rand.nextInt(list.size());

                // add element in temporary list
                newList.add(list.get(randomIndex));
            }
        }
        else {
            for (int i = 0; i < totalItems; i++) {

                // take a random index between 0 to size
                // of given List
                int randomIndex = rand.nextInt(list.size());

                // add element in temporary list
                newList.add(list.get(randomIndex));

                // Remove selected element from original list
                list.remove(randomIndex);
            }
        }
        return newList;
    }
}
