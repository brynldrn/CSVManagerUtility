/**
 *
 */
package com.bryan.dumper;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.bryan.dumper.util.CSVUtilities;

/**
 * @author Bryan
 *
 */
public class CsvDumperApplication {

    /**
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        // TODO Auto-generated method stub
        CSVUtilities csvUtilities = new CSVUtilities();
        csvUtilities.backupCSV("C:\\var\\ec\\batch\\csv\\dtb_category.csv");
        /*csvUtilities.restoreCSV("C:\\var\\ec\\batch\\csv\\dtb_category.csv");*/
    }

}
