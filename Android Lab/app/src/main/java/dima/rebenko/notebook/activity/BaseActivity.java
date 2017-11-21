package dima.rebenko.notebook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dima.rebenko.notebook.Helpers.Util;
import io.realm.Realm;

/**
 * Created by a1 on 11/21/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Util.onActivityCreateSetTheme(this);
        Realm.init(this);
    }
}
