/**
 * Interface EntryDAO:
 *
 * This interface defines methods that EntryDAO objects should implement
 *
 * author: Anuj More (atm140330)
 */

package ui.killemall.model;

public interface EntryDAO extends DAO {
        Entry addEntry(Entry entry);
        Entry updateEntry(Entry oldEntry, Entry newEntry);
        Entry deleteEntry(Entry entry);
        Entry updateEntryAt(int index, Entry entry);
        Entry deleteEntryAt(int index);
}
