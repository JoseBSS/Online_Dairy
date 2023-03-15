package com.joberdev.online_dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joberdev.online_dairy.AgregarNota.Agregar_Nota;
import com.joberdev.online_dairy.ListarNotas.Listar_Notas;
import com.joberdev.online_dairy.NotasArchivadas.Notas_Archivadas;
import com.joberdev.online_dairy.Perfil.Perfil_Usuario;

public class MainMenu extends AppCompatActivity {
    Button AgregarNotas, ListarNotas, Archivados,Perfil,AcercaDe,CloseSession;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView UidPrincipal, NamesMain, EmailMain;
    ProgressBar progressBarDatos;

    DatabaseReference Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Online Dairy");

        UidPrincipal = findViewById(R.id.UidPrincipal);
        NamesMain = findViewById(R.id.NombresPrincipal);
        EmailMain = findViewById(R.id.CorreoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        Users = FirebaseDatabase.getInstance().getReference("Usuarios");

        AgregarNotas = findViewById(R.id.AgregarNotas);
        ListarNotas = findViewById(R.id.ListarNotas);
        Archivados = findViewById(R.id.Archivados);
        Perfil = findViewById(R.id.Perfil);
        AcercaDe = findViewById(R.id.AcercaDe);
        CloseSession = findViewById(R.id.CloseSession);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        AgregarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Agregar_Nota.class));
                Toast.makeText(MainMenu.this, "Add Note", Toast.LENGTH_SHORT).show();
            }
        });

        ListarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Listar_Notas.class));
                Toast.makeText(MainMenu.this, "List Notes", Toast.LENGTH_SHORT).show();
            }
        });

        Archivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Notas_Archivadas.class));
                Toast.makeText(MainMenu.this, "Archived Notes", Toast.LENGTH_SHORT).show();
            }
        });

        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Perfil_Usuario.class));
                Toast.makeText(MainMenu.this, "User Profile", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "About", Toast.LENGTH_SHORT).show();
            }
        });

        CloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitAplication();
            }
        });
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void ComprobarInicioSesion(){
        if (user!=null){
            //El usuario ha iniciado sesión
            CargaDeDatos();
        }else {
            //Lo dirigirá al MainActivity
            startActivity(new Intent(MainMenu.this,MainActivity.class));
            finish();
        }
    }

    private void CargaDeDatos(){
        Users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Si el usuario existe
                if (snapshot.exists()){
                    //El progressbar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    //Los TextView se muestran
                    UidPrincipal.setVisibility(View.VISIBLE);
                    NamesMain.setVisibility(View.VISIBLE);
                    EmailMain.setVisibility(View.VISIBLE);

                    //Obtener los datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String correo = ""+snapshot.child("correo").getValue();

                    //Setear los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NamesMain.setText(nombres);
                    EmailMain.setText(correo);

                    //Habilitar los botones del menú
                    AgregarNotas.setEnabled(true);
                    ListarNotas.setEnabled(true);
                    Archivados.setEnabled(true);
                    Perfil.setEnabled(true);
                    AcercaDe.setEnabled(true);
                    CloseSession.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ExitAplication() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenu.this, MainActivity.class));
        Toast.makeText(this, "You logged out successfully", Toast.LENGTH_SHORT).show();
    }
}