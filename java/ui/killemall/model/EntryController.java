/**
 * Class EntryController:
 *
 * This class is the controller to interact with the data store
 *
 * author: Anuj More (atm140330)
 */

package ui.killemall.model;

import java.util.ArrayList;

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

    // Fetches entries that have the status ALIVE
    public ArrayList<Entry> fetchAlive() {
        return dao.fetchAlive();
    }

    // Fetches entries that have the status DEAD
    public ArrayList<Entry> fetchDead() {
        return dao.fetchDead();
    }
}
