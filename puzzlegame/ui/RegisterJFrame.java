package puzzlegame.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * 注册窗口。当前不保存账号数据，只校验输入格式和验证码后返回登录页。
 */
public class RegisterJFrame extends JFrame implements ActionListener {
    // 注册表单控件。
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField captchaField = new JTextField();
    private final JLabel captchaLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton submitButton = new JButton("注册");
    private final JButton backButton = new JButton("返回登录");
    // 用于恢复密码框的默认隐藏效果。
    private char passwordEchoChar;
    // 保存当前显示的验证码，用于提交时比较。
    private String captchaCode;

    /**
     * 创建并显示注册窗口。
     */
    public RegisterJFrame() {
        initJFrame();
        initView();
        refreshCaptcha();
        this.setVisible(true);
    }

    /**
     * 初始化注册窗口的基础属性。
     */
    private void initJFrame() {
        this.setSize(603, 680);
        this.setTitle("拼图 注册");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
    }

    /**
     * 按登录页相同的四行布局创建注册表单。
     */
    private void initView() {
        addLabel("用户名", 130, 165, 80, 30);
        usernameField.setBounds(210, 165, 190, 30);
        this.getContentPane().add(usernameField);

        addLabel("密码", 130, 220, 80, 30);
        passwordField.setBounds(210, 220, 160, 30);
        passwordEchoChar = passwordField.getEchoChar();
        this.getContentPane().add(passwordField);
        addPasswordEyeButton(370, 220);

        addLabel("验证码", 130, 275, 80, 30);
        captchaField.setBounds(210, 275, 80, 30);
        this.getContentPane().add(captchaField);
        captchaLabel.setBounds(305, 275, 95, 30);
        captchaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        captchaLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        captchaLabel.setToolTipText("点击更换验证码");
        captchaLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshCaptcha();
            }
        });
        this.getContentPane().add(captchaLabel);

        submitButton.setBounds(175, 335, 85, 32);
        backButton.setBounds(300, 335, 100, 32);
        submitButton.addActionListener(this);
        backButton.addActionListener(this);
        this.getContentPane().add(submitButton);
        this.getContentPane().add(backButton);

        messageLabel.setBounds(130, 385, 330, 25);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        this.getContentPane().add(messageLabel);

        // 背景最后添加，位于所有输入控件的底层。
        this.getContentPane().add(LoginJFrame.createBackgroundLabel());
    }

    /**
     * 在指定位置添加表单文字标签。
     */
    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        this.getContentPane().add(label);
    }

    /**
     * 添加密码显示按钮。按住时显示明文，松开或移出按钮后重新隐藏。
     */
    private void addPasswordEyeButton(int x, int y) {
        JButton eyeButton = new JButton("\uD83D\uDC41");
        eyeButton.setBounds(x, y, 30, 30);
        eyeButton.setMargin(new Insets(0, 0, 0, 0));
        eyeButton.setToolTipText("按住显示密码");
        eyeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                passwordField.setEchoChar(passwordEchoChar);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passwordField.setEchoChar(passwordEchoChar);
            }
        });
        this.getContentPane().add(eyeButton);
    }

    /**
     * 生成并显示新的随机验证码，同时清空旧输入。
     */
    private void refreshCaptcha() {
        captchaCode = LoginJFrame.createVerificationCode();
        captchaLabel.setText(captchaCode);
        captchaField.setText("");
    }

    /**
     * 校验注册页中的用户名、密码和验证码。
     */
    private boolean validateRegistrationInput() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        String passwordText = new String(password);
        // 使用后清空字符数组，避免密码在内存中停留过久。
        Arrays.fill(password, '\0');

        if (!LoginJFrame.isLegalInput(username) || !LoginJFrame.isLegalInput(passwordText)) {
            messageLabel.setText("用户名和密码只能使用字母、数字和下划线");
            return false;
        }
        if (!LoginJFrame.isCaptchaCorrect(captchaField.getText(), captchaCode)) {
            messageLabel.setText("验证码错误，请重新输入");
            refreshCaptcha();
            return false;
        }
        return true;
    }

    /**
     * 处理注册提交和返回登录页两个按钮的点击事件。
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (validateRegistrationInput()) {
                this.dispose();
                new LoginJFrame();
            }
        } else if (e.getSource() == backButton) {
            this.dispose();
            new LoginJFrame();
        }
    }
}
