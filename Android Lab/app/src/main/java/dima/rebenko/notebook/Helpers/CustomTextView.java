package dima.rebenko.notebook.Helpers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by a1 on 11/21/17.
 */

public class CustomTextView{

    public static TextView setupTextView(TextView view)
    {
        view.setTextSize(ThemeApp.currentFontSize);
        return view;
    }
}
