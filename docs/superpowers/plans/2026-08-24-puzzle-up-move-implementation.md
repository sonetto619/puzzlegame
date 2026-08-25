# Puzzle Up Move Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Move the tile beneath the blank cell upward without confusing the blank with tile `0` or accessing beyond the grid.

**Architecture:** Keep `15` as the data-model sentinel for the blank cell. Extract the up-move data exchange into a package-visible `moveUp()` method so it can be called from the keyboard handler and directly tested. The renderer continues to skip `15`.

**Tech Stack:** Java 26, Swing, Java assertions.

---

### Task 1: Add failing assertions for the up move

**Files:**
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`
- Test: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`

- [ ] **Step 1: Add deterministic state assertions**

```java
private static int[][] copy(int[][] board) {
    return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
}

frame.data = new int[][] {
        {0, 1, 2, 3},
        {4, 5, 15, 7},
        {8, 9, 6, 10},
        {11, 12, 13, 14}
};
frame.x = 1;
frame.y = 2;
frame.moveUp();

assert frame.data[1][2] == 6 : "tile below the blank should move up";
assert frame.data[2][2] == 15 : "new blank cell should retain the 15 sentinel";
assert frame.x == 2 && frame.y == 2 : "blank coordinates should move down";

frame.x = 3;
frame.y = 2;
int[][] beforeBoundaryMove = copy(frame.data);
frame.moveUp();
assert Arrays.deepEquals(frame.data, beforeBoundaryMove)
        : "up move at the bottom edge must not change data";
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```powershell
javac -encoding UTF-8 -cp out/production/puzzlegame -d out/production/puzzlegame puzzlegame/ui/GameJFrameImageTest.java
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: compilation fails because `GameJFrame` does not define `moveUp()`.

### Task 2: Keep the blank sentinel and move it safely

**Files:**
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:39-60`
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:67-72`
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:186-201`

- [ ] **Step 1: Preserve the `15` value during initialization**

```java
data[i / 4][i % 4] = tempArr[i];
if (tempArr[i] == 15) {
    x = i / 4;
    y = i % 4;
}
```

- [ ] **Step 2: Correct the background width**

```java
JLabel background = new JLabel(createScaledIcon(
        "image\\background.png",
        508,
        560));
```

- [ ] **Step 3: Add the tested movement method with explanatory comments**

```java
void moveUp() {
    if (x == 3) {
        return;
    }

    // 15 is the only blank-cell marker; tile 0 remains a normal image tile.
    data[x][y] = data[x + 1][y];
    data[x + 1][y] = 15;
    x++;
    initImage();
}
```

- [ ] **Step 4: Call the method from the up-key branch**

```java
} else if (keyCode == KeyEvent.VK_UP) {
    moveUp();
}
```

### Task 3: Recompile and verify

**Files:**
- Verify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java`
- Verify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`

- [ ] **Step 1: Compile all Java sources and run assertions**

```powershell
$sources = Get-ChildItem -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out/production/puzzlegame $sources
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: exit code 0. The test verifies both a valid up move and the bottom-edge no-op.

- [ ] **Step 2: Run `APP` in IntelliJ IDEA**

Expected: pressing Up moves only the tile beneath the blank cell into it. At the bottom edge, pressing Up does nothing and causes no exception.

## Self-review

- Spec coverage: initialization preserves `15`; `moveUp()` handles valid and bottom-edge cases; the background width correction removes the unrelated negative-size typo.
- Scope: only the up direction is implemented; the other arrow directions remain unchanged.
- Repository: this project has no `.git` directory, so no commit step is applicable.
