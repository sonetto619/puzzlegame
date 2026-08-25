package puzzlegame.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

import java.awt.event.KeyEvent;
/**
 * 拼图游戏主窗口，负责图片渲染、键盘移动和菜单操作。
 */
public class GameJFrame extends JFrame implements KeyListener, ActionListener {

    // 定义二维数组 对应16个图片的位置
    int[][] data = new int[4][4];

    // 记录空白方块在二维数组中的位置
    int x = 0, y = 0;

    // 定义一个变量 记录当前显示的图片的路径
    String path = "image\\";
    String imageName = "image0.jpg";

    // 定义一个二维数组，存储正确的数据
    int[][] win = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
    };

    // 定义一个变量 记录步数
    int step = 0;

    // 创建选项下面的条目对象
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reLoginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem accountItem = new JMenuItem("公众号");
    JMenu changeImageMenu = new JMenu("更换图片");

    // 定义标签数组 对应16个图片
    JLabel[] labels = new JLabel[16];

    /**
     * 创建游戏窗口，并完成菜单、拼图数据和界面的初始化。
     */
    public GameJFrame(){
        // 初始化窗口
        initJFrame();
        // 初始化菜单
        initJMenuBar();
        //初始化数据
        initData();
        // 初始化图片
        initImage();

        // 设置窗口的标题
        this.setVisible(true);
    }

    /**
     * 使用指定图形配置创建窗口，供测试或特殊显示环境使用。
     */
    public GameJFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    // 初始化数据
    private void initData() {
        //定义一个数组
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        // 打乱数组
        Random r = new Random();
        for(int i = 0; i < tempArr.length; i++){
            int index = r.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }

        // 将打乱后的数组赋值给data二维数组
        for(int i = 0;i < tempArr.length;i++){
            // 15 是唯一的空值哨兵；0 仍然是一个正常图片块。
            data[i / 4][i % 4] = tempArr[i];
            if(tempArr[i] == 15) {
                x = i / 4;
                y = i % 4;
            }
        }
    }

    // 初始化图片
    private void initImage() {
        // 清空原有的图片
        this.getContentPane().removeAll();

        if (victory()) {
            ImageIcon icon = AssetLoader.imageIcon("victory.jpg");
            // 获取图片
            Image scaledImage = icon.getImage().getScaledInstance(
                    197, 99, Image.SCALE_SMOOTH);
            // 创建标签
            JLabel winLabel = new JLabel(new ImageIcon(scaledImage));
            winLabel.setBounds(203, 203, 197, 99);
            this.getContentPane().add(winLabel);
        }

        JLabel stepCount = new JLabel("步数: " + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        // 添加背景图片
        JLabel background = new JLabel(createScaledIcon(
                path + "background.png",
                508,
                560));
        background.setBounds(40, 40, 508, 560);

        try {
            // 读取完整的大图
            BufferedImage image = ImageIO.read(AssetLoader.imageFile(imageName));

            // 获取原图片的宽高
            int width = image.getWidth();
            int height = image.getHeight();

            // 每一块的宽高
            int pieceWidth = width / 4;
            int pieceHeight = height / 4;

            // 4行4列
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {

                    // 从原图中截取一小块
                    BufferedImage piece = image.getSubimage(col * pieceWidth,row * pieceHeight,pieceWidth,pieceHeight);

                    // 把这一小块图片放进 JLabel
                    JLabel jLabel = new JLabel(new ImageIcon(piece));
                    labels[row * 4 + col] = jLabel;

                }
            }

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    // 获取当前数据
                    int num = data[row][col];
                    if (num == 15) {
                        continue;
                    }

                    // 设置位置
                    labels[num].setBounds(col * 105 + 83, row * 105 + 134, 105, 105);
                    // 给图片添加边框
                    // bevelType: 0表示凹陷，1表示凸起
                    labels[num].setBorder(new BevelBorder(BevelBorder.RAISED)); // 1表示凸起

                    // 添加到窗口
                    this.getContentPane().add(labels[num]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 背景最后添加，位于拼图块的底层。
        this.getContentPane().add(background);

        // 刷新界面
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    /**
     * 将指定图片按目标宽高缩放后封装为图标。
     */
    private static ImageIcon createScaledIcon(String imagePath, int width, int height) {
        Image source = AssetLoader.imageIcon(imagePath).getImage();
        Image scaled = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // 初始化菜单
    private void initJMenuBar() {
        // 创建整个菜单对象
        JMenuBar jMenuBar = new JMenuBar();

        // 创建文件菜单对象
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我们");

        // 将每一个选项下面的条目添加到菜单
        functionJMenu.add(replayItem);
        initChangeImageMenu();
        functionJMenu.add(changeImageMenu);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);
        aboutJMenu.add(accountItem);

        // 给条目绑定事件
        replayItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        // 将菜单添加到窗口
        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);

        // 给整个界面设置菜单
        this.setJMenuBar(jMenuBar);

    }

    // 扫描 image 文件夹，为每张游戏图片创建一个二级菜单项。
    private void initChangeImageMenu() {
        File imageDirectory = AssetLoader.imageDirectory();
        File[] imageFiles = imageDirectory.listFiles(file -> {
            String fileName = file.getName().toLowerCase();
            return file.isFile()
                    && fileName.startsWith("image")
                    && (fileName.endsWith(".jpg")
                    || fileName.endsWith(".jpeg")
                    || fileName.endsWith(".png")
                    || fileName.endsWith(".gif"));
        });

        if (imageFiles == null) {
            return;
        }

        Arrays.sort(imageFiles, (first, second) ->
                first.getName().compareToIgnoreCase(second.getName()));

        for (int i = 0; i < imageFiles.length; i++) {
            JMenuItem imageItem = new JMenuItem("图片" + (i + 1));
            // 菜单显示名称和真实文件名分开保存，避免路径写死。
            imageItem.setActionCommand(imageFiles[i].getName());
            imageItem.addActionListener(this);
            changeImageMenu.add(imageItem);
        }
    }

    private void initJFrame() {
        // 设置窗口的大小
        this.setSize(603, 680);
        // 设置界面标题
        this.setTitle("拼图单机版 v1.0");
        // 设置界面置顶
        this.setAlwaysOnTop(true);
        // 设置界面居中
        this.setLocationRelativeTo(null);
        // 设置关闭模式
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 取消默认居中放置
        this.setLayout(null);
        // 给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    /**
     * 键盘字符输入事件。当前游戏不需要处理该事件。
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 处理按键按下事件。按住 E 键时临时显示完整图片和背景。
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // 判断是否按下了E键
        if(code == KeyEvent.VK_E) {
            // 把界面中所有图片全部删除
            this.getContentPane().removeAll();
            // 加载一张完整的图片
            JLabel all = new JLabel(AssetLoader.imageIcon(imageName));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);
            // 加载背景图片
            JLabel background = new JLabel(createScaledIcon(
                    path + "background.png",
                    508,
                    560));
            background.setBounds(40, 40, 508, 560);
            // 添加背景图片
            this.getContentPane().add(background);
            // 刷新界面
            this.getContentPane().revalidate();
            this.getContentPane().repaint();
        }
    }

    /**
     * 处理按键松开事件：方向键移动拼图，E 键恢复拼图，P 键用于测试胜利状态。
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // 判断游戏是否胜利，如果胜利，此方法将不再执行
        if (victory()) {
            // 如果胜利，不再执行后续代码
            return;
        }
        // 对上下左右进行判断
        // 左：37 上：38 右：39 下：40
        int keyCode = e.getKeyCode();
        if(keyCode == 37) {
            moveLeft();
        }else if (keyCode == 38) {
            moveUp();
        }else if (keyCode == 39) {
            moveRight();
        }else if (keyCode == 40) {
            moveDown();
        }else if (keyCode == 69){
            initImage();
        }else if(keyCode == 80){
            data = new int[][]{
                    {0, 1, 2, 3},
                    {4, 5, 6, 7},
                    {8, 9, 10, 11},
                    {12, 13, 14, 15}
            };
            initImage();
        }
    }

    // 判断是否胜利
    public boolean victory() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (data[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 向上移动
    void moveUp() {
        if (x == 3) {
            return;
        }
        // 交换空白方块下方的数字与15哨兵，然后跟踪新的空白行。
        data[x][y] = data[x + 1][y];
        data[x + 1][y] = 15;
        x++;
        step++;
        initImage();
    }

    // 向下移动
    void moveDown() {
        if (x == 0) {
            return;
        }
        // 交换空白方块上方的数字与15哨兵，然后跟踪新的空白行。
        data[x][y] = data[x - 1][y];
        data[x - 1][y] = 15;
        x--;
        step++;
        initImage();
    }

    // 向左移动
    void moveLeft() {
        if (y == 3) {
            return;
        }
        // 交换空白方块右侧的数字与15哨兵，然后跟踪新的空白行。
        data[x][y] = data[x][y + 1];
        data[x][y + 1] = 15;
        y++;
        step++;
        initImage();
    }

    // 向右移动
    void moveRight() {
        if (y == 0) {
            return;
        }
        // 交换空白方块左侧的数字与15哨兵，然后跟踪新的空白行。
        data[x][y] = data[x][y - 1];
        data[x][y - 1] = 15;
        y--;
        step++;
        initImage();
    }

    /**
     * 处理菜单事件，包括重新开始、重新登录、更换图片、关闭游戏和显示公众号。
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // 获取当前被点击的条目对象
        Object obj = e.getSource();
        // 判断是哪个条目被点击了
        if(obj == replayItem){
            // 计步器归零
            step = 0;
            // 再次打乱二维数组中的数据
            initData();
            // 重新初始化图片
            initImage();

        }else if(obj == reLoginItem){
            // 重新登录
            this.setVisible(false);
            // 显示登录界面
            new LoginJFrame().setVisible(true);

        }else if(obj == closeItem){
            // 关闭游戏
            System.exit(0);

        }else if (changeImageMenu.isMenuComponent((Component) obj)) {
            // 选择新图片后，从零步开始新的拼图。
            imageName = e.getActionCommand();
            step = 0;
            initData();
            initImage();

        }else if (obj == accountItem) {
            // 显示公众号图片
            JDialog jDialog = new JDialog(this, "公众号", true);

            // 加载公众号图片
            ImageIcon originalIcon = AssetLoader.imageIcon("gongzhonghao.jpg");
            // 获取图片对象并进行缩放
            Image scaledImage = originalIcon.getImage().getScaledInstance(258, 258, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage)); // 创建一个标签对象，用于显示图片
            jDialog.setContentPane(imageLabel);                         // 设置容器对象
            jDialog.pack();                                             // 调整窗口大小
            jDialog.setResizable(false);                                // 设置不可调整大小
            jDialog.setLocationRelativeTo(this);                        // 设置居中
            jDialog.setModal(true);                                     // 弹窗不关闭，程序不继续执行
            jDialog.setVisible(true);                                   // 显示窗口
        }
    }
}
