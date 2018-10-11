package com.quangtd.qmazes.util;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.quangtd.qmazes.R;

import org.jetbrains.annotations.Nullable;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


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
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.show();
    }


    public static void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void showError(Context context, String message) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setTitle(message).show();
    }

    public static void showUnlock(Context context /*, PrettyDialogCallback unlockAllCallback*/) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setTitle(context.getString(R.string.lock_level_warning));
        //            unlockNowCallback.onClick();
        prettyDialog.addButton(context.getString(R.string.OK), R.color.pdlg_color_black, R.color.pdlg_color_green, prettyDialog::dismiss);
//        prettyDialog.addButton("Cancel", R.color.pdlg_color_black, R.color.pdlg_color_yellow, prettyDialog::dismiss);
        prettyDialog.setCancelable(true);
        prettyDialog.setIcon(R.drawable.btn_locked).setIconTint(R.color.pdlg_color_yellow)
                .setIconCallback(prettyDialog::dismiss).show();
    }

    public static void showTimeUp(Context context, PrettyDialogCallback prettyDialogCallback) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(false);
        prettyDialog.setTitle(context.getString(R.string.time_up));
        prettyDialog.addButton(context.getString(R.string.try_again), R.color.pdlg_color_black, R.color.pdlg_color_yellow, () -> {
            prettyDialog.dismiss();
            prettyDialogCallback.onClick();
        });
        prettyDialog.setIcon(R.drawable.ic_hourglass).setIconTint(R.color.pdlg_color_yellow)
                .setIconCallback(() -> {
                    prettyDialogCallback.onClick();
                    prettyDialog.dismiss();
                }).show();
    }

    public static void showReload(Context context, PrettyDialogCallback onClickReload, PrettyDialogCallback onClickCancel) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setTitle(context.getString(R.string.reload)).setIcon(R.drawable.circular_arrow)
                .addButton(context.getString(R.string.OK), R.color.pdlg_color_black, R.color.pdlg_color_green, () -> {
                    onClickReload.onClick();
                    prettyDialog.dismiss();
                })
                .addButton(context.getString(R.string.cancel), R.color.pdlg_color_black, R.color.pdlg_color_white, () -> {
                    onClickCancel.onClick();
                    prettyDialog.dismiss();
                })
                .show();
    }

    public static void showExitConfirm(Context context, PrettyDialogCallback onClickExit, PrettyDialogCallback onClickCancel) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setIcon(R.drawable.ic_close)
                .setTitle(context.getString(R.string.exit_game_confirm))
                .addButton(context.getString(R.string.OK), R.color.pdlg_color_black, R.color.pdlg_color_red, () -> {
                    prettyDialog.dismiss();
                    onClickExit.onClick();
                })
                .addButton(context.getString(R.string.cancel), R.color.pdlg_color_black, R.color.pdlg_color_white, () -> {
                    onClickCancel.onClick();
                    prettyDialog.dismiss();
                })
                .show();
    }

    public static void showWin(Context context, String title, PrettyDialogCallback onClickNext) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setTitle(title).setIcon(R.drawable.ic_win)
                .addButton(context.getString(R.string.next), R.color.pdlg_color_black, R.color.pdlg_color_green, () -> {
                    prettyDialog.dismiss();
                    onClickNext.onClick();
                })
                .show();
    }

    public static void showUnlockComplete(Context context, PrettyDialogCallback onClickNext) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setTitle(context.getString(R.string.unlock_new_level)).setIcon(R.drawable.btn_locked)
                .addButton(context.getString(R.string.play_now), R.color.pdlg_color_black, R.color.pdlg_color_green, () -> {
                    prettyDialog.dismiss();
                    onClickNext.onClick();
                })
                .show();
    }

    public static void showNotify(Context context, String message, PrettyDialogCallback oNClickOkCallback) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setTitle(message).setIcon(R.mipmap.logo)
                .addButton(context.getString(R.string.OK), R.color.pdlg_color_black, R.color.pdlg_color_yellow, () -> {
                    prettyDialog.dismiss();
                    oNClickOkCallback.onClick();
                })
                .show();
    }

    public static void showUnlockCategory(@Nullable Context context) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prettyDialog.setCancelable(true);
        prettyDialog.setTitle(context.getString(R.string.unlock_new_category)).setIcon(R.drawable.btn_locked)
                .addButton(context.getString(R.string.btn_ok), R.color.pdlg_color_black, R.color.pdlg_color_green, prettyDialog::dismiss)
                .show();
    }
}
