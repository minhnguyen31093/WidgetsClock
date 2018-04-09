package com.myst.kingdomheartsclock.service;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.myst.kingdomheartsclock.ui.ProgressDialog;

/**
 * Created by Minh. Nguyen Le on 9/28/2015.
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected Context context;
    protected Exception exception;

    private ProgressDialog progressDialog;
    protected boolean isCompleted;

    public BaseAsyncTask(Context context) {
        this.context = context;
    }

    @SafeVarargs
    public final AsyncTask<Params, Progress, Result> start(Params... params) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return execute(params);
        } else {
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }
    }

    @SafeVarargs
    public final AsyncTask<Params, Progress, Result> startWithCheckNetwork(Params... params) {
        return start(params);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        isCompleted = false;
        if (progressDialog != null) {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isCompleted) {
                        progressDialog.show();
                    }
                }
            }, 200);
        }
    }

    @SafeVarargs
    @Override
    protected final Result doInBackground(Params... params) {
        try {
            return run(params);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        isCompleted = true;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (exception == null) {
            onSuccess(result);
        } else {
            onFail(exception);
        }
    }

    protected void onFail(Exception exception) {

    }

    @Nullable
    protected abstract Result run(Params... params) throws Exception;

    protected abstract void onSuccess(Result result);
}
