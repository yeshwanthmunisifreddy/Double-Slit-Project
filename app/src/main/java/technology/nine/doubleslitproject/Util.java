package technology.nine.doubleslitproject;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


class Util {
    static void encodeAndSaveFile( Context context,Bitmap photo,String fileName) {

        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);
            File directory = contextWrapper.getDir("DoubleSlit", Context.MODE_PRIVATE);
            File myPath = new File(directory,fileName);
            KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
            Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
            if (!crypto.isAvailable()) {
                return;
            }
            if (!myPath.exists()){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
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
    private  static Bitmap bytesToBitmap(byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    static  Bitmap decodeFile(String filename, Context context){
        KeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("DoubleSlit", Context.MODE_PRIVATE);
        File file = new File(directory, filename);

        try {
            FileInputStream fileStream = new FileInputStream(file);

            InputStream inputStream = crypto.getCipherInputStream(
                    fileStream,
                    Entity.create("entity_id"));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            byte[] buffer = new byte[1024];

            while((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            inputStream.close();
            Bitmap bitmap = bytesToBitmap(out.toByteArray());
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
