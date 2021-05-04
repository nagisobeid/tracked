package om.tracked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class JoinGroup extends AppCompatActivity {

    User user = new User();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> groups = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    String joinGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_group);
        listView = (ListView) findViewById(R.id.listViewGroups);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String gr = (String) (listView.getItemAtPosition(position));
                db.collection("group")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if(document.get("name").equals(gr)) {
                                            DocumentReference ref = document.getReference();
                                            //DocumentReference ref = db.collection("group").document();
                                            ref.update("members", FieldValue.arrayUnion(user.getUsername()));
                                            Intent i = new Intent(JoinGroup.this, Home.class);
                                            i.putExtra("user", user);
                                            startActivity(i);
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                /*//String group = (String)adapter.getItemAtPosition(position);
                Group group = new Group();
                group.setgName((String)adapter.getItemAtPosition(position));
                //System.out.println(value);
                Intent i = new Intent(Home.this, GroupHome.class);
                i.putExtra("group", group);
                startActivity(i);*/
            }
        });
        //
        displayGroups();
    }

    public void displayGroups() {
        db.collection("group")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                groups.add(document.get("name").toString());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}