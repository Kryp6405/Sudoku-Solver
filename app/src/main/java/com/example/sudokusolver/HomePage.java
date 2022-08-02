package com.example.sudokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class HomePage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    GoogleSignInClient gsic;
    final int CODE = 1;
    FirebaseAuth auth;
    SignInButton signInButton;
    Button open;
    ImageButton profile, signout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        signInButton = findViewById(R.id.signInButton);
        open = findViewById(R.id.open);
        profile = findViewById(R.id.profile);
        signout = findViewById(R.id.signout);

        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("850772773658-8gtp38ts2o0tndkkn6asqddv9vq7te9o.apps.googleusercontent.com").requestEmail().build();
        gsic = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsic.getSignInIntent();
                startActivityForResult(intent, CODE);
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, UserProfile.class);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
                Toast.makeText(getApplicationContext(), "Signed Out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE){
            System.out.println("In if statment");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                System.out.println("In try statment");
                GoogleSignInAccount gsia = task.getResult(ApiException.class);
                Toast.makeText(getApplicationContext(), "Signed In!", Toast.LENGTH_SHORT).show();
                AuthCredential authCredential = GoogleAuthProvider.getCredential(gsia.getIdToken(), null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successful Auth!", Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = auth.getCurrentUser();
                            //GoogleSignInAccount gsia = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            startActivity(new Intent(HomePage.this, UserProfile.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed Auth", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch(ApiException e){
                System.out.println("In catch statment");
                Toast.makeText(getApplicationContext(), "Failed To Sign In", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else{
            System.out.println("In else statment");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
