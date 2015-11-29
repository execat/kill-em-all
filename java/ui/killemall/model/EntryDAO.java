package ui.killemall.model;

/**
 * Created by atm on 11/28/15.
 */
public interface EntryDAO extends DAO {
        Entry addEntry(Entry contact);
        Entry updateEntry(Entry oldEntry, Entry newEntry);
        Entry deleteEntry(Entry contact);
        Entry updateEntryAt(int index, Entry contact);
        Entry deleteEntryAt(int index);
}
