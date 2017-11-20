package dima.rebenko.notebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import dima.rebenko.notebook.activity.CalclulatorFragment;
import dima.rebenko.notebook.activity.RGB;

public class Navigation extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation);
        ImageButton first = (ImageButton)findViewById(R.id.first_task);
        ImageButton second = (ImageButton)findViewById(R.id.second_task);
        ImageButton third = (ImageButton)findViewById(R.id.third_task);
        first.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.first_task:
            {
                intent = new Intent(getApplicationContext(), RGB.class);
                break;
            }
            case R.id.second_task:
            {
                intent = new Intent(getApplicationContext(), CalclulatorFragment.class);
                break;
            }
            case R.id.third_task:
            {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            }
        }
        startActivity(intent);
    }
}
