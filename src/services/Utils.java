package services;

import java.io.File;

public class Utils {
    /**
     * 新建指定路径文件夹
     * @param file
     */
    public static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());    // 递归新建所有路径
        file.mkdir();
    }

    public static boolean isPathValid(String path){
        File file = new File(path);
        return file.exists();
    }
}
