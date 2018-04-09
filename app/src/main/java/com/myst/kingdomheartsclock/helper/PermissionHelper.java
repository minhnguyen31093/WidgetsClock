package com.myst.kingdomheartsclock.helper;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.myst.kingdomheartsclock.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by minh on 7/14/16.
 */
public class PermissionHelper {

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static final String IS_FIRST_TIME_REQUEST_PERMISSION_CAMERA = "IS_FIRST_TIME_REQUEST_PERMISSION_CAMERA";
    public static final String IS_FIRST_TIME_REQUEST_PERMISSION_GALLERY = "IS_FIRST_TIME_REQUEST_PERMISSION_GALLERY";
    public static final String IS_FIRST_TIME_REQUEST_PERMISSION_READ_CONTACTS = "IS_FIRST_TIME_REQUEST_PERMISSION_READ_CONTACTS";
    public static final String IS_FIRST_TIME_REQUEST_PERMISSION_LOCATION = "IS_FIRST_TIME_REQUEST_PERMISSION_LOCATION";
    public static final String IS_FIRST_TIME_REQUEST_PERMISSION_CALL_PHONE = "IS_FIRST_TIME_REQUEST_PERMISSION_CALL_PHONE";
    private Activity activity;
    private Fragment fragment;
    private OnPermissionListener onPermissionListener;
    private PemissionType currentype;
    private String reason;
    private SharedPreferences sharedPreferences;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
    }

    @NonNull
    private Permissions getPermissionsByType(@NonNull PemissionType type) {
        Permissions permissions = new Permissions();
        List<String> permissionsNeeded = new ArrayList<>();
        List<String> permissionsList = new ArrayList<>();
        switch (type) {
            case CAMERA:
                if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                    permissionsNeeded.add("Camera");
                }
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissionsNeeded.add("Write External Storage");
                }
                if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    permissionsNeeded.add("Read External Storage");
                }
                break;
            case GALLERY:
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissionsNeeded.add("Write External Storage");
                }
                if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    permissionsNeeded.add("Read External Storage");
                }
                break;
            case READ_CONTACTS:
                if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS)) {
                    permissionsNeeded.add("Contacts");
                }
                break;
            case LOCATION:
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    permissionsNeeded.add("Location");
                }
                break;
            case CALL_PHONE:
                if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE)) {
                    permissionsNeeded.add("Phone");
                }
                break;
        }
        permissions.setPermissionsNeeded(permissionsNeeded);
        permissions.setPermissionsList(permissionsList);
        return permissions;
    }

    private boolean addPermission(@NonNull List<String> permissionsList, @NonNull String permission) {
        Activity activity = getActivity();
        if (activity != null) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    @NonNull
    private List<String> getPermissionsStringByType(@Nullable PemissionType type) {
        List<String> permissions = new ArrayList<>();
        if (type != null) {
            switch (type) {
                case CAMERA:
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    break;
                case GALLERY:
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    break;
                case READ_CONTACTS:
                    permissions.add(Manifest.permission.READ_CONTACTS);
                    break;
                case LOCATION:
                    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    break;
                case CALL_PHONE:
                    permissions.add(Manifest.permission.CALL_PHONE);
                    break;
            }
        }
        return permissions;
    }

    private String getPreNameByType(PemissionType type) {
        switch (type) {
            case CAMERA:
                return IS_FIRST_TIME_REQUEST_PERMISSION_CAMERA;
            case GALLERY:
                return IS_FIRST_TIME_REQUEST_PERMISSION_GALLERY;
            case READ_CONTACTS:
                return IS_FIRST_TIME_REQUEST_PERMISSION_READ_CONTACTS;
            case LOCATION:
                return IS_FIRST_TIME_REQUEST_PERMISSION_LOCATION;
            case CALL_PHONE:
                return IS_FIRST_TIME_REQUEST_PERMISSION_CALL_PHONE;
        }
        return null;
    }

    public void checkPermission(@NonNull PemissionType type) {
        currentype = type;
        String preName = getPreNameByType(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permissions permissions = getPermissionsByType(type);
            if (permissions.getPermissionsList() != null && permissions.getPermissionsList().size() > 0) {
                final List<String> permissionsList = permissions.getPermissionsList();
                if (permissions.getPermissionsNeeded() != null && permissions.getPermissionsNeeded().size() > 0) {
                    final List<String> permissionsNeeded = permissions.getPermissionsNeeded();
                    // Need Rationale
                    if (!sharedPreferences.getBoolean(preName, false)) {
                        sharedPreferences.edit().putBoolean(preName, true).apply();
                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
                    } else {
                        StringBuilder message;
                        message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
                        int size = permissionsNeeded.size();
                        for (int i = 1; i < size; i++) {
                            message.append(", ").append(permissionsNeeded.get(i));
                        }
                        showMessageOKCancel(message.toString(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_CODE_ASK_PERMISSIONS);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                    default:
                                        if (onPermissionListener != null) {
                                            onPermissionListener.onDenied();
                                        }
                                        break;
                                }
                            }
                        });
                    }
                } else {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
                }
            } else {
                if (onPermissionListener != null) {
                    onPermissionListener.onGranted(currentype);
                }
            }
        } else {
            if (onPermissionListener != null) {
                onPermissionListener.onGranted(currentype);
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        DialogUtils.alert(getActivity(), message, getString(android.R.string.ok), getString(android.R.string.cancel), okListener);
    }

    private Activity getActivity() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getActivity();
        }
        return null;
    }

    private String getString(int id) {
        if (activity != null) {
            return activity.getString(id);
        } else if (fragment != null) {
            return fragment.getString(id);
        } else {
            return "";
        }
    }

    private String getPackageName() {
        Activity activity = getActivity();
        if (activity != null) {
            return activity.getPackageName();
        } else {
            return "";
        }
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    private void requestPermissions(@NonNull String[] permissions, int requestCode) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else if (fragment != null) {
            fragment.requestPermissions(permissions, requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                List<String> permissionsString = getPermissionsStringByType(currentype);
                int size = permissionsString.size();
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                for (int i = 0; i < size; i++) {
                    perms.put(permissionsString.get(i), PackageManager.PERMISSION_GRANTED);
                }

                // Fill with results
                int len = permissions.length;
                for (int i = 0; i < len; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                for (int i = 0; i < size; i++) {
                    if (perms.get(permissionsString.get(i)) != PackageManager.PERMISSION_GRANTED) {
                        // Permission Denied
                        if (onPermissionListener != null) {
                            onPermissionListener.onDenied();
                        }
                        return;
                    }
                }
                // All Permissions Granted
                if (onPermissionListener != null) {
                    onPermissionListener.onGranted(currentype);
                }
            }
            break;
        }
    }

    public void setOnPermissionListener(OnPermissionListener onPermissionListener) {
        this.onPermissionListener = onPermissionListener;
    }

    private class Permissions {
        private List<String> permissionsNeeded;
        private List<String> permissionsList;

        public List<String> getPermissionsNeeded() {
            return permissionsNeeded;
        }

        public void setPermissionsNeeded(List<String> permissionsNeeded) {
            this.permissionsNeeded = permissionsNeeded;
        }

        public List<String> getPermissionsList() {
            return permissionsList;
        }

        public void setPermissionsList(List<String> permissionsList) {
            this.permissionsList = permissionsList;
        }
    }

    public enum PemissionType {
        CAMERA(0), GALLERY(1), READ_CONTACTS(2), LOCATION(3), CALL_PHONE(4);
        private int value;

        PemissionType(int value) {

        }

        public int getValue() {
            return value;
        }

        @NonNull
        public static PemissionType fromValue(int value) {
            for (PemissionType c : PemissionType.values()) {
                if (c.value == value) {
                    return c;
                }
            }
            throw new IllegalArgumentException("Invalid PemissionType value: " + value);
        }
    }

    public interface OnPermissionListener {
        void onGranted(PemissionType currenType);

        void onDenied();
    }
}
