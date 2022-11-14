package services;

import java.io.*;

public class Copy {
    public static void copy(String srcPath, String dstPath) throws IOException {
        try {
            File file = new File(srcPath);
            if (file.isDirectory()) {
                Copy.copyDir(srcPath, dstPath);

            } else {
                File f = new File(dstPath);
                if (!f.exists()) {
                    f.createNewFile();
                }
                Copy.copyFile(srcPath, dstPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void copyDir(String srcPath, String destPath) throws IOException {
        File src = new File(srcPath);//源头
        File dest = new File(destPath);//目的地
        //判断是否为目录，不存在则不作操作
        if (!src.isDirectory()) {
            return;
        }
        //判断目的目录是否存在，不存在就创建目录
        if (!dest.exists()) {
            Utils.mkdir(dest);
        }
        //获取源头目录下的文件列表组成数组，每个对象代表一个目录或者文件
        File[] srcList = src.listFiles();
        if (null != srcList && srcList.length > 0) {
            //遍历源头目录下的文件列表
            for (File aSrcList : srcList) {
                //如果是目录的话
                if (aSrcList.isDirectory()) {
                    //递归调用遍历该目录
                    copyDir(srcPath + File.separator + aSrcList.getName(), destPath + File.separator + aSrcList.getName());
                } else if (aSrcList.isFile()) {
                    copyFile(srcPath + File.separator + aSrcList.getName(), destPath + File.separator + aSrcList.getName());
                }
            }
        }
    }


    private static void copyFile(String isFile, String osFile) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(isFile);
            os = new FileOutputStream(osFile);
            byte[] data = new byte[4096];//缓存容器
            int len;//接收长度
            while ((len = is.read(data)) != -1) {
                os.write(data, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //	释放资源 分别关闭 先打开的后关闭
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}