package com.stadium.player.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.stadium.player.R;

/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class DialogUtils {
    /**
     * method, used to show alert dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param onClickListener
     * @return
     */
    public static AlertDialog showAlertDialog(Context context, int messageResId, DialogInterface.OnClickListener onClickListener) {
        return showAlertDialog(context, context.getString(messageResId), onClickListener);
    }

    /**
     * method, used to alert dialog with passed message string
     *
     * @param context
     * @param message
     * @param onClickListener
     */
    public static AlertDialog showAlertDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);

        // check the click listener
        if (onClickListener != null) {
            // not null
            // add positive click listener
            dialogBuilder.setPositiveButton(context.getString(R.string.ok), onClickListener);
        } else {
            // null
            // add new click listener to dismiss the dialog
            dialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        // create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }

    /**
     * method, used to show confirm dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param positiveClickListener
     */
    public static AlertDialog showConfirmDialog(Context context, int messageResId, DialogInterface.OnClickListener positiveClickListener,
                                                DialogInterface.OnClickListener negativeClickListener) {

        return showConfirmDialog(context, context.getString(messageResId), positiveClickListener, negativeClickListener);
    }

    /**
     * method, used to show confirm dialog with passed message string
     *
     * @param context
     * @param message
     * @param positiveClickListener
     */
    public static AlertDialog showConfirmDialog(Context context, String message, DialogInterface.OnClickListener positiveClickListener,
                                                DialogInterface.OnClickListener negativeClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);

        // add positive click listener
        dialogBuilder.setPositiveButton(context.getString(R.string.yes), positiveClickListener);

        // check negative click listener
        if (negativeClickListener != null) {
            // not null
            // add negative click listener
            dialogBuilder.setNegativeButton(context.getString(R.string.no), negativeClickListener);
        } else {
            // null
            // add new click listener to dismiss the dialog
            dialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        // create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        return dialog;
    }

    /**
     * method, used to show progress dialog with passed message res id and not cancelable
     *
     * @param context
     * @param messageResId
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, int messageResId) {
        return showProgressDialog(context, messageResId, false);
    }

    /**
     * method, used to show progress dialog with passed message res id and not cancelable
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, message, false);
    }

    /**
     * method, used to show progress dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, int messageResId, boolean cancelable) {
        return showProgressDialog(context, context.getString(messageResId), cancelable);
    }

    /**
     * method, used to show progress dialog with passed message string
     *
     * @param context
     * @param message
     * @param cancelable
     * @return the dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }
}
