package com.fugao.formula.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文件操作相关
 * Created by huxq on 2017/5/23 0023.
 */

public class FileHelper {
    public static String SDCARD_PATH = SDCardUtils.getSDPath();
    public static String appSDPath = SDCARD_PATH + "/" + "fugaoapps" + "/formula";
    /**
     * 得到assets 文件中的内容
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getFromAssets(String fileName, Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getApplicationContext().getResources().getAssets()
                    .open(fileName));

            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 创建sd卡的目录
     *
     * @param dir
     * @return
     */
    public static File creatSDDir(String dir) {
        File appSDPathFile = new File(appSDPath);
        if (!appSDPathFile.exists()) {
            appSDPathFile.mkdirs();
        }
        File destFileDir = new File(appSDPathFile + "/" + dir);
        if (!destFileDir.exists()) {
            destFileDir.mkdirs();
        }

        return destFileDir;

    }

    /**
     * 把string 保存在文件中
     *
     * @param filename
     *            文件名称
     * @param path
     *            文件路径
     * @param content
     *            要写的内容
     * @throws IOException
     */
    public static void saveStringFile(String path, String filename,
                                       String content) {
        File filePath = new File(appSDPath + "/" + path);

        if (!filePath.exists()) {
            creatSDDir(path);
        }
        File fileAbsolutely = new File(filePath + "/" + filename);
        if (!fileAbsolutely.exists()) {
            try {
                fileAbsolutely.createNewFile();

                FileOutputStream fos = new FileOutputStream(fileAbsolutely);

                fos.write(content.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            fileAbsolutely.delete();
            try {
                fileAbsolutely.createNewFile();

                FileOutputStream fos = new FileOutputStream(fileAbsolutely);

                fos.write(content.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
