package puzzlegame.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

/**
 * 登录窗口，负责收集账号、密码和验证码，并在校验通过后进入游戏。
 */
public class LoginJFrame extends JFrame implements ActionListener {
    // 登录页面使用的背景图片路径。
    // 验证码可使用的字符，排除了容易混淆的 I、O、0、1。
    private static final String CAPTCHA_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    // 登录表单控件。
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField captchaField = new JTextField();
    private final JLabel captchaLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton loginButton = new JButton("登录");
    private final JButton registerButton = new JButton("注册");
    // 记录密码框默认的掩码字符，以便松开眼睛按钮后恢复隐藏。
    private char passwordEchoChar;
    // 保存当前显示的验证码，用于与用户输入进行比较。
    private String captchaCode;

    /**
     * 创建并显示登录窗口。
     */
    public LoginJFrame() {
        initJFrame();
        initView();
        refreshCaptcha();
        this.setVisible(true);
    }

    /**
     * 初始化登录窗口的基础属性。
     */
    private void initJFrame() {
        this.setSize(603, 680);
        this.setTitle("拼图 登录");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
    }

    /**
     * 按“用户名、密码、验证码、操作按钮”四行布局创建界面。
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

        loginButton.setBounds(190, 335, 85, 32);
        registerButton.setBounds(315, 335, 85, 32);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        this.getContentPane().add(loginButton);
        this.getContentPane().add(registerButton);

        messageLabel.setBounds(130, 385, 330, 25);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        this.getContentPane().add(messageLabel);

        // 背景最后添加，位于所有输入控件的底层。
        this.getContentPane().add(createBackgroundLabel());
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
     * 创建缩放后的背景标签，供登录页和注册页复用。
     */
    static JLabel createBackgroundLabel() {
        Image source = AssetLoader.imageIcon("background.png").getImage();
        Image scaled = source.getScaledInstance(508, 560, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaled));
        background.setBounds(40, 40, 508, 560);
        return background;
    }

    /**
     * 判断用户名或密码是否非空，且只包含字母、数字或下划线。
     */
    static boolean isLegalInput(String value) {
        return value != null && value.matches("[A-Za-z0-9_]+");
    }

    /**
     * 比较用户输入与当前验证码，字母大小写不敏感。
     */
    static boolean isCaptchaCorrect(String input, String expected) {
        return input != null && expected != null && input.trim().equalsIgnoreCase(expected);
    }

    /**
     * 随机生成四位字母数字验证码。
     */
    static String createVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            code.append(CAPTCHA_CHARACTERS.charAt(random.nextInt(CAPTCHA_CHARACTERS.length())));
        }
        return code.toString();
    }

    /**
     * 生成并显示新验证码，同时清空旧的验证码输入。
     */
    private void refreshCaptcha() {
        captchaCode = createVerificationCode();
        captchaLabel.setText(captchaCode);
        captchaField.setText("");
    }

    /**
     * 校验登录信息。校验失败时在页面上提示原因；验证码错误会自动刷新。
     */
    private boolean validateLoginInput() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        String passwordText = new String(password);
        // 使用后清空字符数组，避免密码在内存中停留过久。
        Arrays.fill(password, '\0');

        if (!isLegalInput(username) || !isLegalInput(passwordText)) {
            messageLabel.setText("用户名和密码只能使用字母、数字和下划线");
            return false;
        }
        if (!isCaptchaCorrect(captchaField.getText(), captchaCode)) {
            messageLabel.setText("验证码错误，请重新输入");
            refreshCaptcha();
            return false;
        }
        return true;
    }

    /**
     * 处理登录和跳转注册页两个按钮的点击事件。
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            if (validateLoginInput()) {
                this.dispose();
                new GameJFrame();
            }
        } else if (e.getSource() == registerButton) {
            this.dispose();
            new RegisterJFrame();
        }
    }
}
