package com.bryan.dumper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * CSV Utilities Public methods are chainable.
 *
 * @author Bryan
 */
public class CSVUtilities {

    private final Date curDate;
    private final long timeStamp;
    private static String DOCUMENTS_FOLDER = System.getProperty("user.home") + File.separator
            + "Documents\\TSC Backups\\";

    public CSVUtilities() {
        this.curDate = new Date();
        this.timeStamp = curDate.getTime();
    }

    /**
     * PUBLIC ACCESSIBLE METHOD Use this to backup your file to the DESTINATION
     * FOLDER specified. This returns the instance of the method so you can
     * method chain. Enjoy :)
     *
     * @param source_file
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public CSVUtilities backupCSV(String source_file) throws IOException, FileNotFoundException {

        if (!new File(DOCUMENTS_FOLDER).exists()) {
            new File(DOCUMENTS_FOLDER).mkdirs();
        }

        File source = new File(source_file);
        File dest = new File(DOCUMENTS_FOLDER + source.getName().replaceAll(".csv", "") + "#" + timeStamp + ".csv");

        copyFileUsingStream(source, dest);
        clearCSV(source_file);

        System.out.println("CSV BACKUP: " + dest.getName() + " successfully backed up! Destination folder: "
                + DOCUMENTS_FOLDER);
        return this;
    }

    /**
     * PUBLIC ACCESSIBLE METHOD Use this to clear the contents of the CSV
     * whenever you like. This returns the instance of the method so you can
     * method chain.
     *
     * @param source_file
     * @return
     * @throws IOException
     */
    public CSVUtilities clearCSV(String source_file) throws IOException {

        File source = new File(source_file);

        if (source.delete() && source.createNewFile()) {
            System.out.println("CSV CLEAR: File refreshed. Contents are now blank.");
        }

        return this;
    }

    /**
     * PUBLIC ACCESSIBLE METHOD Use this to restore the CSV you want. IMPORTANT
     * REMINDER: Pass the whole path of the original file please! This returns
     * the same class instance so you can still chain if you like.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public CSVUtilities restoreCSV(final String filePath) throws IOException {
        System.out.println("Filepath: " + filePath);
        File dir = new File(DOCUMENTS_FOLDER);
        File origFilePath = new File(filePath);
        final String fileName = origFilePath.getName();

        // We'll search the matched files later for this exact value.
        long latestBackupAvailable = 0;

        File[] matches = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(fileName.replace(".csv", "")) && name.endsWith(".csv");
            }
        });

        // Search the array to find the latest value
        for (File found : matches) {
            String lastChars = getLastnCharacters(found.getName(), 17);
            String dateInStr = lastChars.replace(".csv", "");

            // Compare each UNIX time to get the most recent value
            if (Long.parseLong(dateInStr) > latestBackupAvailable) {
                latestBackupAvailable = Long.parseLong(dateInStr);
            }
        }

        // Finally, latest record is retrieved.
        // Restore it here
        final String latestUnixTime = Long.toString(latestBackupAvailable);
        File[] latestBackup = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(fileName.replace(".csv", "") + "#" + latestUnixTime) && name.endsWith(".csv");
            }
        });

        File src = null;

        for (File f : latestBackup) {
            src = f;
        }

        copyFileUsingStream(src, origFilePath);
        System.out.println("CSV RESTORE: " + filePath + " successfully restored!");
        return this;
    }

    /**
     * PUBLIC ACCESSIBLE METHOD Use this to destroy the CSV you want. IMPORTANT
     * REMINDER: Pass the whole path of the original file please! This returns
     * the same class instance so you can still chain if you like.
     *
     * @param filePath
     * @return
     */
    public CSVUtilities destroyCSV(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
            System.out.println("CSV DESTROY: " + filePath + " successfully removed.");
        } else {
            System.err.println("CSV DESTROY: " + filePath + " failed to remove.");
        }
        return this;
    }

    /**
     * PUBLIC ACCESSIBLE METHOD Use this to set permissions on the CSV you want. IMPORTANT
     * REMINDER: Pass the whole path of the original file please! This returns
     * the same class instance so you can still chain if you like.
     *
     * @param filePath
     * @return
     */
    public CSVUtilities lockCSV(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            file.setExecutable(false);
            file.setReadable(false);
            file.setWritable(false);
            System.out.println("CSV LOCK: " + filePath + " successfully locked.");
          } else {
            System.err.println("CSV LOCK: " + filePath + " failed to lock.");
          }
          return this;
    }

    /**
     * Use Stream to avoid any dependencies Please provide the SOURCE and
     * DESTINATION with BOTH file extensions intact (e.g. .csv/.txt).
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * Use this method for your own good. This is a substring alternative. It
     * gives you much freedom on selecting parts of the string, especially when
     * retrieving the last characters.
     *
     * @param inputString
     * @param subStringLength
     * @return
     */
    private String getLastnCharacters(String inputString, int subStringLength) {
        int length = inputString.length();
        if (length <= subStringLength) {
            return inputString;
        }
        int startIndex = length - subStringLength;
        return inputString.substring(startIndex);
    }
}
