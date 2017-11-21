package dima.rebenko.notebook.Helpers;

import android.app.Activity;
import android.content.Intent;

import dima.rebenko.notebook.R;

/**
 * Created by a1 on 11/21/17.
 */

public class Util {
    private static int sTheme;

    public final static int THEME_GREEN = 0;
    public final static int THEME_PURPLE = 1;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_GREEN:
                activity.setTheme(R.style.Theme_GreenTheme);
                break;
            case THEME_PURPLE:
                activity.setTheme(R.style.Theme_PurpleTheme);
                break;
        }
    }
}

