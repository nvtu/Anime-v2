package joker.anime_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity {

    ImageView coverPic, profilePic;
    EditText etName, etEmail, etJob, etQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initLayouts();
        setEvents();
    }

    private void setEvents() {
        coverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initLayouts() {
        etName = (EditText) findViewById(R.id.chooseName);
        etEmail = (EditText) findViewById(R.id.chooseEmail);
        etJob = (EditText) findViewById(R.id.chooseJob);
        etQuote = (EditText) findViewById(R.id.chooseQuote);
        coverPic = (ImageView) findViewById(R.id.coverPicture);
        profilePic = (ImageView) findViewById(R.id.profilePicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.yes:
                transferDataToPersonal();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void transferDataToPersonal() {
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.hasIntent = true;
        startActivity(intent);
    }
}
