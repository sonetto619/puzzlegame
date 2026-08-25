package teat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyFrame2 extends JFrame implements MouseListener {
    JButton jbt1 = new JButton("点我啊");

    public MyFrame2(){
        // 设置窗口的大小
        this.setSize(603, 680);
        // 设置窗口的标题
        this.setTitle("事件演示");
        // 设置窗口总是在最上层
        this.setAlwaysOnTop(true);
        // 设置窗口居中
        this.setLocationRelativeTo(null);
        // 设置窗口关闭操作
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 设置窗口的布局管理器为空
        this.setLayout(null);

        jbt1.setBounds(0, 0, 100, 50);
        // 为按钮添加鼠标监听器
        jbt1.addMouseListener(this);
        // 将按钮添加到窗口中
        this.getContentPane().add(jbt1);
        // 设置窗口可见
        this.setVisible(true);
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("单击");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("按下");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("松开");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("划入");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("划出");
    }

}
