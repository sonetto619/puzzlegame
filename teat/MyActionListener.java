package teat;

import java.awt.event.ActionListener;

public class MyActionListener implements ActionListener {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        System.out.println("按钮被点击了");
    }
}
