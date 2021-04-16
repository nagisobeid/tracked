package om.tracked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Options extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("user");
    }

    public void onCreateGroupClicked(View view) {
        Intent cg = new Intent(Options.this, CreateGroup.class);
        cg.putExtra("user", user);
        startActivity(cg);
    }

    public void onJoinGroupClicked(View view) {
    }
}