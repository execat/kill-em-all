package ui.killemall.model;

/**
 * Created by atm on 11/28/15.
 */

import android.location.Location;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class FlatFileEntryDAO:
 *
 * Disk access object class for writing contacts to flat file
 * This is where all the writing, reading, and updating of records happens.
 *
 * author: Anuj More (atm140330)
 */
public class FlatFileEntryDAO implements EntryDAO, Comparator<Entry> {
    // To read the data file
    private Reader reader;

    // Separator of data
    private String separator = "::";    // Use constants

    // Static headers
    private ArrayList<String> flatFileHeaders;

    // Current state of the data
    private ArrayList<Entry> data;

    // File that will store the data
    private String dataFilePath;
    private File dataFile;

    // File that will store the deleted data // Implement later
    private String backupFilePath;
    private File backupFile;

    /**
     * Constructor
     *
     * Sets up FlatFileEntryDAO object
     * Creates data and backup file if they don't exist
     * Reads the data file into "data"
     */

    public FlatFileEntryDAO() {
        flatFileHeaders = new ArrayList<String>();
        data = new ArrayList<Entry>();
        String rootPath = "/storage/emulated/0/Android/data/com.example.android.camera2basic/files/";

        // Assures data file is created
        String dataPath = "data.txt";
        dataFile = new File(rootPath, dataPath);
        assureFileExists(dataFile);

        // Assures backup file is created
        String backupPath = "backup.txt";
        backupFile = new File(rootPath, backupPath);
        assureFileExists(backupFile);

        data = prefetch();

        try {
            String path = Environment.getExternalStorageDirectory() + dataPath;
            reader = new FileReader(path);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * close
     *
     * Closes all the connections
     * @throws Exception
     */

    @Override
    public void close() throws Exception {
        data = null;
        dataFile = null;
        backupFile = null;
        reader = null;
    }

    /**
     * prefetch
     *
     * Does the initial fetch operation
     *
     * @return
     */
    public ArrayList<Entry> prefetch() {
        ArrayList<Entry> all = new ArrayList<Entry>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(separator);

                // Create Entry object
                File file = new File(strs[0]);
                String timestamp = strs[1];
                Location location = new Location("");
                location.setLongitude(Double.parseDouble(strs[2]));
                location.setLatitude(Double.parseDouble(strs[3]));
                EntryStatus status = EntryStatus.valueOf(strs[4]);

                // Add Entry object
                Entry tmp = new Entry(file, timestamp, location, status);
                all.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            return new ArrayList<Entry>();
        }

        System.out.println("--------------------------> Prefetch " + all);
        return all;
    }

    /**
     * fetchAll
     *
     * Fetches all the records from the datafile and returns an ArrayList of Entrys
     * @return
     */

    public ArrayList<Entry> fetchAll() {
        return sort();
    }


    /**
     * sort
     *
     * Sorts the data list by first name
     * @return
     */
    private ArrayList<Entry> sort() {
        Collections.sort(data, new Comparator<Entry>() {
            public int compare(Entry lhs, Entry rhs) {
                return lhs.getFile().compareTo(rhs.getFile());
            }
        });
        return data;
    }

    /**
     * fetch
     *
     * Fetches a Entry at a given position
     *
     * @param index
     * @return
     */
    public Entry fetch(int index) {
        return sort().get(index);
    }

    /**
     * assureFileExists
     *
     * Assures that a file with a given path exists
     * Creates an empty file if the path does not exist
     *
     * @return
     */
    private boolean assureFileExists(File f) {
        if(!f.exists()) {
            return createFile(f);
        }
        return true;
    }

    /**
     * createFile
     *
     * Creates the file if the file doesn't exist
     *
     * @param file
     * @return
     */

    private boolean createFile(File file) {
        try {
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong while trying to create the file: " + file.getPath());
            return false;
        }
        return true;
    }

    /**
     * writeToFile
     *
     * Writes a contacts array to a file
     *
     * This method reads contents of File in a temp array and writes the CSV-like notation of Entry[] to file
     *
     * @param contacts
     * @param file
     * @return
     */
    private boolean writeToFile(ArrayList<Entry> contacts, File file) {
        System.out.println("CALLING WRITE TO FILE: \n\n" + contacts);
        sort();
        createFile(file);

        FileWriter fw;
        try {
            // These should ideally be two try-catch blocks
            fw = new FileWriter(file);

            for (Entry contact: contacts) {
                fw.write(contact.toString() + "\n");
            }

            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * verifyValidity
     *
     * Impelemnt later, currently does nothing
     *
     * @param contact
     * @return
     */
    private boolean verifyValidity(Entry contact, ArrayList<Entry> excludeList) {
        return true;
    }

    /**
     * addEntry
     *
     * Adds the passed contact to the end of the list
     *
     * @param contact
     * @return
     */
    @Override
    public Entry addEntry(Entry contact) {
        // Check
        if (!verifyValidity(contact, null)) {
            return null;
        }

        // Add
        data.add(contact);
        writeToFile(data, dataFile);
        return contact;
    }

    /**
     * updateEntry
     *
     * Updates an oldEntry to a new one subject to validity criteria
     *
     * @param oldEntry
     * @param newEntry
     * @return
     */
    @Override
    public Entry updateEntry(Entry oldEntry, Entry newEntry) {
        // Check
        ArrayList<Entry> excludeList = new ArrayList<Entry>();
        excludeList.add(oldEntry);
        if (!verifyValidity(newEntry, excludeList)) {
            return null;
        }

        // Update
        int index = data.indexOf(oldEntry);
        deleteEntry(oldEntry);
        data.add(index, newEntry);
        writeToFile(data, dataFile);
        return newEntry;
    }

    /**
     * deleteEntry
     *
     * Deletes a passed contact
     *
     * @param contact
     * @return
     */
    @Override
    public Entry deleteEntry(Entry contact) {
        data.remove(contact);
        writeToFile(data, dataFile);
        return contact;
    }

    /**
     * updateEntryAt
     *
     * Updates a contact to the passed contact at the given index subject to validation preconditions
     *
     * @param index
     * @param contact
     * @return
     */
    @Override
    public Entry updateEntryAt(int index, Entry contact) {
        Entry oldEntry = data.get(index);
        updateEntry(oldEntry, contact);
        writeToFile(data, dataFile);
        return contact;
    }

    /**
     * deleteEntryAt
     *
     * Deletes a contact at the given position
     *
     * @param index
     * @return
     */
    @Override
    public Entry deleteEntryAt(int index) {
        Entry contact = data.get(index);
        deleteEntry(contact);
        writeToFile(data, dataFile);
        return contact;

        /* Alternative
        data.remove(index)
        writeToFile(data, dataFile);
        return contact;
         */
    }

    @Override
    public int compare(Entry lhs, Entry rhs) {
        return lhs.getStatus().compareTo(rhs.getStatus());
    }
}

