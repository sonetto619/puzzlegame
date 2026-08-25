# Puzzle Random Blank Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Leave a single randomly positioned blank grid cell by not rendering the sixteenth puzzle image.

**Architecture:** Retain the existing shuffled values `0` through `15`. During the render loop, treat `15` as the blank-cell sentinel and skip its `JLabel` placement; all other values preserve their existing mapping to image tiles.

**Tech Stack:** Java 26, Swing, Java assertions.

---

### Task 1: Add a failing test for the blank tile

**Files:**
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`
- Test: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrameImageTest.java`

- [ ] **Step 1: Replace the fixed background index and add a component-count assertion**

```java
import java.util.Arrays;

Component background = Arrays.stream(frame.getContentPane().getComponents())
        .filter(component -> component.getX() == 40 && component.getY() == 40)
        .findFirst()
        .orElseThrow();
assert frame.getContentPane().getComponentCount() == 16
        : "15 puzzle tiles and one background should be rendered";
assert frame.getContentPane().getComponentZOrder(background)
        == frame.getContentPane().getComponentCount() - 1
        : "background should be below every puzzle tile";
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```powershell
javac -encoding UTF-8 -cp out/production/puzzlegame -d out/production/puzzlegame puzzlegame/ui/GameJFrameImageTest.java
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: assertion failure because the current window has 17 components: 16 tiles plus the background.

### Task 2: Skip the blank tile during rendering

**Files:**
- Modify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java:97-106`

- [ ] **Step 1: Add the sentinel guard before setting bounds**

```java
int num = data[row][col];
if (num == 15) {
    continue;
}
```

- [ ] **Step 2: Recompile and rerun the test**

Run:

```powershell
javac -encoding UTF-8 -d out/production/puzzlegame APP.java puzzlegame/ui/*.java
java -ea -cp out/production/puzzlegame puzzlegame.ui.GameJFrameImageTest
```

Expected: the process exits with code 0; one randomly selected coordinate has no tile label and the background remains at the bottom.

### Task 3: Verify the next game window

**Files:**
- Verify: `C:/Users/Sonetto/IdeaProjects/puzzlegame/puzzlegame/ui/GameJFrame.java`

- [ ] **Step 1: Run `APP` from IntelliJ IDEA**

Expected: 15 tiles are visible, exactly one grid cell is empty, and its location changes across launches.

## Self-review

- Spec coverage: Task 1 proves that the sixteenth tile is absent; Task 2 implements the `15` sentinel; Task 3 checks randomized visual behavior.
- Scope: no input, movement, background, or randomization algorithm changes.
- Repository: this project has no `.git` directory, so no commit step is applicable.
