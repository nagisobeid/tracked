package om.tracked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity implements Serializable {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CharSequence text = "";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);
    }

    public void onButtonCreateAccountSubmit(View view) {
        Context context = getApplicationContext();
        //CharSequence text = "";
        int duration = Toast.LENGTH_SHORT;

        EditText username = findViewById(R.id.editTextUsername);
        EditText password = findViewById(R.id.editTextPassword);
        EditText passwordVerify = findViewById(R.id.editTextPasswordVerify);
        if(username.getText().toString().trim().length() == 0) {
            //System.out.println("Username Required");
            text = "Username Required";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (password.getText().toString().trim().length() == 0) {
            //System.out.println("Username Required");
            text = "Password Required";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (passwordVerify.getText().toString().trim().length() == 0) {
            //System.out.println("Username Required");
            text = "Verify Password";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!password.getText().toString().equals(passwordVerify.getText().toString())) {
            text = "Passwords Do Not Match";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            //text = "Welcome";
            Map<String, Object> user = new HashMap<>();
            user.put("username", username.getText().toString());
            user.put("password", password.getText().toString());
            // Add a new document with a generated ID
            db.collection("user")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            User user = new User();
                            user.setUsername(username.getText().toString());
                            user.setPw(password.getText().toString());
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            text = "Account Created";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent i = new Intent(CreateAccount.this, Options.class);
                            i.putExtra("user", user);
                            //i.putExtras("userObject", user);
                            //i.putExtra("userObject", user);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            //
        }
    }
}