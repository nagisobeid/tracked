package om.tracked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    //Group group = new Group();
    User user = new User();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> groups = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        Intent i = getIntent();
        /*group = (Group)i.getSerializableExtra("group");
        System.out.println(group.getgName());*/
        user = (User)i.getSerializableExtra("user");
        listView = (ListView) findViewById(R.id.listViewGroups);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);
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
                                groups.add(document.getString("name"));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groups);
        //listView.setAdapter(adapter);
    }
}