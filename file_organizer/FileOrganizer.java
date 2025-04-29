import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {
    private static final Map<String, String> FILE_TYPES = new HashMap<>();
    static {
        FILE_TYPES.put("documents", ".pdf,.docx,.doc,.txt,.rtf");
        FILE_TYPES.put("images", ".jpg,.jpeg,.png,.gif,.bmp");
        FILE_TYPES.put("music", ".mp3,.wav");
        FILE_TYPES.put("videos", ".mp4,.avi,.mov,.mkv");
        FILE_TYPES.put("archives", ".zip,.rar,.7z,.tar,.gz");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FileOrganizer <source_directory>");
            return;
        }

        String sourceDir = args[0];
        organizeFiles(sourceDir);
    }

    private static void organizeFiles(String sourceDir) {
        File directory = new File(sourceDir);
        if (!directory.isDirectory()) {
            System.out.println("Invalid source directory: " + sourceDir);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the source directory.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName().toLowerCase();
                String fileExtension = getFileExtension(fileName);

                for (Map.Entry<String, String> entry : FILE_TYPES.entrySet()) {
                    String category = entry.getKey();
                    String extensions = entry.getValue();

                    if (extensions.contains(fileExtension)) {
                        moveFile(file, category, sourceDir);
                        break;
                    }
                }
            }
        }
    }

    private static void moveFile(File file, String category, String sourceDir) {
        String destinationDir = sourceDir + File.separator + category;
        File destDir = new File(destinationDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        Path source = file.toPath();
        Path destination = Paths.get(destinationDir, file.getName());

        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved " + file.getName() + " to " + category);
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex).toLowerCase();
    }
}