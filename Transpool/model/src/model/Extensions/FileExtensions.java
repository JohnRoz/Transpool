package model.Extensions;

import java.io.File;

public class FileExtensions {
    public static String getFileExtension(File f) {
        return f.getName().split("\\.")[1].toLowerCase();
    }
}
