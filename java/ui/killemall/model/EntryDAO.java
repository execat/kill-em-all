package ui.killemall.model;

/**
 * Created by atm on 11/28/15.
 */
public interface EntryDAO extends DAO {
        Entry addEntry(Entry entry);
        Entry updateEntry(Entry oldEntry, Entry newEntry);
        Entry deleteEntry(Entry entry);
        Entry updateEntryAt(int index, Entry entry);
        Entry deleteEntryAt(int index);
}
