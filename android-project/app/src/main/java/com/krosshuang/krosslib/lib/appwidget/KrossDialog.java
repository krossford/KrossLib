package com.krosshuang.krosslib.lib.appwidget;

import android.app.Dialog;
import android.content.Context;

/*
<style name="customer_dialog_style" parent="@android:style/Theme.Dialog">
<item name="android:windowBackground">@null</item>
<item name="android:windowNoTitle">true</item>
</style>
*/

/**
 *
 *
 * Created by krosshuang on 2016/1/30.
 */
public abstract class KrossDialog extends Dialog {

    public KrossDialog(Context context) {
        super(context);
    }
}
