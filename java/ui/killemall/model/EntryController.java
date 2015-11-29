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

    // Adds contact
    public static Entry addEntry(Entry contact) {
        return dao.addEntry(contact);
    }

    // Updates contact at a position
    public static Entry updateEntryAt(int i, Entry contact) {
        return dao.updateEntryAt(i, contact);
    }

    // Fetches all the contacts
    public static ArrayList<Entry> fetchAll() {
        return dao.fetchAll();
    }

    // Fetches a contact at position "index"
    public static Entry fetch(int index) {
        return dao.fetch(index);
    }

    // Deletes a contact at position "index"
    public static Entry delete(int index) {
        return dao.deleteEntryAt(index);
    }
}
