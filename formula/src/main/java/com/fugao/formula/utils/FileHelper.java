package com.fugao.formula.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 文件操作相关
 * Created by huxq on 2017/5/23 0023.
 */

public class FileHelper {
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

}
