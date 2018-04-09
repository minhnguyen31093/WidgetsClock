package com.myst.kingdomheartsclock.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.myst.kingdomheartsclock.service.BaseAsyncTask;
import com.myst.kingdomheartsclock.ui.ProgressDialog;
import com.myst.kingdomheartsclock.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Minh Nguyen on 7/5/2016.
 */
public class CacheImageHelper {

    private Context context;
    private ProgressDialog cacheProgressDialog;
    @Nullable
    private ArrayList<String> images;
    private ArrayList<String> cacheImages;
    private int currentProgress = 0;
    private OnCacheImageListener onCacheImageListener;

    public CacheImageHelper(@NonNull Context context) {
        this.context = context;
        cacheProgressDialog = new ProgressDialog(context);
        cacheProgressDialog.setCancelable(false);
    }

    public void createCacheImages(@Nullable ArrayList<String> images) {
        this.images = images;
        this.currentProgress = 0;
        this.cacheImages = new ArrayList<>();
        if (images != null && images.size() > 0) {
            cacheProgressDialog.show();
            CacheImageTask cacheImageTask = new CacheImageTask(context);
            cacheImageTask.start(images.get(currentProgress));
        }
    }

    public void setOnCacheImageListener(OnCacheImageListener onCacheImageListener) {
        this.onCacheImageListener = onCacheImageListener;
    }

    private class CacheImageTask extends BaseAsyncTask<String, Void, File> {

        public CacheImageTask(Context context) {
            super(context);
        }

        @Nullable
        @Override
        protected File run(String... params) throws Exception {
            return ImageUtils.resizeImage(context, new File(params[0]));
        }

        @Override
        protected void onSuccess(@Nullable File result) {
            if (result != null) {
                cacheImages.add(result.getPath());
                currentProgress++;
                if (images != null && currentProgress < images.size()) {
                    CacheImageTask cacheImageTask = new CacheImageTask(context);
                    cacheImageTask.start(images.get(currentProgress));
                } else if (images == null || currentProgress == images.size()) {
                    if (onCacheImageListener != null) {
                        onCacheImageListener.onComplete(cacheImages);
                    }
                    cacheProgressDialog.dismiss();
                }
            }
        }

        @Override
        protected void onFail(Exception exception) {
            super.onFail(exception);
            if (onCacheImageListener != null) {
                onCacheImageListener.onFail();
            }
            cacheProgressDialog.dismiss();
        }
    }

    public interface OnCacheImageListener {
        void onComplete(ArrayList<String> images);

        void onFail();
    }
}
