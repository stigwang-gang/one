package services;

import java.io.*;

public class Encrypt {
    public static void encrypt(String srcPath, String dstPath, int key) throws IOException {
        try {
            File file = new File(srcPath);
            if (file.isDirectory()) {
                Encrypt.recurDir(srcPath, dstPath, key, false);
            } else {
                File f = new File(dstPath);
                if (!f.exists()) {
                    f.createNewFile();
                }
                Encrypt.modifyFileBytes(srcPath, dstPath, key, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String srcPath, String dstPath, int key) throws IOException {
        try {
            File file = new File(srcPath);
            if (file.isDirectory()) {
                Encrypt.recurDir(srcPath, dstPath, key, true);
            } else {
                File f = new File(dstPath);
                if (!f.exists()) {
                    f.createNewFile();
                }
                Encrypt.modifyFileBytes(srcPath, dstPath, key, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recurDir(String srcPath, String destPath, int key, boolean restore) throws IOException {
        File srcDir = new File(srcPath);//源头
        File destDir = new File(destPath);//目的地
        //判断是否为目录，不是则不操作
        if (!srcDir.isDirectory()) {
            return;
        }
        //判断目的目录是否存在，不存在就创建目录
        if (!destDir.exists()) {
            Utils.mkdir(destDir);
        }
        //遍历源目录下的文件列表
        File[] srcList = srcDir.listFiles();
        if (null != srcList && srcList.length > 0) {
            for (File aSrcList : srcList) {
                //如果是目录的话递归
                if (aSrcList.isDirectory()) {
                    recurDir(srcPath + File.separator + aSrcList.getName(), destPath + File.separator + aSrcList.getName(), key, restore);
                } else if (aSrcList.isFile()) {
                    modifyFileBytes(srcPath + File.separator + aSrcList.getName(), destPath + File.separator + aSrcList.getName(), key, restore);
                }
            }
        }
    }

    private static void modifyFileBytes(String isFile, String osFile, int key, boolean restore) throws IOException {
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(isFile));
            os = new BufferedOutputStream(new FileOutputStream(osFile));
            byte[] data = new byte[4096];//缓存容器

            int len;//接收长度

            while ((len = is.read(data)) != -1) {
                for (int i = 0; i < 4096; i++) {
                    // 【关键】添加/删除无意义字节
                    if (restore) {
                        data[i] -= (byte) key;
                    } else {
                        data[i] += (byte) key;
                    }

                }
                os.write(data, 0, len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //释放资源
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