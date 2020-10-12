package com.example.contactlessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessapp.DbHelpers.CreateAccountCustomerHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {
    Button login_button;
    EditText username_input;
    EditText password_input;

    private String username;
    private String password;
    private String Customer = "Customer";
    private String Shop= "Shop";
    private String Establishment = "Establishment";
    private  String getEmail;

    private ProgressDialog progressDialog;
    private  FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null){
            firebaseAuth.signOut();
        }

        username_input = findViewById(R.id.username_xml);
        password_input = findViewById(R.id.password_xml);
        login_button = findViewById(R.id.login_xml);
        TextView forgotpass_input = findViewById(R.id.forgotpass_xml);
        TextView createaccount_input = findViewById(R.id.createaccount_xml);
        progressDialog = new ProgressDialog(this);

        forgotpass_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassActivity.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this,"you clicked forgot pass",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        createaccount_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateAccountActivity.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this,"you clicked create account",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void btnLogin(View view) {
         username = username_input.getText().toString().trim();
         password = password_input.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            username_input.setError("enter username");
            return;
        }
        if(TextUtils.isEmpty(password)){
            password_input.setError("enter password");
            return;
        }
        progressDialog.setMessage("Checking account...");
        progressDialog.show();

        //checks username input
        DatabaseReference loginRef = FirebaseDatabase.getInstance().getReference("Registered_Users");
        loginRef.orderByChild("username").equalTo(username)
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       //if username is exist
                       if(snapshot.exists()){
                           //checks the username account type
                           final DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Registered_Users/" + username);
                           accountRef.orderByValue().equalTo(Customer).addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  //if account type exist
                                  if(snapshot.exists()){
                                      //checks user password input
                                      accountRef.orderByValue().equalTo(password).addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              //if exist or match
                                              if(snapshot.exists()){
                                                  DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Registered_Users/" + username + "/emailAddress");
                                                  emailRef.addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                          if(snapshot.exists()){
                                                              getEmail = snapshot.getValue().toString();
                                                              firebaseAuth.signInWithEmailAndPassword(getEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                                                      if(task.isSuccessful()){
                                                                          Intent intent = new Intent(MainActivity.this, CustomerMainActivity.class);
                                                                          intent.putExtra("username_input", username_input.getText().toString());
                                                                          startActivity(intent);
                                                                          finish();
                                                                      }else {
                                                                          Toast.makeText(MainActivity.this, "error!! " ,Toast.LENGTH_LONG ).show();
                                                                      }
                                                                  }
                                                              });
                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(@NonNull DatabaseError error) {

                                                      }
                                                  });

                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
                                  }
                                  else{
                                      accountRef.orderByValue().equalTo(Shop).addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                              if(snapshot.exists()){
                                                  accountRef.orderByValue().equalTo(password).addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                          if(snapshot.exists()){
                                                              DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Registered_Users/" + username + "/email");
                                                              emailRef.addValueEventListener(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                      if(snapshot.exists()){
                                                                           getEmail = snapshot.getValue().toString().trim();
                                                                          firebaseAuth.signInWithEmailAndPassword(getEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                              @Override
                                                                              public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                  if(task.isSuccessful()){
                                                                                      Intent intent = new Intent(MainActivity.this, ShopEstablishmentMainActivity.class);
                                                                                      startActivity(intent);
                                                                                      finish();
                                                                                  }else {
                                                                                      Toast.makeText(MainActivity.this, "error!! " ,Toast.LENGTH_LONG ).show();
                                                                                  }
                                                                              }
                                                                          });
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(@NonNull DatabaseError error) {

                                                                  }
                                                              });
                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(@NonNull DatabaseError error) {

                                                      }
                                                  });
                                              }
                                              else{
                                                  accountRef.orderByValue().equalTo(Establishment).addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                          if(snapshot.exists()){
                                                              accountRef.orderByValue().equalTo(password).addValueEventListener(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                      if(snapshot.exists()){
                                                                          DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Registered_Users/" + username + "/email");
                                                                          emailRef.addValueEventListener(new ValueEventListener() {
                                                                              @Override
                                                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                  if(snapshot.exists()){
                                                                                      getEmail = snapshot.getValue().toString().trim();
                                                                                      firebaseAuth.signInWithEmailAndPassword(getEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                          @Override
                                                                                          public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                              if(task.isSuccessful()){
                                                                                                  Intent intent = new Intent(MainActivity.this, ShopEstablishmentMainActivity.class);
                                                                                                  startActivity(intent);
                                                                                                  finish();
                                                                                              }else {
                                                                                                  Toast.makeText(MainActivity.this, "error!! " ,Toast.LENGTH_LONG ).show();
                                                                                              }
                                                                                          }
                                                                                      });

                                                                                  }
                                                                              }

                                                                              @Override
                                                                              public void onCancelled(@NonNull DatabaseError error) {

                                                                              }
                                                                          });
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(@NonNull DatabaseError error) {

                                                                  }
                                                              });
                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(@NonNull DatabaseError error) {

                                                      }
                                                  });
                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError error) {

                                          }
                                      });
                                  }





                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });
                       }
                       else {
                           Toast.makeText(MainActivity.this, "user not found, please try again", Toast.LENGTH_LONG).show();
                       }progressDialog.dismiss();
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }


               });




//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered_Users");
//        userRef.orderByChild("username")
//                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    DatabaseReference passref = FirebaseDatabase.getInstance().getReference("Registered_Users").child(username);
//                    passref.child("password").equalTo(password).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                Toast.makeText(MainActivity.this,"user match", Toast.LENGTH_LONG).show();
//                            }else{
//                                Toast.makeText(MainActivity.this,"user not found!", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
////                    DatabaseReference passRef = FirebaseDatabase.getInstance().getReference("Registered_Users" + username);
////                    passRef.orderByChild("username").equalTo(username).orderByChild("password").equalTo(password).addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            if(snapshot.exists()){
////                                Toast.makeText(MainActivity.this,"user match", Toast.LENGTH_LONG).show();
////
////                            }else{
////                                Toast.makeText(MainActivity.this,"user not found!", Toast.LENGTH_LONG).show();
////                            }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////
////                        }
////                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}