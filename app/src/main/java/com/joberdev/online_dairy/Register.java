package com.joberdev.online_dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText NameEt, EmailEt, PasswordEt, ConfirmPassword;
    Button RegisterUser;
    TextView GotAccount;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String name = " ", email = " ", password = " ", confirm = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        NameEt = findViewById(R.id.NameEt);
        EmailEt = findViewById(R.id.EmailEt);
        PasswordEt = findViewById(R.id.PasswordEt);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        RegisterUser = findViewById(R.id.RegisterUser);
        GotAccount = findViewById(R.id.GotAccount);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        GotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }

    private void validateData(){
        name = NameEt.getText().toString();
        email = EmailEt.getText().toString();
        password = PasswordEt.getText().toString();
        confirm = ConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter your name",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter your password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirm)){
            Toast.makeText(this,"Confirm your password",Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm)){
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        }
        else{
            CreateAccount();
        }
    }

    private void CreateAccount() {
        progressDialog.setMessage("Creating your account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SaveInformation();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SaveInformation() {
        progressDialog.setMessage("Saving your information");
        progressDialog.dismiss();

        //Taking the information account
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid",  uid);
        Datos.put("correo", email);
        Datos.put("nombres", name);
        Datos.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, MainMenu.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}