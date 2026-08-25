package puzzlegame.ui;

import java.io.File;

/**
 * 检查程序从项目外部启动时，仍能找到 image 文件夹中的资源。
 */
public class AssetLoaderTest {
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        File background = AssetLoader.imageFile("background.png");
        check(background.isFile(), "background.png should be found outside the project working directory");
        check(AssetLoader.imageFile("image0.jpg").isFile(),
                "image0.jpg should be found outside the project working directory");
    }
}
