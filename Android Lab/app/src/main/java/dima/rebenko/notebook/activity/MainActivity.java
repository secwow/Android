package dima.rebenko.notebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import dima.rebenko.notebook.Helpers.Util;
import dima.rebenko.notebook.R;
import dima.rebenko.notebook.adapters.MyNotesRecyclerViewAdapter;
import dima.rebenko.notebook.model.Note;
import dima.rebenko.notebook.Helpers.RealmDB;
import io.realm.Realm;


public class MainActivity extends BaseActivity implements MyNotesRecyclerViewAdapter.onRecyclerViewClickListener  {

    MyNotesRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.recyclerView = (RecyclerView) findViewById(R.id.list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
        @Override
        public void OnClick(View view, Note note) {

        }

        @Override
        public void onLongClick(View view, Note note) {
            final String[] mCatsName ={"Edit", "Delete"};
            AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose action");
            builder.setItems(mCatsName, (dialog, item) -> {
                if (item==1){
                    RealmDB.deleteItem(note);
                    adapter.updateData(RealmDB.getAllNotes());
                    adapter.notifyDataSetChanged();
                } else {
                    Intent edit = new Intent(getApplicationContext(), EditAddActivity.class);
                    edit.putExtra("isEdit", true);
                    edit.putExtra("id", note.getId());
                    startActivityForResult(edit, EditAddActivity.EDIT);
                }
            });
            builder.create();
            builder.show();
        }


    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.search_bar, null);
        actionBar.setCustomView(mCustomView, new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        actionBar.setDisplayShowCustomEnabled(true);
        Button add = (Button)actionBar.getCustomView().findViewById(R.id.add_button);

        add.setOnClickListener(view -> {
            Intent edit = new Intent(getApplicationContext(), EditAddActivity.class);
            startActivityForResult(edit, EditAddActivity.ADD);
        });

        TextView search = (TextView) actionBar.getCustomView().findViewById(R.id.searchField);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.updateData(RealmDB.searchNotesByDescription(charSequence.toString()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ImageButton filterButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.filter_button);
        filterButton.setOnClickListener(view -> {
            if(!"".equals(search.getText().toString())) {
                adapter.updateData(RealmDB.filterNotesByDescription(search.getText().toString()));
                adapter.notifyDataSetChanged();
            } else {
                final String[] mCatsName = {"Not important", "Important", "Most important"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose action");
                builder.setItems(mCatsName, (dialog, item) -> {
                    Log.d("Item", String.valueOf(item));
                    if (item == 2) {
                        adapter.updateData(RealmDB.filterByImportance(Note.Importance.MOST_IMPORTANT));
                        adapter.notifyDataSetChanged();
                    }
                    if (item == 1) {
                        adapter.updateData(RealmDB.filterByImportance(Note.Importance.IMPORTANT));
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.updateData(RealmDB.filterByImportance(Note.Importance.NO_MATTER));
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        this.setupActionBar();
        this.adapter = new MyNotesRecyclerViewAdapter(RealmDB.getAllNotes(), this);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            adapter.notifyDataSetChanged();
        }
    }
}