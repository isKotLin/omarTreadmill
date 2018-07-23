package com.vigorchip.omatreadmill.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class DimenTool {
    static DecimalFormat decimalFormat;
    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimen.xml");
        BufferedReader reader = null;
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw768 = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
//                    sw480.append(start).append(num * 0.8).append(end).append("\r\n");
//                    sw600.append(start).append(num * 1).append(end).append("\r\n");
//                    sw768.append(start).append(num * 1.28).append(end).append("\r\n");
                    sw480.append(start).append(decimalFormat.format(num * 0.75)).append(end).append("\r\n");
                    sw600.append(start).append(decimalFormat.format(num * 0.75)).append(end).append("\r\n");
                    sw768.append(start).append(decimalFormat.format(num * 1.75)).append(end).append("\r\n");
                } else {
                    sw480.append(tempString).append("");
                    sw600.append(tempString).append("");
                    sw768.append(tempString).append("");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);
            System.out.println("<!--  sw768 -->");
            System.out.println(sw768);
            String sw480file = "./app/src/main/res/values-sw480dp-land/dimen.xml";
            String sw600file = "./app/src/main/res/values-sw600dp-land/dimen.xml";
            String sw768file = "./app/src/main/res/values-sw768dp-land/dimen.xml";
            //将新的内容，写入到指定的文件中去
            writeFile(sw480file, sw480.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw768file, sw768.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * 写入方法
     */
    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }
    public static void main(String[] args) {
        decimalFormat=new DecimalFormat("0.0");
        gen();
    }
}