package com.stadium.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import com.stadium.app.Const;
import com.stadium.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Shamyyoun on 11/4/2015.
 * A utility class for resizing, downscaling, fixing orientation and saving bitmaps.
 */
public class BitmapUtils {

    /**
     * creates a file with the specified name in the external storage directory.
     *
     * @param ctx      the context
     * @param fileName the file name to create
     * @return the file if created, or null otherwise
     */
    public static File createImagePath(Context ctx, String fileName) {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Utils.showLongToast(ctx, R.string.unplug_mobile_cable_from_computer);
            return null;
        }

        File directoryPath = new File(Environment.getExternalStorageDirectory().getPath() + Const.APP_FILES_DIR + "/images");

        if (!directoryPath.exists()) {
            if (!directoryPath.mkdirs()) {
                return null;
            }
        }

        File filePath = new File(directoryPath + String.format("/%s.jpg", fileName));
        return filePath;
    }

    /**
     * Deletes all images in the images directory.
     */
    public static void cleanImagesDirectory() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File imagesDirectory = new File(Environment.getExternalStorageDirectory().getPath() + Const.APP_FILES_DIR + "/images");

        if (imagesDirectory.exists() && imagesDirectory.isDirectory()) {
            for (File file : imagesDirectory.listFiles()) {
                if (!file.isDirectory()) file.delete();
            }
        }
    }

    /**
     * Resize the bitmap which located in the specified path and respecting its aspect ratio, rotate it by Exif rotation and save it in place.
     *
     * @param bitmapFilePath
     * @param width
     * @param height
     * @return the resized bitmap if successfully resized or null.
     */
    public static Bitmap resizeBitmap(String bitmapFilePath, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapFilePath);

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;

        if (!(originalWidth < width && originalHeight < height)) { //don't scale up the bitmap
            if (originalHeight > originalWidth) {
                newHeight = height;
                multFactor = (float) originalWidth / (float) originalHeight;
                newWidth = (int) (newHeight * multFactor);
            } else if (originalWidth > originalHeight) {
                newWidth = width;
                multFactor = (float) originalHeight / (float) originalWidth;
                newHeight = (int) (newWidth * multFactor);
            } else if (originalHeight == originalWidth) {
                newHeight = height;
                newWidth = width;
            }
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            bitmap.recycle();
            bitmap = resizedBitmap;
        }


        //resizing is before rotation for not consuming large space when rotating

        //handling exif rotation
        //http://stackoverflow.com/a/4105966
        //for more: https://gist.github.com/9re/1990019
        try {
            ExifInterface exifData = new ExifInterface(bitmapFilePath);
            int orientation = exifData.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Utils.logE("Orientation: " + orientation);

            Matrix matrix = new Matrix();
            Bitmap rotatedBitmap = null;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
            }

            if (rotatedBitmap != null) { //bitmap has been rotated
                bitmap.recycle();
                bitmap = rotatedBitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        saveBitmap(bitmap, bitmapFilePath, 80);

        return bitmap;
    }


    /**
     * Resize the bitmap which located in the specified originalUri and respecting its aspect ratio, rotate it by Exif rotation and save it in  a new path with the specified newFileName.
     *
     * @param originalUri
     * @param newFileName the name of the resized bitmap file
     * @param width
     * @param height
     * @return the resized bitmap File path or null.
     */
    public static File resizeBitmap(Context ctx, Uri originalUri, String newFileName, int width, int height) {

        InputStream inputStream;
        Bitmap bitmap;
        try {
            inputStream = ctx.getContentResolver().openInputStream(originalUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String originalPath = FileUtils.getPath(ctx, originalUri); //the actual image path
        Utils.logE("path: ====== " + originalPath);
        if (originalPath == null)
            return null;


        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;

        if (!(originalWidth < width && originalHeight < height)) { //don't scale up the bitmap
            if (originalHeight > originalWidth) {
                newHeight = height;
                multFactor = (float) originalWidth / (float) originalHeight;
                newWidth = (int) (newHeight * multFactor);
            } else if (originalWidth > originalHeight) {
                newWidth = width;
                multFactor = (float) originalHeight / (float) originalWidth;
                newHeight = (int) (newWidth * multFactor);
            } else if (originalHeight == originalWidth) {
                newHeight = height;
                newWidth = width;
            }
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            bitmap.recycle();
            bitmap = resizedBitmap;
        }


        //resizing is before rotation for not consuming large space when rotating

        //handling exif rotation
        //http://stackoverflow.com/a/4105966
        //for more: https://gist.github.com/9re/1990019
        try {
            ExifInterface exifData = new ExifInterface(originalPath);
            int orientation = exifData.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Utils.logE("Orientation: " + orientation);

            Matrix matrix = new Matrix();
            Bitmap rotatedBitmap = null;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    break;
            }

            if (rotatedBitmap != null) { //bitmap has been rotated
                bitmap.recycle();
                bitmap = rotatedBitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        File newPath = createImagePath(ctx, newFileName);
        if (newPath != null)
            saveBitmap(bitmap, newPath.getAbsolutePath(), 80);

        return newPath;
    }

    /**
     * Saves the bitmap to the specified path with the specified quality.
     *
     * @param bitmapFilePath
     * @param bitmap
     * @param quality
     * @return True if successfully saved.
     */
    public static boolean saveBitmap(Bitmap bitmap, String bitmapFilePath, int quality) {
        File imageFile = new File(bitmapFilePath);

        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            Boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            return compressed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}