package com.myst.kingdomheartsclock.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.myst.kingdomheartsclock.utils.DialogUtils;
import com.myst.kingdomheartsclock.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by minh on 7/12/17.
 */

public class ImagePickerHelper {

    public static final int REQ_CODE_UPLOAD_IMAGE_SELECT_FILE = 1003;
    public static final int REQ_CODE_UPLOAD_IMAGE_SELECT_CAMERA = 1004;
    public static final int REQ_CODE_UPLOAD_IMAGE_CROP_PIC = 1005;

    private Fragment fragment;
    private Activity activity;
    private AlertDialog selectImageDialog;
    private PermissionHelper permissionHelper;
    private OnSelectImageListener onSelectImageListener;
    private File mFilePhotoTaken;
    private int aspectX = -1, aspectY = -1;

    public ImagePickerHelper(Activity activity) {
        this.activity = activity;
    }

    public ImagePickerHelper(Activity activity, int aspectX, int aspectY) {
        this.activity = activity;
        setRatio(aspectX, aspectY);
    }

    public ImagePickerHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    public ImagePickerHelper(Fragment fragment, int aspectX, int aspectY) {
        this.fragment = fragment;
        setRatio(aspectX, aspectY);
    }

    @NonNull
    private PermissionHelper.OnPermissionListener onPermissionListener = new PermissionHelper.OnPermissionListener() {
        @Override
        public void onGranted(@NonNull PermissionHelper.PemissionType type) {
            Intent intent;
            switch (type) {
                case CAMERA:
                    try {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            mFilePhotoTaken = File.createTempFile("IMG_", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.getUriFromFile(getActivity(), mFilePhotoTaken));
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivityForResult(intent, REQ_CODE_UPLOAD_IMAGE_SELECT_CAMERA);
                        }
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage());
                    }
                    break;
                case GALLERY:
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQ_CODE_UPLOAD_IMAGE_SELECT_FILE);
                    break;
            }
            if (selectImageDialog != null && selectImageDialog.isShowing()) {
                selectImageDialog.dismiss();
            }
        }

        @Override
        public void onDenied() {
        }
    };

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_UPLOAD_IMAGE_SELECT_FILE:
                    crop(data.getData());
                    break;
                case REQ_CODE_UPLOAD_IMAGE_SELECT_CAMERA:
                    crop(Uri.fromFile(mFilePhotoTaken));
                    break;
            }
        }
    }

    private void crop(Uri sourceUri) {
        if (sourceUri != null) {
            try {
                mFilePhotoTaken = File.createTempFile("IMG_", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    public void setRatio(int aspectX, int aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }

    public void selectImage(OnSelectImageListener onSelectImageListener) {
        this.onSelectImageListener = onSelectImageListener;
        selectImage();
    }

    public void selectImage() {
        if (permissionHelper == null) {
            if (fragment != null) {
                permissionHelper = new PermissionHelper(fragment);
            } else {
                permissionHelper = new PermissionHelper(activity);
            }
            permissionHelper.setOnPermissionListener(onPermissionListener);
        }
        if (selectImageDialog == null) {
            selectImageDialog = DialogUtils.alertDialog(getActivity(), "Select Image", "Do you want take new image or select from album?", "Open Album", "Open Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (permissionHelper != null) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                permissionHelper.checkPermission(PermissionHelper.PemissionType.GALLERY);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                permissionHelper.checkPermission(PermissionHelper.PemissionType.CAMERA);
                                break;
                        }
                    }
                }
            });
        }
        selectImageDialog.show();
    }

    private Activity getActivity() {
        return fragment != null ? fragment.getActivity() : activity;
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            getActivity().startActivityForResult(intent, requestCode);
        }
        getActivity().overridePendingTransition(0, 0);
    }

    public interface OnSelectImageListener {
        void onCompleted(File file);
    }
}
