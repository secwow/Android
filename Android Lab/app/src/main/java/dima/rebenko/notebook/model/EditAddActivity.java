package dima.rebenko.notebook.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dima.rebenko.notebook.R;


public class EditAddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    TextView nameView, descriptionView, dateView, timeView;
    SeekBar impotance;
    Button submitButton;
    ImageButton imagePicker;
    String name, description, imagePath;
    Note.Importance importance;
    Date date = new Date();
    Note note = new Note();
    Mode mode;
    private static int REQUEST_CODE_PICKER = 100;
    public static final int EDIT = 101;
    public static final int ADD = 102;

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, i);
        calendar.set(Calendar.MINUTE, i1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        timeView.setText(formatter.format(date));

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateView.setText(formatter.format(date));
    }

    public enum Mode {
        EDIT, ADD
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);
        this.nameView = (TextView) findViewById(R.id.name);
        this.dateView = (TextView) findViewById(R.id.date);
        this.timeView = (TextView) findViewById(R.id.time);
        this.descriptionView = (TextView) findViewById(R.id.description);
        this.impotance = (SeekBar) findViewById(R.id.importance);
        this.submitButton = (Button) findViewById(R.id.submit);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isEdit", false)) {
            this.mode = Mode.EDIT;
            this.note = RealmDB.getNoteById(intent.getLongExtra("id", 0));
            this.nameView.setText(this.note.getName());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.date = note.getDate() ;
            try {
                 this.date = formatter.parse(this.date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            dateView.setText(formatter.format(this.date));
            formatter = new SimpleDateFormat("HH:mm");
            timeView.setText(formatter.format(this.date));
            descriptionView.setText(note.getDescription());
            impotance.setProgress(note.getImportance().ordinal());
            this.submitButton.setText(getApplicationContext().getResources().getText(R.string.edit_note));

        } else {
            note.setupId();
            mode = Mode.ADD;
            this.submitButton.setText(getApplicationContext().getResources().getText(R.string.add_button_text));
        }

        this.impotance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("CHANGE", String.valueOf(i));
                note.setImportance(EditAddActivity.this.getImportance(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.timeView.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(EditAddActivity.this, EditAddActivity.this, 10, 10, true);

            dialog.show();
        });
        this.dateView.setOnClickListener(view -> {
            Dialog dialog = new DatePickerDialog(EditAddActivity.this, EditAddActivity.this, 2017, 9, 30 );
            dialog.show();
        });
        this.imagePicker = (ImageButton) findViewById(R.id.image_picker);
        this.imagePicker.setOnClickListener(view -> ImagePicker.create(EditAddActivity.this)
                .returnAfterFirst(true)
                .folderMode(true)
                .folderTitle("Folder")
                .imageTitle("Tap to select")
                .single()
                .imageDirectory("Camera")
                .start(REQUEST_CODE_PICKER));
        if(!"".equals(note.getPathToImage().toString())) {
            Bitmap mainImage = BitmapFactory.decodeFile(this.note.getPathToImage());
            if (mainImage!=null) {
                EditAddActivity.this.imagePicker.setImageBitmap(mainImage);
            }
        }
        this.submitButton.setOnClickListener(view -> {
            this.note.setDescription(EditAddActivity.this.descriptionView.getText().toString());
            this.note.setName(EditAddActivity.this.nameView.getText().toString());
            this.note.setDate(date);

            if (EditAddActivity.this.mode == Mode.ADD) {
                note.setCreationalTime(new Date());
                RealmDB.addNote(this.note);
            } else {
                RealmDB.updateNote(this.note);
            }
            setResult(RESULT_OK, intent);
            finish();
        });

    }

    private Note.Importance getImportance(int value){
        switch (value)
        {
            case 0:
                return EditAddActivity.this.importance = Note.Importance.NO_MATTER;
            case 1:
                return EditAddActivity.this.importance = Note.Importance.IMPORTANT;
            case 2:
                return EditAddActivity.this.importance = Note.Importance.MOST_IMPORTANT;
        }
        return Note.Importance.NO_MATTER;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            this.note.setPathToImage(images.get(0).getPath());
            Bitmap mainImage = BitmapFactory.decodeFile(this.note.getPathToImage());
            if (mainImage!=null) {
                EditAddActivity.this.imagePicker.setImageBitmap(mainImage);
            }
        }
    }
}
