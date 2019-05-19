package DbHandler;

import Record.IRecord;
import Record.RecordNotFoundException;

import java.sql.SQLException;
import java.util.LinkedList;

public interface IHandler {
    public void addRecord(IRecord record);

    public IRecord getRecord(Integer id) throws SQLException, RecordNotFoundException;

    public void delRecord(Integer id) throws RecordNotFoundException;

    public void updateRecord(Integer id, String recordName, String recordText) throws RecordNotFoundException;

    public LinkedList<IRecord> getAllRecords();

    public LinkedList<IRecord> getAllRecords(String filterBy, Object value) throws IllegalAccessException;

    public void closeConnection();
}
