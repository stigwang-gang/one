import services.Copy;
import services.Verify;
import services.Zip;

public class test {
    public static void main(String[] args) throws Exception {
        Copy.copy("C:\\Users\\92351\\Desktop\\test.txt","C:\\Users\\92351\\Desktop\\测试");
        Zip.compress("C:\\Users\\92351\\Desktop\\test","C:\\Users\\92351\\Desktop\\Output.zip");
        Zip.decompress("C:\\Users\\92351\\Desktop\\Output.zip","C:\\Users\\92351\\Desktop\\解压文件夹");
        Verify.verify("C:\\Users\\92351\\Desktop\\备份","C:\\Users\\92351\\Desktop\\测试");
    }
}
