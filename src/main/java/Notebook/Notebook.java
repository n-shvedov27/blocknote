package Notebook;

import DbHandler.IHandler;
import Record.IRecord;
import Record.Record;
import Record.RecordNotFoundException;

import java.sql.SQLException;
import java.util.LinkedList;

public class Notebook {
    private IHandler handler;

    public Notebook(IHandler handler) {
        this.handler = handler;
    }

    public void closeHandler(){
        handler.closeConnection();
    }

    public void addRecord(String recordName, String recordText) {
        IRecord newRecord = new Record(recordName, recordText);
        handler.addRecord(newRecord);
    }


    public IRecord getRecord(int id) throws SQLException, RecordNotFoundException {
        return handler.getRecord(id);
    }

    public void delRecord(int id) throws RecordNotFoundException{
        handler.delRecord(id);
    }

    public void updateRecord(int id, String recordName, String recordText) throws RecordNotFoundException{
        handler.updateRecord(id, recordName, recordText);
    }

    public LinkedList<IRecord> getAllRecords(){
        return handler.getAllRecords();
    }

    public LinkedList<IRecord> getAllRecords(String sortBy, Object value) throws IllegalAccessException{
        return handler.getAllRecords(sortBy, value);
    }

}
