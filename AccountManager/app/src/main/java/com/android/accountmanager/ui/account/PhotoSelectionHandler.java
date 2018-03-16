package com.android.accountmanager.ui.account;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoSelectionHandler implements View.OnClickListener {
    private static final String TAG = "PhotoSelectionHandler";
    private static final String PHOTO_DATE_FORMAT = "'IMG'_yyyyMMdd_HHmmss";
    private static final int REQUEST_CODE_CAMERA_WITH_DATA = 1001;
    private static final int REQUEST_CODE_PHOTO_PICKED_WITH_DATA = 1002;
    private static final int REQUEST_CROP_PHOTO = 1003;

    private final FragmentActivity mActivity;
    private final Uri mTempPhotoUri;
    private final Uri mCroppedPhotoUri;
    private final int mPhotoSize;

    public PhotoSelectionHandler(FragmentActivity activity) {
        mActivity = activity;
        mTempPhotoUri = generateTempImageUri(activity);
        mCroppedPhotoUri = generateTempCroppedImageUri(activity);
        mPhotoSize = activity.getResources().getDimensionPixelSize(R.dimen.account_photo_size);
    }

    @Override
    public void onClick(View view) {

    }

    public void startPickFromGalleryActivity() {
        final Intent intent = getPhotoPickIntent(mTempPhotoUri);
        mActivity.startActivityForResult(intent, REQUEST_CODE_PHOTO_PICKED_WITH_DATA);
    }

    public void startTakePhotoActivity() {
        final Intent intent = getTakePhotoIntent(mTempPhotoUri);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA_WITH_DATA);
    }

    public boolean handlePhotoActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final Uri uri;
            switch (requestCode) {
                case REQUEST_CROP_PHOTO:
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = mCroppedPhotoUri;
                    }
                    mActivity.getContentResolver().delete(mTempPhotoUri, null, null);
                    if (mActivity instanceof AccountContract.AccountView) {
                        return ((AccountContract.AccountView) mActivity).onPhotoSelected(uri);
                    }
                    return false;
                case REQUEST_CODE_PHOTO_PICKED_WITH_DATA:
                case REQUEST_CODE_CAMERA_WITH_DATA:
                    boolean isWritable = false;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        isWritable = true;
                    } else {
                        uri = mTempPhotoUri;
                        isWritable = true;
                    }
                    final Uri toCrop;
                    if (isWritable) {
                        toCrop = uri;
                    } else {
                        toCrop = mTempPhotoUri;
                        try {
                            if (!savePhotoFromUriToUri(mActivity, uri,
                                    toCrop, false)) {
                                return false;
                            }
                        } catch (SecurityException e) {
                            Log.d(TAG, "Did not have read-access to uri : " + uri);
                            return false;
                        }
                    }

                    doCropPhoto(toCrop, mCroppedPhotoUri);
                    return true;
            }
        }
        return false;
    }

    private Intent getTakePhotoIntent(Uri outputUri) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        addPhotoPickerExtras(intent, outputUri);
        return intent;
    }

    private Intent getPhotoPickIntent(Uri outputUri) {
        final Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        addPhotoPickerExtras(intent, outputUri);
        return intent;
    }

    private Intent getCropImageIntent(Uri inputUri, Uri outputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        addPhotoPickerExtras(intent, outputUri);
        addCropExtras(intent, mPhotoSize);
        return intent;
    }

    public static Uri generateTempCroppedImageUri(Context context) {
        final String fileProviderAuthority = context.getResources().getString(
                R.string.photo_file_provider_authority);
        return FileProvider.getUriForFile(context, fileProviderAuthority,
                new File(pathForTempPhoto(context, generateTempCroppedPhotoFileName())));
    }

    private static void addPhotoPickerExtras(Intent intent, Uri photoUri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));
    }

    private static void addCropExtras(Intent intent, int photoSize) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", photoSize);
        intent.putExtra("outputY", photoSize);
    }

    public static Uri generateTempImageUri(Context context) {
        final String fileProviderAuthority = context.getResources().getString(
                R.string.photo_file_provider_authority);
        return FileProvider.getUriForFile(context, fileProviderAuthority,
                new File(pathForTempPhoto(context, generateTempPhotoFileName())));
    }

    private static String pathForTempPhoto(Context context, String fileName) {
        final File dir = context.getCacheDir();
        dir.mkdirs();
        final File f = new File(dir, fileName);
        return f.getAbsolutePath();
    }

    private static String generateTempPhotoFileName() {
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(PHOTO_DATE_FORMAT, Locale.US);
        return "AccountPhoto-" + dateFormat.format(date) + ".jpg";
    }

    private static String generateTempCroppedPhotoFileName() {
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(PHOTO_DATE_FORMAT, Locale.US);
        return "AccountPhoto-" + dateFormat.format(date) + "-cropped.jpg";
    }

    private boolean hasIntentHandler(Intent intent) {
        final List<ResolveInfo> resolveInfo = mActivity.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null && resolveInfo.size() > 0;
    }

    public static boolean savePhotoFromUriToUri(Context context, Uri inputUri, Uri outputUri,
                                                boolean deleteAfterSave) {
        if (inputUri == null || outputUri == null) {
            return false;
        }
        try (FileOutputStream outputStream = context.getContentResolver()
                .openAssetFileDescriptor(outputUri, "rw").createOutputStream();
             InputStream inputStream = context.getContentResolver().openInputStream(inputUri)) {

            final byte[] buffer = new byte[16 * 1024];
            int length;
            int totalLength = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                totalLength += length;
            }
            Log.v(TAG, "Wrote " + totalLength + " bytes for photo " + inputUri.toString());
        } catch (IOException | NullPointerException e) {
            Log.e(TAG, "Failed to write photo: " + inputUri.toString() + " because: " + e);
            return false;
        } finally {
            if (deleteAfterSave) {
                context.getContentResolver().delete(inputUri, null, null);
            }
        }
        return true;
    }

    private void doCropPhoto(Uri inputUri, Uri outputUri) {
        final Intent intent = getCropImageIntent(inputUri, outputUri);
        try {
            mActivity.startActivityForResult(intent, REQUEST_CROP_PHOTO);
        } catch (Exception e) {
            Log.e(TAG, "Cannot crop image", e);
            Toast.makeText(mActivity, R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
        if (uri == null) {
            Log.v(TAG, "uri is null,can't use it");
            return null;
        }
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        try {
            return BitmapFactory.decodeStream(imageStream);
        } finally {
            Closeables.closeQuietly(imageStream);
        }
    }

    public static Drawable getDrawableFromUri(Context context, Uri uri) throws FileNotFoundException {
        if (uri == null) {
            Log.v(TAG, "uri is null,can't use it");
            return null;
        }
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            drawable.setTargetDensity(context.getResources().getDisplayMetrics());
            return drawable;
        } finally {
            Closeables.closeQuietly(imageStream);
        }
    }

    public static Drawable getRoundDrawableFromUri(Context context, Uri uri) throws FileNotFoundException {
        if (uri == null) {
            Log.v(TAG, "uri is null,can't use it");
            return null;
        }
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        try {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageStream);
            drawable.setTargetDensity(context.getResources().getDisplayMetrics());
            drawable.setCircular(true);
            return drawable;
        } finally {
            Closeables.closeQuietly(imageStream);
        }
    }
}
