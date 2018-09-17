package com.quangtd.qmazes.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.quangtd.qmazes.R;
import com.quangtd.qmazes.util.ScreenUtils;

import java.util.Objects;


public class DialogUtils {
    private static Dialog dialog;

    private DialogUtils() {
        //no-op
    }

    public static void showLoadingDialog(Context context) {
        if (null == context) return;
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialog = null;
        }
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.show();
    }

    /**
     * dismiss loading dialog when call API done
     */
    public static void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void createMultiChoiceDialog(Context context, String title, String[] arrs, boolean[] bools, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMultiChoiceItems(arrs, bools, (dialog, which, isChecked) -> bools[which] = isChecked);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        builder.show();
    }

    public static void createSingleDialog(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        builder.show();
    }

    public static AlertDialog.Builder createSingleDialogBuilder(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener, DialogAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setTitle(title);
        builder.setPositiveButton("yes", (dialog, which) -> callback.onClickPositive());
        return builder;
    }

    public static AlertDialog.Builder createSingleDialogBuilder(Context context, String title, String[] arrs, int positionChosen, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(arrs, positionChosen, listener);
        builder.setTitle(title);
        return builder;
    }

    public static void createAlertDialog(Context context, String title, String message) {
        createAlertDialog(context, title, message, null);
    }

    public static void createAlertDialog(Context context, String title, String message, DialogAlertCallback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton("Ok", (dialog, which) -> {
            if (null != callback) {
                callback.onClickPositive();
            }
            dialog.dismiss();
        });
        alert.setOnDismissListener(dialog -> {
            if (null != callback) {
                callback.onClickPositive();
            }
            dialog.dismiss();
        });
        alert.show();
    }

    public static void createConfirmDialog(Context context, String title, String message, DialogConfirmCallback callback) {
        createConfirmDialog(context, title, message,
                "yes",
                "no",
                callback);
    }

    public static void createConfirmDialog(Context context, String title, String message, String textPositive, String textNegative, DialogConfirmCallback callback) {
        AlertDialog.Builder alert;
        if (TextUtils.isEmpty(title)) {
            alert = new AlertDialog.Builder(context, R.style.FullHeightDialog);
        } else {
            alert = new AlertDialog.Builder(context);
            alert.setTitle(title);
        }
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton(textPositive, (dialog, which) -> {
            if (null != callback) {
                callback.onClickPositiveButton();
            }
            dialog.dismiss();
        });
        alert.setNegativeButton(textNegative, (dialog, which) -> {
            if (null != callback) {
                callback.onClickNegativeButton();
            }
            dialog.dismiss();
        });
        alert.show();
    }

    public static void showInputDialog(Context context, String initInputText, DialogInputCallBack callBack) {
        if (null == context) return;
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialog = null;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_text);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP | Gravity.START;
        lp.x = 40;
        lp.y = ScreenUtils.getHeightScreen(context) / 5;
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);

        final EditText editText = dialog.findViewById(R.id.edtText);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnOK = dialog.findViewById(R.id.btnOK);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnOK.performClick();
                handled = true;
            }
            return handled;
        });

        editText.setText(initInputText);
        int pos = editText.getText().length();
        editText.setSelection(pos);
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnOK.setOnClickListener(v -> {
            String mText;
            mText = editText.getText().toString().trim();
            if (TextUtils.isEmpty(mText)) {
                mText = editText.getHint().toString().trim();
            }
            callBack.onClickOk(mText);
            dialog.dismiss();
        });
        dialog.show();
    }

    public interface DialogConfirmCallback {
        void onClickPositiveButton();

        void onClickNegativeButton();
    }

    public interface DialogAlertCallback {
        void onClickPositive();
    }

    public interface DialogInputCallBack {
        void onClickOk(String text);
    }
}