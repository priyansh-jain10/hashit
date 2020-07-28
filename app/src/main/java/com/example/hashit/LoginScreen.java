package com.example.hashit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hashit.util.JournalApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("User");
    private DatabaseReference databaseReference;
    private EditText emailEdittext;
    private EditText passwordEdittext;
    private EditText confirmpasswordEdittext;
    private ProgressBar progressBar;
    private Button createaccountbtn;
    private EditText usernameedittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
       toolbar=findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       //getSupportActionBar().setTitle("Sign Up");
       //getSupportActionBar().setTitle("Sign Up");
//         textView = findViewById(R.id.toolbarTextView);
//        textView.setText("SignUp");
        firebaseAuth = FirebaseAuth.getInstance();
        createaccountbtn = findViewById(R.id.confirmbutton);
        progressBar = findViewById(R.id.progressbar);
        usernameedittext = findViewById(R.id.fullname);
        emailEdittext = findViewById(R.id.username);
        passwordEdittext = findViewById(R.id.password);
        confirmpasswordEdittext=findViewById(R.id.confirmpassword);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser = firebaseAuth.getCurrentUser();
                if (currentuser != null) {
                    // user is already logged in


                } else {

                }
            }
        };
        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEdittext.getText().toString()) &&
                        !TextUtils.isEmpty(passwordEdittext.getText().toString())
                        && !TextUtils.isEmpty(usernameedittext.getText().toString())) {
                    String email = emailEdittext.getText().toString().trim();
                    String password = passwordEdittext.getText().toString().trim();
                    String confirmpassword=confirmpasswordEdittext.getText().toString().trim();
                    String username = usernameedittext.getText().toString().trim();
                    if(confirmpassword.equals(password)){
                        CreateUserEmailAccount(email, password, username);
                    }
                    else{
                        Toast.makeText(LoginScreen.this, "Please verify confirm password", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(LoginScreen.this, "Empty fields Not Allowed", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void CreateUserEmailAccount(final String email, String password, final String username) {
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //we take user to Add Journal Activity
                        Toast.makeText(LoginScreen.this, "task is successful", Toast.LENGTH_LONG).show();
                        currentuser = firebaseAuth.getCurrentUser();
                        assert currentuser != null;
                        final String currentUserId = currentuser.getUid();
                        //create a user map
                        Map<String, String> userObj = new HashMap<>();
                        userObj.put("username", username);
                        userObj.put("userId", currentUserId);

                        //Save to our firestore database
                        databaseReference= FirebaseDatabase.getInstance().getReference().child("BlogUser");
                        DatabaseReference newuser=databaseReference.child(currentUserId);
                        newuser.child("name").setValue(username);
                        newuser.child("email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginScreen.this, SignInScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
//                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d("CreateAc", "onSuccess: Sucess");
//                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (Objects.requireNonNull(task.getResult()).exists()) {
//                                            progressBar.setVisibility(View.INVISIBLE);
//                                            String name = task.getResult().getString("username");
////                                            JournalApi journalApi = JournalApi.getInstance();
////                                            journalApi.setUserId(currentUserId);
////                                            journalApi.setUsername(name);
//                                            Log.d("CreateAccount", "onComplete: entered sucessfully");
//                                            Intent intent = new Intent(LoginScreen.this, SignInScreen.class);
//                                                   intent.putExtra("username",name);
//                                                   intent.putExtra("userId",currentUserId); //Replaced by Journal Api global by
////                                                       declaring in manifest and puting data in ito
//                                            // of application superclass
//                                            startActivity(intent);
//                                            finish();
//                                        } else {
//                                            progressBar.setVisibility(View.INVISIBLE);
//
//                                        }
//
//                                    }
//                                });
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });


                    } else {
                        Toast.makeText(LoginScreen.this, "task is unsuccessful", Toast.LENGTH_LONG).show();
                        Log.w("createAccont", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginScreen.this, "Authentication failed.",
                                Toast.LENGTH_LONG).show();
                        try {
                            throw task.getException();
                        }
                        catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthEmailException e){
                            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthException e){
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {


        }
    }
    }

