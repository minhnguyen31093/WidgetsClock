package com.myst.kingdomheartsclock.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.myst.kingdomheartsclock.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ImageUtils {
    private static int maxWidth = 480;
    private static int maxHeight = 480;

    private static String checkUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        if (url.contains("http:")) {
            url = url.replace("http:", "https:");
        }
        return url;
    }

    public static Uri getImageUri(@NonNull Context inContext, @NonNull Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(@NonNull Context context, @NonNull Uri contentUri) {
        Cursor cursor;
        String[] projection = {MediaStore.Images.Media.DATA};
        cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String picturePath = cursor.getString(column_index);
        cursor.close();
        //Log.e("Edit profile","picture path : "+picturePath);
        return picturePath;
    }

    @NonNull
    public static File getFileFromStorage() {
        File f = new File(Environment.getExternalStorageDirectory().toString());
        for (File temp : f.listFiles()) {
            if (temp.getName().equals("temp.jpg")) {
                f = temp;
                break;
            }
        }
        return f;
    }

    @Nullable
    public static File resizeImage(Context context, @Nullable File image) {
        if (image != null) {
            Bitmap bm = resize(image.getPath());
            bm = rotate(bm, image.getPath());
            if (bm != null) {
                File resized = saveImage(context, bm, image.getName());
                if (resized != null) {
                    return resized;
                }
            }
        }
        return image;
    }

    private static Bitmap rotate(Bitmap bp, String path) {
        if (bp != null && path != null) {
            Matrix matrix = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
            } catch (IOException e) {
                Log.e("Rotate error", e.getMessage());
            }
            return Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
        }
        return bp;
    }

    private static Bitmap resize(String path) {
        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //just decode the file
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;
        //initialization of the scale
        int resizeScale = 1;
        //get the good scale
        if (orignalWidth > maxWidth || orignalHeight > maxHeight) {
            final int heightRatio = Math.round((float) orignalHeight / (float) maxHeight);
            final int widthRatio = Math.round((float) orignalWidth / (float) maxWidth);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;
        //get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;
        //check if it's possible to store into the vm java the picture
        if (Runtime.getRuntime().freeMemory() > bmSize) {
            //decode the file
            bp = BitmapFactory.decodeFile(path, opts);
        } else {
            return null;
        }
        return bp;
    }

    public static Bitmap resize(Bitmap bp, int maxWidth) {
        int height = bp.getHeight();
        int width = bp.getWidth();
        if (width > maxWidth) {
            height = height * maxWidth / width;
            width = maxWidth;
        }
        return Bitmap.createScaledBitmap(bp, width, height, true);
    }

    public static File saveImage(Context context, @NonNull Bitmap image, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("cacheImage", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "WC" + name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return mypath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File saveImage(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("cacheImage", Context.MODE_PRIVATE);
        File newFile = new File(directory, "temp_image.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(newFile);
            fos.close();
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteDirectory(@NonNull File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        deleteDirectory(file1);
                    } else {
                        file1.delete();
                    }
                }
            }
            file.delete();
        }
    }

    public static Bitmap captureScreenAndIgnore(ViewGroup parent, int id) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        parent.setDrawingCacheEnabled(true);
        Bitmap newBitmap = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(newBitmap);
        int childcount = parent.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View v = parent.getChildAt(i);
            if (v.getId() != id && v.getWidth() > 0 && v.getHeight() > 0) {
                v.setDrawingCacheEnabled(true);
                v.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                v.draw(new Canvas(b));
                int iX = v.getLeft();
                int iY = v.getTop();

                if (b != null) {
                    newCanvas.save();
                    newCanvas.drawBitmap(b, iX, iY, paint);
                    newCanvas.restore();
                    b.recycle();
                }
                v.setDrawingCacheEnabled(false);
            }
        }
        return newBitmap;
    }

    public static void rotateImage(Context context, Uri imageUri) {
        rotateImage(context, imageUri, getRealPathFromURI(context, imageUri));
    }

    public static void rotateImage(Context context, Uri imageUri, String file) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);

        int rotationAngle = getCameraPhotoOrientation(context, imageUri, file);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        FileChannel destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public static Uri getUriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }
}
