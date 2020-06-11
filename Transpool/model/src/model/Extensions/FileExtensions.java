package model.Extensions;

import java.io.File;

public class FileExtensions {
    public static String getFileExtension(File f) {
        String[] split = f.getName().split("\\.");
        return split[split.length - 1].toLowerCase();
    }
}
