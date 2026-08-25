package teat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyJFrame3 extends JFrame implements KeyListener {
    public MyJFrame3() {
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

        // 给整个窗体添加键盘监听
        this.addKeyListener(this);



        // 设置窗口可见
        this.setVisible(true);
    }

    public MyJFrame3(GraphicsConfiguration gc) {
        super(gc);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println((char) e.getKeyCode() + "被按下");

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println((char) e.getKeyCode() + "被释放");
    }
}
