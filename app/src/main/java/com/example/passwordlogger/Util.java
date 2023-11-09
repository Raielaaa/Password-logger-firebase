package com.example.passwordlogger;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

public class Util {
    private Dialog dialog;

    public void displayCustomDialog(
            Activity activity,
            Integer layoutDialog
    ) {
        try {
            dialog = new Dialog(activity);

            dialog.setContentView(layoutDialog);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.custom_dialog_background));
            }

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            dialog.show();
        } catch (Exception e) {
            Log.e("MyTag", "displayCustomDialog: " + e.getMessage());
            // Handle the exception, if necessary
        }
    }
    public void dismissDialog() {
        dialog.dismiss();
    }
}
