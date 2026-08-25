package puzzlegame.ui;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 统一定位游戏图片资源，避免资源路径依赖启动时的当前工作目录。
 */
final class AssetLoader {
    private AssetLoader() {
        // 工具类不需要创建对象。
    }

    /**
     * 查找 image 文件夹中的指定图片。
     *
     * @param fileName 图片文件名，也可以传入 image\\xxx.jpg 形式的旧路径
     * @return 找到的图片文件
     */
    static File imageFile(String fileName) {
        String normalizedName = normalizeName(fileName);
        Path currentDirectory = Paths.get(System.getProperty("user.dir"))
                .toAbsolutePath()
                .normalize();

        Path programDirectory = getProgramDirectory();
        Path candidate = findInParents(currentDirectory, normalizedName);
        if (candidate != null) {
            return candidate.toFile();
        }

        candidate = findInParents(programDirectory, normalizedName);
        if (candidate != null) {
            return candidate.toFile();
        }

        throw new IllegalArgumentException("找不到图片资源: " + normalizedName);
    }

    /**
     * 加载 image 文件夹中的图片图标。
     */
    static ImageIcon imageIcon(String fileName) {
        return new ImageIcon(imageFile(fileName).getAbsolutePath());
    }

    /**
     * 查找 image 文件夹本身，用于动态扫描可选关卡图片。
     */
    static File imageDirectory() {
        Path currentDirectory = Paths.get(System.getProperty("user.dir"))
                .toAbsolutePath()
                .normalize();
        Path directory = findDirectoryInParents(currentDirectory);
        if (directory == null) {
            directory = findDirectoryInParents(getProgramDirectory());
        }
        if (directory == null) {
            throw new IllegalArgumentException("找不到 image 图片文件夹");
        }
        return directory.toFile();
    }

    private static String normalizeName(String fileName) {
        String normalized = fileName.replace('\\', '/');
        if (normalized.startsWith("image/")) {
            normalized = normalized.substring("image/".length());
        }
        return normalized;
    }

    private static Path findInParents(Path start, String fileName) {
        if (start == null) {
            return null;
        }
        Path current = start;
        while (current != null) {
            Path candidate = current.resolve("image").resolve(fileName);
            if (candidate.toFile().isFile()) {
                return candidate;
            }
            current = current.getParent();
        }
        return null;
    }

    private static Path findDirectoryInParents(Path start) {
        if (start == null) {
            return null;
        }
        Path current = start;
        while (current != null) {
            Path candidate = current.resolve("image");
            if (candidate.toFile().isDirectory()) {
                return candidate;
            }
            current = current.getParent();
        }
        return null;
    }

    private static Path getProgramDirectory() {
        try {
            Path location = Paths.get(AssetLoader.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            return location.toFile().isFile() ? location.getParent() : location;
        } catch (URISyntaxException | NullPointerException exception) {
            return null;
        }
    }
}
