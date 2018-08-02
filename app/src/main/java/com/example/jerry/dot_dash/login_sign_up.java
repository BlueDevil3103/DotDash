package com.example.jerry.dot_dash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class login_sign_up extends Activity {

    Button signup,login,forgot;
    EditText inputEmail,inputPassword;
    LinearLayout layout1, layout2;
    Animation uptodown;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        layout1=(LinearLayout)findViewById(R.id.layout1);
        layout2=(LinearLayout)findViewById(R.id.layout2);
        uptodown = AnimationUtils.loadAnimation(login_sign_up.this,R.anim.uptodown);
        layout1.setAnimation(uptodown);

        signup=(Button)findViewById(R.id.signup);
        login=(Button)findViewById(R.id.login);
        forgot=(Button)findViewById(R.id.forgot);
        inputEmail=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        //Intent for the Sign Up Activity.

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_sign_up.this,Reset_Password.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_sign_up.this,Signup.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email=inputEmail.getText().toString().trim();
                password=inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(login_sign_up.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(login_sign_up.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Logging in please wait....");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login_sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful() ) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.min_pass));
                            }else
                            {
                                Toast.makeText(login_sign_up.this, "Authentication failed", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                        else
                        {
                            checkIfEmailVerified();
                        }

                    }
                });

            }
        });



    }
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Intent intent=new Intent(login_sign_up.this,MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(login_sign_up.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(login_sign_up.this, "Email not Verified", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            //restart this activity

        }
    }
}
