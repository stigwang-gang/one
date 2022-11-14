package views;

import services.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Timer;

public class MainWindow extends JFrame {
    private JLabel welcome;
    private JPanel mainPanel;
    private JButton copyBtn;
    private JButton zipBtn;
    private JButton unzipBtn;
    private JButton encryptBtn;
    private JButton decryptBtn;
    private JButton verifyBtn;
    private JTextArea srcDrop;
    private JTextArea dstDrop;
    private JTextField srcPath;
    private JTextField dstPath;
    private JButton backupBtn;

    Timer t = new Timer();

    public MainWindow() {
        this.setTitle("数据备份工具");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 窗口居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = this.getPreferredSize().width;
        int frameHeight = this.getPreferredSize().height;
        this.setSize(frameWidth, frameHeight);
        this.setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);

        // 图标
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/folder.png"));
        this.setIconImage(icon.getImage());

        // 使用系统自带的文件选择器外观
        if (UIManager.getLookAndFeel().isSupportedLookAndFeel()) {
            final String platform = UIManager.getSystemLookAndFeelClassName();
            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                try {
                    UIManager.setLookAndFeel(platform);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        setDroppable(srcDrop, srcPath, false);
        setDroppable(dstDrop, dstPath, true);

        verifyBtn.addActionListener(e -> {
            try {
                onVerify();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        copyBtn.addActionListener(e -> {
            try {
                onCopy();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        backupBtn.addActionListener(e -> {
            try {
                backcomb();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

        encryptBtn.addActionListener(e -> {
            try {
                onEncrypt();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        decryptBtn.addActionListener(e -> {
            try {
                onDecrypt();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        zipBtn.addActionListener(e -> {
            try {
                onCompress();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        unzipBtn.addActionListener(e -> {
            try {
                onDecompress();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * 验证文件一致性
     *
     * @throws Exception
     */
    private void onVerify() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String firstpath1 = this.srcPath.getText().replace('\\', '/');
        String secondpath2 = this.dstPath.getText().replace('\\', '/');

        boolean result = Verify.verify(firstpath1, secondpath2);

        if (result) {
            JOptionPane.showMessageDialog(null, "备份验证成功，所选目录或文件二者一致", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "备份验证失败，所选目录或文件二者不一致", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 复制备份
     *
     * @throws Exception
     */
    private void onCopy() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File f = new File(this.srcPath.getText().replace('\\', '/'));
        Copy.copy(f.toString(), this.dstPath.getText().replace('\\', '/') + '/' + f.getName());
        JOptionPane.showMessageDialog(null, "操作已完成");
    }

    /**
     * 文件自动备份
     */
    private void backcomb()throws Exception{
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String firstpath = this.srcPath.getText().replace('\\', '/');
        String secondpath = this.dstPath.getText().replace('\\', '/');
        //输入备份时间间隔
        String key;
        boolean loop = true;
        int i = 0;
        while (loop) {
            try {
                key = JOptionPane.showInputDialog("请输入循环时间");
                i = Integer.parseInt(key);
                loop = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "请输入数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (i == 0)
        {
            //终止备份

            t.cancel();
            System.out.println("任务结束");
            return;
        }

        t.schedule(new BackupFile(firstpath, secondpath), 6000, i);//6秒后开始没i毫秒备份一次

    }


    /**
     * 文件压缩
     *
     * @throws Exception
     */
    private void onCompress() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File f = new File(this.srcPath.getText().replace('\\', '/'));
        Zip.compress(f.toString(), this.dstPath.getText().replace('\\', '/') + '/' + f.getName() + ".zip");
        JOptionPane.showMessageDialog(null, "操作已完成");
    }

    /**
     * 文件解压
     *
     * @throws Exception
     */
    private void onDecompress() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File f = new File(this.srcPath.getText().replace('\\', '/'));
        String fileName = f.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (suffix.equals("zip")) {
            Zip.decompress(f.toString(), this.dstPath.getText().replace('\\', '/'));
        }
        JOptionPane.showMessageDialog(null, "操作已完成");
    }

    /**
     * 文件加密
     *
     * @throws Exception
     */
    private void onEncrypt() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key;
        boolean loop = true;
        int i = 0;
        while (loop) {
            try {
                key = JOptionPane.showInputDialog("请输入数字密码");
                i = Integer.parseInt(key);
                loop = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "请输入数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        File f = new File(this.srcPath.getText().replace('\\', '/'));
        Encrypt.encrypt(f.toString(), this.dstPath.getText().replace('\\', '/') + '/' + f.getName(), i);
        JOptionPane.showMessageDialog(null, "操作已完成");
    }

    /**
     * 文件解密
     *
     * @throws Exception
     */
    private void onDecrypt() throws Exception {
        if (!Utils.isPathValid(this.dstPath.getText()) || !Utils.isPathValid(this.srcPath.getText())) {
            JOptionPane.showMessageDialog(null, "路径解析失败，请重新检查！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.srcPath.getText().replace("\\", "/").contains(this.dstPath.getText().replace("\\", "/"))){
            JOptionPane.showMessageDialog(null, "请不要在同一目录下解密文件！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String key;
        boolean loop = true;
        int i = 0;
        while (loop) {
            try {
                key = JOptionPane.showInputDialog("请输入数字密码");
                i = Integer.parseInt(key);
                loop = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "请输入数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        File f = new File(this.srcPath.getText().replace('\\', '/'));
        Encrypt.decrypt(f.toString(), this.dstPath.getText().replace('\\', '/') + '/' + f.getName(), i);
        JOptionPane.showMessageDialog(null, "操作已完成");
    }

    private void setDroppable(JTextArea dropArea, JTextField targetField, boolean onlyDir) {
        // 1.拖拽响应
        dropArea.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    // set path
                    targetField.setText(filepath);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
        // 2.点击选择路径
        dropArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (onlyDir) {
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                } else {
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                }
                if (onlyDir) {
                    fileChooser.showDialog(new JLabel(), "选择文件夹");
                } else {
                    fileChooser.showDialog(new JLabel(), "选择文件/文件夹");
                }
                File file = fileChooser.getSelectedFile();
                if (null != file && file.exists()) {
                    targetField.setText(file.toString());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                MainWindow.this.setCursor(new Cursor(12));  // 光标切换为“点击”
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MainWindow.this.setCursor(new Cursor(0));  // 光标切换为“默认”
            }
        });
    }
}
