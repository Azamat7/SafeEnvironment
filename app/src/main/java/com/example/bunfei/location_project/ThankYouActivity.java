package com.example.bunfei.location_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        final Button Searchbutton = (Button) findViewById(R.id.SearchAgain);
        Searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntent = new Intent(ThankYouActivity.this, MyLocationUsingLocationAPI.class);
                startActivity(nextIntent);
            }
        });
    }
}
