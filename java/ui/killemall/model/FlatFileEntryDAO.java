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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Class FlatFileEntryDAO:
 *
 * Disk access object class for writing entrys to flat file
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
            String path = rootPath + dataPath;
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

    public ArrayList<Entry> fetchAlive() {
        return aliveSort();
    }

    public ArrayList<Entry> fetchDead() {
        return deadSort();
    }


    /**
     * sort
     *
     * Sorts the data list by file name
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

    private ArrayList<Entry> aliveSort() {
        Collections.sort(data, new Comparator<Entry>() {
            public int compare(Entry lhs, Entry rhs) {
                return lhs.getFile().compareTo(rhs.getFile());
            }
        });

        ArrayList<Entry> list = new ArrayList();
        Iterator iterator = data.iterator();
        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if(entry.getStatus() == EntryStatus.ALIVE) {
                list.add(entry);
            }
        }

        return list;
    }

    private ArrayList<Entry> deadSort() {
        Collections.sort(data, new Comparator<Entry>() {
            public int compare(Entry lhs, Entry rhs) {
                return lhs.getFile().compareTo(rhs.getFile());
            }
        });

        ArrayList<Entry> list = new ArrayList();
        Iterator iterator = data.iterator();
        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if(entry.getStatus() == EntryStatus.DEAD) {
                list.add(entry);
            }
        }

        return list;
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
     * Writes a entrys array to a file
     *
     * This method reads contents of File in a temp array and writes the CSV-like notation of Entry[] to file
     *
     * @param entrys
     * @param file
     * @return
     */
    private boolean writeToFile(ArrayList<Entry> entrys, File file) {
        System.out.println("CALLING WRITE TO FILE: \n\n" + entrys);
        sort();
        createFile(file);

        FileWriter fw;
        try {
            // These should ideally be two try-catch blocks
            fw = new FileWriter(file);

            for (Entry entry: entrys) {
                fw.write(entry.toString() + "\n");
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
     * @param entry
     * @return
     */
    private boolean verifyValidity(Entry entry, ArrayList<Entry> excludeList) {
        return true;
    }

    /**
     * addEntry
     *
     * Adds the passed entry to the end of the list
     *
     * @param entry
     * @return
     */
    @Override
    public Entry addEntry(Entry entry) {
        // Check
        if (!verifyValidity(entry, null)) {
            return null;
        }

        // Add
        data.add(entry);
        writeToFile(data, dataFile);
        return entry;
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
     * Deletes a passed entry
     *
     * @param entry
     * @return
     */
    @Override
    public Entry deleteEntry(Entry entry) {
        data.remove(entry);
        writeToFile(data, dataFile);
        return entry;
    }

    /**
     * updateEntryAt
     *
     * Updates a entry to the passed entry at the given index subject to validation preconditions
     *
     * @param index
     * @param entry
     * @return
     */
    @Override
    public Entry updateEntryAt(int index, Entry entry) {
        Entry oldEntry = data.get(index);
        updateEntry(oldEntry, entry);
        writeToFile(data, dataFile);
        return entry;
    }

    /**
     * deleteEntryAt
     *
     * Deletes a entry at the given position
     *
     * @param index
     * @return
     */
    @Override
    public Entry deleteEntryAt(int index) {
        Entry entry = data.get(index);
        deleteEntry(entry);
        writeToFile(data, dataFile);
        return entry;

        /* Alternative
        data.remove(index)
        writeToFile(data, dataFile);
        return entry;
         */
    }

    @Override
    public int compare(Entry lhs, Entry rhs) {
        return lhs.getStatus().compareTo(rhs.getStatus());
    }
}
