package com.mit.mylib.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MSDCardUtils {

    private MSDCardUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean isFileExistsInSDCard(String filePath, String fileName) {
        boolean flag = false;
        if(checkSDCardAvailable()) {
            File file = new File(filePath, fileName);
            if(file.exists()) {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean saveFileToSDCard(String filePath, String filename, String content) throws Exception {
        boolean flag = false;
        if(checkSDCardAvailable()) {
            File dir = new File(filePath);
            if(!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(filePath, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
            flag = true;
        }

        return flag;
    }

    public static byte[] readFileFromSDCard(String filePath, String fileName) {
        byte[] buffer = null;

        try {
            if(checkSDCardAvailable()) {
                String filePaht = filePath + "/" + fileName;
                FileInputStream fin = new FileInputStream(filePaht);
                int length = fin.available();
                buffer = new byte[length];
                fin.read(buffer);
                fin.close();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return buffer;
    }

    public static boolean deleteSDFile(String filePath, String fileName) {
        File file = new File(filePath + "/" + fileName);
        return file.exists() && !file.isDirectory()?file.delete():false;
    }
}
