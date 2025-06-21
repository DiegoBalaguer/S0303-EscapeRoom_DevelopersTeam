package utils;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

/**
 * PROGRAM: DirectoryUtils
 * AUTHOR:  Diego Balaguer
 * DATE:    10/05/2025
 */

public class DirectoryUtils {

    public static String formatLineShow(String initLine, boolean isInit, boolean isLast, File file) {

        return StringUtils.formatToChars(
                String.format("%s%s%s %s",
                        initLine,
                        (isInit ? "" : (isLast ? "└── " : "├── ")),
                        (file.isDirectory() ? "[D]" : "[F]"),
                        (isInit ? file.getAbsoluteFile() : file.getName())
                ), 90)
                + StringUtils.getDateFormatUSA(new Date(file.lastModified()));
    }

    public static String getSortedOneDirectory(File fileInput) {
        String message = DirectoryUtils.formatLineShow("", true,false, fileInput) + System.lineSeparator();
        message += makeListSortedOneDirectory(fileInput);

        return message;
    }

    private static String makeListSortedOneDirectory(File fileInput) {
        StringBuilder message = new StringBuilder();
        File[] files = fileInput.listFiles();
        if (files == null || files.length == 0) return "";
        Arrays.sort(files);

        for (int i = 0; i < files.length; i++) {
            boolean isLast = (i == files.length - 1);
            File file = files[i];
            message.append(DirectoryUtils.formatLineShow("", false, isLast, file));
            message.append(System.lineSeparator());
        }
        return message.toString();
    }

    public static String getSortedDirectory(File fileInput) {
        String message = DirectoryUtils.formatLineShow("", true,false, fileInput) + System.lineSeparator();
        message += makeListSortedDirectory(fileInput, "");

        return message;
    }

    private static String makeListSortedDirectory(File fileInput, String previous) {
        StringBuilder message = new StringBuilder();
        File[] files = fileInput.listFiles();
        if (files == null || files.length == 0) return "";
        Arrays.sort(files);

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            boolean isLast = (i == files.length - 1);
            message.append(DirectoryUtils.formatLineShow(previous, false, isLast, file));
            message.append(System.lineSeparator());
            if (file.isDirectory()) {
                message.append(makeListSortedDirectory(file, previous + (isLast ? "    " : "│   ")));
            }
        }
        return message.toString();
    }
}
