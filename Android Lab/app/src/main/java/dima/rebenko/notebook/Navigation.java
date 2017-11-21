package dima.rebenko.notebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import dima.rebenko.notebook.Helpers.CustomTextView;
import dima.rebenko.notebook.Helpers.ThemeApp;
import dima.rebenko.notebook.Helpers.Util;
import dima.rebenko.notebook.activity.BaseActivity;
import dima.rebenko.notebook.activity.CalclulatorFragment;
import dima.rebenko.notebook.activity.MainActivity;
import dima.rebenko.notebook.activity.RGB;

public class Navigation extends BaseActivity implements View.OnClickListener {

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
        CustomTextView.setupTextView((TextView)findViewById(R.id.textViewSize));
        EditText fontSize = (EditText)findViewById(R.id.fontSize);
        Button changeFontSize = (Button)findViewById(R.id.changeButton);
        changeFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fontSize.getText().equals(""))
                {
                    ThemeApp.currentFontSize = Integer.valueOf(fontSize.getText().toString());
                }
            }
        });
        Spinner themeChanger = (Spinner)findViewById(R.id.theme_changer);
        themeChanger.setSelection(ThemeApp.currentPosition);
        ThemeApp.currentPosition = themeChanger.getSelectedItemPosition();
        themeChanger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (ThemeApp.currentPosition != position) {
                    Util.changeToTheme(Navigation.this, position);
                }
                ThemeApp.currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
