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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateGroup extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;
    Context context;
    CharSequence text = "";
    int duration = Toast.LENGTH_SHORT;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_group);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");
        context = getApplicationContext();
        //System.out.println("In Create Group");
    }

    public void onCreateGroupClicked(View view) {
        EditText groupName = findViewById(R.id.editTextGroupName);
        if(groupName.getText().toString().trim().length() == 0) {
            text = "Group Name Cannot Be Empty";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Map<String, Object> group = new HashMap<>();
            group.put("name", groupName.getText().toString());
            group.put("members", Arrays.asList(user.getUsername()));
            db.collection("group")
                    .add(group)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Group gr = new Group();
                            gr.setgName(groupName.getText().toString());
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            text = "Group Created";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent i = new Intent(CreateGroup.this, Home.class);
                            i.putExtra("user", user);
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