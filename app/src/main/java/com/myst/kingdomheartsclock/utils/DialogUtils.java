package com.myst.kingdomheartsclock.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * Created by Minh. Nguyen Le on 9/26/2015.
 */
public class DialogUtils {

    public static AlertDialog createAlertDialog(Context context, String title, String message, String positiveString, String negativeString, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.isEmpty()) {
            builder.setTitle(title);
        }
        if (message != null && !message.isEmpty()) {
            builder.setMessage(message);
        }
        if (positiveString != null && !positiveString.isEmpty()) {
            builder.setPositiveButton(positiveString, onClickListener);
        }
        if (negativeString != null && !negativeString.isEmpty()) {
            builder.setNegativeButton(negativeString, onClickListener);
        }
        return builder.create();
    }

    public static void alert(Context context, String message, String positiveString, String negativeString, DialogInterface.OnClickListener onClickListener) {
        createAlertDialog(context, "", message, positiveString, negativeString, onClickListener).show();
    }

    public static void alert(Context context, String message, String positiveString, DialogInterface.OnClickListener onClickListener) {
        createAlertDialog(context, "", message, positiveString, "", onClickListener).show();
    }

    private static void alert(Context context, String message, String positiveString) {
        createAlertDialog(context, "", message, positiveString, "", null).show();
    }

    public static void alert(Context context, int titleId, int messageId, int positiveStringId, int negativeStringId, DialogInterface.OnClickListener onClickListener) {
        createAlertDialog(context, context.getString(titleId), context.getString(messageId), context.getString(positiveStringId), context.getString(negativeStringId), onClickListener).show();
    }

    public static AlertDialog alertDialog(Context context, int titleId, int messageId, int positiveStringId, int negativeStringId, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, context.getString(titleId), context.getString(messageId), context.getString(positiveStringId), context.getString(negativeStringId), onClickListener);
    }

    public static AlertDialog alertDialog(Context context, String titleId, String messageId, String positiveStringId, String negativeStringId, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, titleId, messageId, positiveStringId, negativeStringId, onClickListener);
    }

    public static void alert(Context context, int messageId, int positiveStringId, int negativeStringId, DialogInterface.OnClickListener onClickListener) {
        createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), context.getString(negativeStringId), onClickListener).show();
    }

    public static AlertDialog alertDialog(Context context, int messageId, int positiveStringId, int negativeStringId, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), context.getString(negativeStringId), onClickListener);
    }

    public static void alert(Context context, int messageId, int positiveStringId, DialogInterface.OnClickListener onClickListener) {
        createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), "", onClickListener).show();
    }

    public static void alertForce(Context context, int messageId, int positiveStringId, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), "", onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static AlertDialog alertDialog(Context context, int messageId, int positiveStringId, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), "", onClickListener);
    }

    public static AlertDialog alertDialog(Context context, String message, String positiveString, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, "", message, positiveString, "", onClickListener);
    }

    public static void alert(Context context, int messageId, int positiveStringId) {
        createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), "", null).show();
    }

    public static AlertDialog alertDialog(Context context, int messageId, int positiveStringId) {
        return createAlertDialog(context, "", context.getString(messageId), context.getString(positiveStringId), "", null);
    }

    public static void alert(Context context, View view, int positiveStringId, int negativeStringId, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setView(view).setPositiveButton(positiveStringId, onClickListener).setNegativeButton(negativeStringId, onClickListener).create().show();
    }

    public static void alert(Context context, int titleId, View view) {
        new AlertDialog.Builder(context).setTitle(titleId).setView(view).create().show();
    }

    public static void alert(Context context, int titleId, ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setTitle(titleId).setSingleChoiceItems(adapter, checkedItem, onClickListener).create().show();
    }

    public static void alertYesNo(Context context, int messageId, DialogInterface.OnClickListener onClickListener) {
        alert(context, messageId, android.R.string.yes, android.R.string.no, onClickListener);
    }

    public static void alertInfo(Context context, String message) {
        alert(context, message, context.getString(android.R.string.ok));
    }

    public static void alertInfo(Context context, int message) {
        alertInfo(context, context.getString(message));
    }

    public static void alertInfoForce(Context context, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = alertDialog(context, message, android.R.string.ok, onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static AlertDialog alertDialogInfoForce(Context context, int message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = alertDialog(context, message, android.R.string.ok, onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static void alertError(Context context, int messageId) {
        alert(context, messageId, android.R.string.cancel);
    }

    public static void alertErrorForce(Context context, int messageId) {
        AlertDialog alertDialog = alertDialog(context, messageId, android.R.string.cancel);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void alertErrorForce(Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = alertDialog(context, msg, context.getString(android.R.string.cancel), onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void alertErrorForce(Context context, int messageId, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = alertDialog(context, messageId, android.R.string.cancel, onClickListener);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void alertError(Context context, int messageId, DialogInterface.OnClickListener onClickListener) {
        alert(context, messageId, android.R.string.cancel, onClickListener);
    }

    public static void alertError(Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        alert(context, msg, context.getString(android.R.string.cancel), onClickListener);
    }

    public static void alertError(Context context, String msg) {
        alert(context, msg, context.getString(android.R.string.cancel));
    }

    public static void toast(Context context, int id) {
        toast(context, context.getString(id));
    }

    public static void toast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }
}