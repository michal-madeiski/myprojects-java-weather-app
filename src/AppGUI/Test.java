package AppGUI;

import java.awt.*;

public class Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App();
            }
        });
    }
}