package om.tracked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    CharSequence text = "";
    int duration = Toast.LENGTH_SHORT;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
    }

    public void onButtonLoginSubmit(View view) {
        if(username.getText().toString().trim().length() == 0) {
            text = "Please enter username";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if(password.getText().toString().trim().length() == 0) {
            text = "Please enter password";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            System.out.println("INSIDE ON COMPLETE*************************************");
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    if(username.getText().toString().equals(document.getString("username"))) {
                                        //Log.d(TAG, document.getId() + " => " + document.getString("password"));
                                        if(password.getText().toString().equals(document.getString("password"))) {
                                            User user = new User();
                                            user.setPw(password.getText().toString());
                                            user.setUsername(username.getText().toString());
                                            Intent i = new Intent(Login.this, Home.class);
                                            i.putExtra("user", user);
                                            //i.putExtras("userObject", user);
                                            //i.putExtra("userObject", user);
                                            startActivity(i);
                                        } else {
                                            text = "Password is incorrect";
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }

                                    }
                                    //Log.d(TAG, document.getId() + " => " + document.getString("password"));
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                //System.out.println("*********************************** ERROR NAGI");
                            }
                        }
                    });
        }
    }
}