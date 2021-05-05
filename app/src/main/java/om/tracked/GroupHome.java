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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupHome extends AppCompatActivity {

    //User user = new User();
    Group group = new Group();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> members = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group_home);

        Intent i = getIntent();
        group = (Group) i.getSerializableExtra("group");
        listView = (ListView) findViewById(R.id.listViewMembers);
        adapter = new ArrayAdapter<String>(this, R.layout.custom_lv, members);
        listView.setAdapter(adapter);
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
                System.out.println(value);
                Intent i = new Intent(GroupHome.this, MapsActivity.class);
                i.putExtra("member", value);
                //i.putExtras("userObject", user);
                //i.putExtra("userObject", user);
                startActivity(i);
            }
        });
        displayMembers();
    }

    public void displayMembers() {
        db.collection("group")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("name").equals(group.getgName())) {
                                    ArrayList<String> list = (ArrayList<String>) document.get("members");
                                    for (String member : list) {
                                        System.out.println(member);
                                        members.add(member);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}