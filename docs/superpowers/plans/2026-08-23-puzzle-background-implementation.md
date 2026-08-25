# Puzzle Background Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Scale the game background to its label bounds and render it below all 16 puzzle tiles.

**Architecture:** Keep image construction in `GameJFrame`. Add one private image-scaling helper, retain the existing tile slicing logic, and add the background after the tiles so Swing assigns it the lowest Z-order. A lightweight Java assertion test will exercise the helper and inspect the content pane order.

**Tech Stack:** Java 26, Swing, `ImageIcon`, `ImageIO`, Java assertions.

---

### Task 1: Create a reproducible rendering test

**Files:**
- Create: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`
- Test: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`

- [ ] **Step 1: Write the failing test**

```java
package puzzlegame.ui;

import javax.swing.ImageIcon;
import java.awt.Component;
import java.lang.reflect.Method;

public class GameJFrameImageTest {
    public static void main(String[] args) throws Exception {
        Method createScaledIcon = GameJFrame.class.getDeclaredMethod(
                "createScaledIcon", String.class, int.class, int.class);
        createScaledIcon.setAccessible(true);

        ImageIcon icon = (ImageIcon) createScaledIcon.invoke(
                null,
                "C:\\Users\\Sonetto\\IdeaProjects\\puzzlegame\\image\\background.png",
                508,
                560);
        assert icon.getIconWidth() == 508 : "background width should be 508";
        assert icon.getIconHeight() == 560 : "background height should be 560";

        GameJFrame frame = new GameJFrame();
        Component background = frame.getContentPane().getComponents()[16];
        assert frame.getContentPane().getComponentZOrder(background) == 16
                : "background should be below every puzzle tile";
        frame.dispose();
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```powershell
javac -encoding UTF-8 -cp out/production/puzzlegame -d out/production/puzzlegame puzzlegame/ui/GameJFrameImageTest.java
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: failure because `GameJFrame` has no `createScaledIcon` method.

### Task 2: Scale and layer the background image

**Files:**
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:3-7`
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:56-110`

- [ ] **Step 1: Add the image scaling helper and import**

```java
import java.awt.Image;

private static ImageIcon createScaledIcon(String imagePath, int width, int height) {
    Image source = new ImageIcon(imagePath).getImage();
    Image scaled = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
}
```

- [ ] **Step 2: Replace the background construction and defer its insertion**

```java
JLabel background = new JLabel(createScaledIcon(
        "C:\\Users\\Sonetto\\IdeaProjects\\puzzlegame\\image\\background.png",
        508,
        560));
background.setBounds(40, 40, 508, 560);
```

Remove the early `this.getContentPane().add(background);`. After the `try`/`catch` that adds the 16 tile labels, add:

```java
this.getContentPane().add(background);
```

- [ ] **Step 3: Run the assertion test again**

Run:

```powershell
javac -encoding UTF-8 -d out/production/puzzlegame APP.java puzzlegame/ui/*.java
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: the assertion process exits with code 0, the background icon measures 508 x 560, and it has Z-order 16.

### Task 3: Verify the visual result

**Files:**
- Verify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java`

- [ ] **Step 1: Start `APP` from IntelliJ IDEA**

Expected: the game window shows the complete blue background frame and 16 visible puzzle tiles inside its white center. The separate login and registration windows remain unrelated to this background fix.

- [ ] **Step 2: Check the Run console**

Expected: shuffled tile data may print, but no `ImageIO` exception or assertion failure appears.

## Self-review

- Spec coverage: Task 2 scales the image and corrects its Z-order; Task 3 verifies the three stated acceptance criteria.
- Scope: login, registration, randomization, and input behavior are intentionally unchanged.
- Repository: this project has no `.git` directory, so no commit step is applicable.
