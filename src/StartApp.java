import views.MainWindow;

import javax.swing.*;

public class StartApp {
    public static void main(String[] args) {
        JFrame mainWindow;
        mainWindow = new MainWindow();
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}
