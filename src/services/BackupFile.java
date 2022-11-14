package services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
public class BackupFile extends TimerTask {
    String strPathSource,strPathTar;
    HashMap<String,String> hm = new HashMap<>();

    public BackupFile(String strPathSource,String strPathTar){
        this.strPathSource = strPathSource;
        this.strPathTar = strPathTar;
    }

    public void backupFile(String strPathSource,String strPathTar){
        if(!hm.keySet().contains(BackupFile.getFileMD5(strPathSource))){//如果文件内容不变，或者恢复到之前的内容，都不备份。
            hm.put(BackupFile.getFileMD5(strPathSource), setDateFormat(System.currentTimeMillis()));//把文件的MD5值存入集合hm的key中
            try {
                BufferedInputStream bin = new BufferedInputStream(new FileInputStream(strPathSource));
                BufferedOutputStream bou = new BufferedOutputStream(
                        new FileOutputStream(strPathTar + randomStr(10) + new File(strPathSource).getName())
                );//增加随机字符串，为的是防止文件名一样，备份失败
                int i = 0;
                while((i = bin.read())!=-1){
                    bou.write(i);
                }
                bin.close();
                bou.close();
            } catch (FileNotFoundException e) {
                System.out.println("文件没有找到~！");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "文件备份成功！");
        }else{
            System.out.println("文件曾经存在过，不给予备份~请注意！");
        }
    }

    //生成随机数字符串
    public String randomStr(int len){
        Random rd = new Random();
        String temp = "";
        for(int i=0;i<=len;i++){
            int num = rd.nextInt(len);
            temp += String.valueOf(num);
        }
        return temp;
    }

    //转化时间格式
    public static String setDateFormat(long time){
        Date dt = new Date(time);
        DateFormat df = new SimpleDateFormat("YYYY年MM月DD日 hh时MM分ss秒");
        return df.format(dt);
    }
    //生成文件的MD5值
    public static String getFileMD5(String strPathSource){
        return MD5Util.getFileMD5(new File(strPathSource));
    }

    public void run(){
        backupFile(strPathSource,strPathTar);
    }
}
