package puzzlegame.ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.Arrays;

public class GameJFrameImageTest {
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return result;
    }

    private static JMenu findSubMenu(JMenu menu, String text) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item instanceof JMenu && text.equals(item.getText())) {
                return (JMenu) item;
            }
        }
        throw new AssertionError("menu should contain submenu: " + text);
    }

    public static void main(String[] args) throws Exception {
        Method createScaledIcon = GameJFrame.class.getDeclaredMethod(
                "createScaledIcon", String.class, int.class, int.class);
        createScaledIcon.setAccessible(true);

        ImageIcon icon = (ImageIcon) createScaledIcon.invoke(
                null,
                "image\\background.png",
                508,
                560);
        check(icon.getIconWidth() == 508, "background width should be 508");
        check(icon.getIconHeight() == 560, "background height should be 560");

        GameJFrame frame = new GameJFrame();
        Component background = Arrays.stream(frame.getContentPane().getComponents())
                .filter(component -> component.getX() == 40 && component.getY() == 40)
                .findFirst()
                .orElseThrow();
        check(frame.getContentPane().getComponentCount() == 17,
                "15 puzzle tiles, the background, and the step label should be rendered");
        check(frame.getContentPane().getComponentZOrder(background)
                        == frame.getContentPane().getComponentCount() - 1,
                "background should be below every puzzle tile");

        frame.data = new int[][]{
                {1, 2, 3, 4},
                {5, 9, 15, 8},
                {10, 11, 6, 12},
                {13, 14, 0, 7}
        };
        frame.x = 1;
        frame.y = 2;
        frame.moveUp();
        check(frame.data[1][2] == 6, "tile below blank should move into blank position");
        check(frame.data[2][2] == 15, "blank marker should move down one row");
        check(frame.x == 2 && frame.y == 2, "blank coordinates should move down one row");
        check(frame.getContentPane().getComponentCount() == 17,
                "rerender should contain 15 puzzle tiles, the background, and the step label");
        check(Arrays.stream(frame.getContentPane().getComponents())
                        .anyMatch(component -> component == frame.labels[6]
                                && component.getX() == 2 * 105 + 83
                                && component.getY() == 1 * 105 + 134),
                "tile below blank should render at the former blank cell");

        frame.x = 3;
        frame.y = 2;
        int[][] snapshot = copy(frame.data);
        frame.moveUp();
        check(Arrays.deepEquals(frame.data, snapshot),
                "up at bottom edge should leave board unchanged");

        frame.keyPressed(new KeyEvent(
                frame,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_E,
                'E'));
        Component previewBackground = Arrays.stream(frame.getContentPane().getComponents())
                .filter(component -> component.getX() == 40 && component.getY() == 40)
                .findFirst()
                .orElseThrow();
        check(frame.getContentPane().getComponentCount() == 2,
                "full-image preview should contain the image and its background");
        check(((ImageIcon) ((JLabel) previewBackground).getIcon()).getIconWidth() == 508,
                "preview background should use the scaled 508-pixel image");
        check(frame.getContentPane().getComponentZOrder(previewBackground) == 1,
                "preview background should stay behind the full image");

        JMenuBar menuBar = frame.getJMenuBar();
        JMenu changeImageMenu = findSubMenu(menuBar.getMenu(0), "更换图片");
        check(changeImageMenu.getItemCount() == 4,
                "change-image submenu should contain every image-prefixed game picture");
        check("图片1".equals(changeImageMenu.getItem(0).getText()),
                "the first image should be labelled 图片1");
        check("图片4".equals(changeImageMenu.getItem(3).getText()),
                "the last image should be labelled 图片4");

        frame.step = 9;
        changeImageMenu.getItem(0).doClick();
        check("image0.jpg".equals(frame.imageName),
                "clicking 图片1 should select image0.jpg");
        check(frame.step == 0,
                "changing the game image should reset the step count");
        frame.dispose();
    }
}
