package ui.killemall.model;

import java.util.ArrayList;

/**
 * Created by atm on 11/28/15.
 */
public class EntryController {
    static FlatFileEntryDAO dao;

    public EntryController() {
        dao = new FlatFileEntryDAO();
    }

    // Adds entry
    public static Entry addEntry(Entry entry) {
        return dao.addEntry(entry);
    }

    // Updates entry at a position
    public static Entry updateEntryAt(int i, Entry entry) {
        return dao.updateEntryAt(i, entry);
    }

    // Fetches all the entrys
    public static ArrayList<Entry> fetchAll() {
        return dao.fetchAll();
    }

    // Fetches a entry at position "index"
    public static Entry fetch(int index) {
        return dao.fetch(index);
    }

    // Deletes a entry at position "index"
    public static Entry delete(int index) {
        return dao.deleteEntryAt(index);
    }
}
