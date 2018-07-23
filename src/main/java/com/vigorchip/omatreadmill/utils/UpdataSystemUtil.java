package com.vigorchip.omatreadmill.utils;

import android.content.Context;
import android.os.RecoverySystem;
import android.widget.Toast;

import com.vigorchip.puliblib.utils.Logutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wr-app1 on 2018/1/31.
 */

public class UpdataSystemUtil {

    public static final String CHCHE_PARTITION = "/cache/";

    public static final String DEFAULT_PACKAGE_NAME = "update.zip";

    public static void updataSystem(Context context, File otaPackageFile) {
        try {
            boolean b = copyFile(otaPackageFile, new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME));
            Logutil.i("查看布尔值有没有：  " +b);
            if (b) {
                if (new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME).exists()) {
                    new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME).delete();
                    installPackage(context, new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME));
                } else {
                    installPackage(context, new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean copyFile(File f1, File f2) throws Exception {
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);

        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return true;
            } else
                out.write(buffer, 0, ins);
        }
    }

    public static void installPackage(Context context, File packageFile) {
        try {
            RecoverySystem.installPackage(context, packageFile);
            Toast.makeText(context,"下载完啦", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
