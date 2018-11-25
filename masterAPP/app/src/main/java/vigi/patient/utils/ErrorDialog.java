package vigi.patient.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import vigi.patient.R;

public class ErrorDialog {


    public void showDialog(final Activity activity, String msg){

            final Dialog dialog = new Dialog(activity);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.utils_error_dialog);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }

            // Create objects

            TextView msgError = dialog.findViewById(R.id.utilsErrorDialog_message);

            // Set text

            msgError.setText(msg);

            // Set on click listener on try again button

            Button dialogButton = dialog.findViewById(R.id.utilsErrorDialog_okBtn);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

