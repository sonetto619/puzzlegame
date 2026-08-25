package teat;

import javax.swing.*;

public class Test3 {
    public static void main(String[] args) {
        // 创建一个JFrame对象
        JFrame jFrame = new JFrame();
        // 设置窗口的大小
        jFrame.setSize(603, 680);
        // 设置窗口的标题
        jFrame.setTitle("事件演示");
        // 设置窗口总是在最上层
        jFrame.setAlwaysOnTop(true);
        // 设置窗口居中
        jFrame.setLocationRelativeTo(null);
        // 设置窗口关闭操作
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 设置窗口的布局管理器为空
        jFrame.setLayout(null);

        //创建一个按钮对象
        JButton jbt = new JButton("点击我");
        // 设置按钮的位置和大小
        jbt.setBounds(0, 0, 100, 50);
        // 给按钮添加监听器
        jbt.addActionListener(new MyActionListener());

        // 将按钮添加到窗口中
        jFrame.getContentPane().add(jbt);

        // 设置窗口可见
        jFrame.setVisible(true);
    }
}
