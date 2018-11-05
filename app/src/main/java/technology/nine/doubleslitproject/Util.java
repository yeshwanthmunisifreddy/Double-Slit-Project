package technology.nine.doubleslitproject;


import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
public  class Util {
   public static void encodeAndSaveFile(Context context, Bitmap photo, String fileName) {
        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);
            File directory = contextWrapper.getDir("DoubleSlit", Context.MODE_PRIVATE);
            File myPath = new File(directory, fileName);
            KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
            Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
            if (!crypto.isAvailable()) {
                return;
            }
            if (!myPath.exists()) {
                OutputStream fileStream = new BufferedOutputStream(
                        new FileOutputStream(myPath));
                OutputStream outputStream = crypto.getMacOutputStream(
                        fileStream,
                        Entity.create("entity_id"));
                outputStream.write(bitmapToBytes(photo));
                outputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static byte[] bitmapToBytes(Bitmap photo) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private static Bitmap bytesToBitmap(byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

   public static Bitmap decodeFile(Context context, String filename) {
        KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("DoubleSlit", Context.MODE_PRIVATE);
        File file = new File(directory, filename);
        Uri uri = null;
        Bitmap bitmap = null;
        try {
            if (file.exists()) {
              uri = Uri.fromFile(file);
                Log.e("Uri",uri+"");
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStream inputStream = crypto.getMacInputStream(fileInputStream, Entity.create("entity_id"));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int read;
                byte[] buffer = new byte[1024];
                while ((read = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, read);
                }
                fileInputStream.close();
                bitmap = bytesToBitmap(byteArrayOutputStream.toByteArray());
                Log.e("Bitmap", bitmap + "");
                String name = getFileName(context,uri);
                Log.e("Name",name+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);

            }
        }
        return result;
    }

    public static Uri getFileUri(Context context,String fileName){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("DoubleSlit", Context.MODE_PRIVATE);
        File file = new File(directory, fileName);
        Uri uri = null;
        if (file.exists()) {
            uri = Uri.fromFile(file);
        }
        return  uri;
    }
}
