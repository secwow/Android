package dima.rebenko.notebook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import dima.rebenko.notebook.R;

public class RGB extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;
    private View coloredView;
    private int red, green, blue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rgb);

        coloredView = findViewById(R.id.colored_view);
        redBar = (SeekBar) findViewById(R.id.red_seekbar);
        blueBar = (SeekBar)findViewById(R.id.blue_seekbar);
        greenBar = (SeekBar)findViewById(R.id.green_seekbar);
        greenBar.setOnSeekBarChangeListener(this);
        redBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()){
            case R.id.blue_seekbar:
                this.blue = i;
                break;
            case R.id.red_seekbar:
                this.red = i;
                break;
            case R.id.green_seekbar:
                this.green = i;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.updateColor();
    }

    private void updateColor()
    {
        this.coloredView.setBackgroundColor(0xff000000 + red * 0x10000 + green * 0x100
            + blue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
