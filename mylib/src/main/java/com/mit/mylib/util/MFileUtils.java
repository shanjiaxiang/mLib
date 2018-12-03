package com.mit.mylib.util;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MFileUtils {
    public static final long GB = 1073741824L;
    public static final long MB = 1048576L;
    public static final long KB = 1024L;
    public static final int ICON_TYPE_ROOT = 1;
    public static final int ICON_TYPE_FOLDER = 2;
    public static final int ICON_TYPE_MP3 = 3;
    public static final int ICON_TYPE_MTV = 4;
    public static final int ICON_TYPE_JPG = 5;
    public static final int ICON_TYPE_FILE = 6;
    public static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    public static final String MP3_REG = "^.*\\.(mp3|wav)$";
    public static final String JPG_REG = "^.*\\.(gif|jpg|png)$";
    private static final String FILENAME_REGIX = "^[^\\/?\"*:<>\\\u0005]{1,255}$";
    public static final String FILE_EXTENSION_SEPARATOR = "";

    private MFileUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static boolean DeleteFile(File file) {
        if(file != null && file.exists()) {
            if(file.isFile()) {
                return file.delete();
            } else if(!file.isDirectory()) {
                return false;
            } else {
                File[] childFile = file.listFiles();
                if(childFile != null && childFile.length != 0) {
                    File[] var2 = childFile;
                    int var3 = childFile.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        File f = var2[var4];
                        DeleteFile(f);
                    }

                    return file.delete();
                } else {
                    return file.delete();
                }
            }
        } else {
            return false;
        }
    }

    public static boolean renameFile(File file, String newFileName) {
        if(newFileName.matches("^[^\\/?\"*:<>\\\u0005]{1,255}$")) {
            File newFile = null;
            if(file.isDirectory()) {
                newFile = new File(file.getParentFile(), newFileName);
            } else {
                String temp = newFileName + file.getName().substring(file.getName().lastIndexOf(46));
                newFile = new File(file.getParentFile(), temp);
            }

            if(file.renameTo(newFile)) {
                return true;
            }
        }

        return false;
    }

    public static String getFileSize(File file) {
        FileInputStream fis = null;

        String var3;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if((long)length < 1073741824L) {
                if((long)length >= 1048576L) {
                    var3 = String.format("%.2f MB", new Object[]{Double.valueOf((double)length * 1.0D / 1048576.0D)});
                    return var3;
                }

                var3 = String.format("%.2f KB", new Object[]{Double.valueOf((double)length * 1.0D / 1024.0D)});
                return var3;
            }

            var3 = String.format("%.2f GB", new Object[]{Double.valueOf((double)length * 1.0D / 1.073741824E9D)});
        } catch (Exception var15) {
            var15.printStackTrace();
            return "未知";
        } finally {
            try {
                fis.close();
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return var3;
    }

    public static void openFile(Activity activity, File file) throws Exception {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), getMimeType(file, activity));
        activity.startActivity(intent);
    }

    public static String getMimeType(File file, Activity activity) throws Exception {
        String name = file.getName().substring(file.getName().lastIndexOf(46) + 1).toLowerCase();
        int id = activity.getResources().getIdentifier(activity.getPackageName() + ":string/" + name, (String)null, (String)null);
        if("class".equals(name)) {
            return "application/octet-stream";
        } else if("3gp".equals(name)) {
            return "video/3gpp";
        } else if("nokia-op-logo".equals(name)) {
            return "image/vnd.nok-oplogo-color";
        } else if(id == 0) {
            throw new Exception("未找到分享该格式的应用");
        } else {
            return activity.getString(id);
        }
    }

    public static List<HashMap<String, Object>> recursionFolder(File folder, FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList();
        File[] files = folder.listFiles();
        if(filter != null) {
            files = folder.listFiles(filter);
        }

        if(files != null) {
            for(int m = 0; m < files.length; ++m) {
                File file = files[m];
                if(file.isDirectory()) {
                    list.addAll(recursionFolder(file, filter));
                } else {
                    HashMap<String, Object> map = new HashMap();
                    map.put("file", file);
                    if(file.getAbsolutePath().toLowerCase().matches("^.*\\.(mp3|wav)$")) {
                        map.put("iconType", Integer.valueOf(3));
                    } else if(file.getAbsolutePath().toLowerCase().matches("^.*\\.(mp4|3gp)$")) {
                        map.put("iconType", Integer.valueOf(4));
                    } else if(file.getAbsolutePath().toLowerCase().matches("^.*\\.(gif|jpg|png)$")) {
                        map.put("iconType", Integer.valueOf(5));
                    } else {
                        map.put("iconType", Integer.valueOf(6));
                    }

                    list.add(map);
                }
            }
        }

        return list;
    }

    public static List<HashMap<String, Object>> unrecursionFolder(File folder, FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList();
        if(!folder.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            HashMap<String, Object> map = new HashMap();
            map.put("file", folder.getParentFile());
            map.put("iconType", Integer.valueOf(1));
            list.add(map);
        }

        File[] files = folder.listFiles();
        if(filter != null) {
            files = folder.listFiles(filter);
        }

        if(files != null && files.length > 0) {
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File p = var4[var6];
                HashMap<String, Object> map = new HashMap();
                map.put("file", p);
                if(p.isDirectory()) {
                    map.put("iconType", Integer.valueOf(2));
                } else if(p.getAbsolutePath().toLowerCase().matches("^.*\\.(mp3|wav)$")) {
                    map.put("iconType", Integer.valueOf(3));
                } else if(p.getAbsolutePath().toLowerCase().matches("^.*\\.(mp4|3gp)$")) {
                    map.put("iconType", Integer.valueOf(4));
                } else if(p.getAbsolutePath().toLowerCase().matches("^.*\\.(gif|jpg|png)$")) {
                    map.put("iconType", Integer.valueOf(5));
                } else {
                    map.put("iconType", Integer.valueOf(6));
                }

                list.add(map);
            }
        }

        return list;
    }

    public static FileFilter getFileFilter(final String reg, boolean isdir) {
        return isdir?new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().toLowerCase().matches(reg) || pathname.isDirectory();
            }
        }:new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().toLowerCase().matches(reg) && pathname.isFile();
            }
        };
    }

    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if(!file.isFile()) {
            return null;
        } else {
            BufferedReader reader = null;

            StringBuilder var7;
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);

                String line;
                for(reader = new BufferedReader(is); (line = reader.readLine()) != null; fileContent.append(line)) {
                    if(!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                }

                reader.close();
                var7 = fileContent;
            } catch (IOException var16) {
                throw new RuntimeException("IOException occurred. ", var16);
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var15) {
                        throw new RuntimeException("IOException occurred. ", var15);
                    }
                }

            }

            return var7;
        }
    }

    public static void writeFile(InputStream in, File file) throws IOException {
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if(file != null && file.exists()) {
            file.delete();
        }

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[131072];
        boolean var4 = true;

        int len;
        while((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        out.flush();
        out.close();
        in.close();
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if(TextUtils.isEmpty(content)) {
            return false;
        } else {
            FileWriter fileWriter = null;

            boolean var4;
            try {
                makeDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                fileWriter.write(content);
                fileWriter.close();
                var4 = true;
            } catch (IOException var13) {
                throw new RuntimeException("IOException occurred. ", var13);
            } finally {
                if(fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException var12) {
                        throw new RuntimeException("IOException occurred. ", var12);
                    }
                }

            }

            return var4;
        }
    }

    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if(contentList != null && contentList.size() >= 1) {
            FileWriter fileWriter = null;

            boolean var16;
            try {
                makeDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                int i = 0;

                String line;
                for(Iterator var5 = contentList.iterator(); var5.hasNext(); fileWriter.write(line)) {
                    line = (String)var5.next();
                    if(i++ > 0) {
                        fileWriter.write("\r\n");
                    }
                }

                fileWriter.close();
                var16 = true;
            } catch (IOException var14) {
                throw new RuntimeException("IOException occurred. ", var14);
            } finally {
                if(fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException var13) {
                        throw new RuntimeException("IOException occurred. ", var13);
                    }
                }

            }

            return var16;
        } else {
            return false;
        }
    }

    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null?new File(filePath):null, stream, append);
    }

    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        FileOutputStream o = null;

        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte[] data = new byte[1024];
            boolean var5 = true;

            int length;
            while((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }

            o.flush();
            boolean var6 = true;
            return var6;
        } catch (FileNotFoundException var16) {
            throw new RuntimeException("FileNotFoundException occurred. ", var16);
        } catch (IOException var17) {
            throw new RuntimeException("IOException occurred. ", var17);
        } finally {
            if(o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException var15) {
                    throw new RuntimeException("IOException occurred. ", var15);
                }
            }

        }
    }

    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException var4) {
            throw new RuntimeException("FileNotFoundException occurred. ", var4);
        }

        return writeFile((String)destFilePath, (InputStream)inputStream);
    }

    public static final byte[] input2byte(InputStream inStream) {
        if(inStream == null) {
            return null;
        } else {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            boolean var3 = false;

            int rc;
            try {
                while((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return swapStream.toByteArray();
        }
    }

    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList <>();
        if(!file.isFile()) {
            return null;
        } else {
            BufferedReader reader = null;

            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
                reader = new BufferedReader(is);
                String line = null;

                while((line = reader.readLine()) != null) {
                    fileContent.add(line);
                }

                reader.close();
                ArrayList var7 = (ArrayList) fileContent;
                return var7;
            } catch (IOException var16) {
                throw new RuntimeException("IOException occurred. ", var16);
            } finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var15) {
                        throw new RuntimeException("IOException occurred. ", var15);
                    }
                }

            }
        }
    }

    public static String getFileNameWithoutExtension(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf("");
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1?(extenPosi == -1?filePath:filePath.substring(0, extenPosi))
                    :(extenPosi == -1?filePath.substring(filePosi + 1):(filePosi < extenPosi?filePath.substring(filePosi + 1, extenPosi):filePath.substring(filePosi + 1)));
        }
    }

    public static String getFileName(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1?filePath:filePath.substring(filePosi + 1);
        }
    }

    public static String getFolderName(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1?"":filePath.substring(0, filePosi);
        }
    }

    public static String getFileExtension(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf("");
            int filePosi = filePath.lastIndexOf(File.separator);
            return extenPosi == -1?"":(filePosi >= extenPosi?"":filePath.substring(extenPosi + 1));
        }
    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if(TextUtils.isEmpty(folderName)) {
            return false;
        } else {
            File folder = new File(folderName);
            return folder.exists() && folder.isDirectory() || folder.mkdirs();
        }
    }

    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    public static boolean isFileExist(String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }

    public static boolean isFolderExist(String directoryPath) {
        if(TextUtils.isEmpty(directoryPath)) {
            return false;
        } else {
            File dire = new File(directoryPath);
            return dire.exists() && dire.isDirectory();
        }
    }

    public static boolean deleteFile(String path) {
        if(TextUtils.isEmpty(path)) {
            return true;
        } else {
            File file = new File(path);
            if(!file.exists()) {
                return true;
            } else if(file.isFile()) {
                return file.delete();
            } else if(!file.isDirectory()) {
                return false;
            } else {
                File[] var2 = file.listFiles();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File f = var2[var4];
                    if(f.isFile()) {
                        f.delete();
                    } else if(f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }

                return file.delete();
            }
        }
    }

    public static long getFileSize(String path) {
        if(TextUtils.isEmpty(path)) {
            return -1L;
        } else {
            File file = new File(path);
            return file.exists() && file.isFile()?file.length():-1L;
        }
    }

    public static File uri2File(Activity activity, Uri uri) {
        String[] projection;
        if(Build.VERSION.SDK_INT < 11) {
            projection = new String[]{"_data"};
            Cursor actualimagecursor = activity.managedQuery(uri, projection, (String)null, (String[])null, (String)null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow("_data");
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            return new File(img_path);
        } else {
            projection = new String[]{"_data"};
            CursorLoader loader = new CursorLoader(activity, uri, projection, (String)null, (String[])null, (String)null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }
}
