package DbHandler;

import Record.IRecord;
import Record.Record;
import Record.RecordNotFoundException;
import TestDbConnection.TestDbConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class DbHandlerTest {

    @Before
    public void initDb() {
        System.out.println("Init db");
        TestDbConnection testDbConnection = TestDbConnection.getInstance();
        try {
            testDbConnection.statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Record" +
                            "(id SERIAL PRIMARY KEY," +
                            "record_name VARCHAR (50) NOT NULL," +
                            "text VARCHAR (500) NOT NULL ," +
                            "post_date TIMESTAMP NOT NULL )");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void clearDb() {
        System.out.println("Clean db");
        TestDbConnection testDbConnection = TestDbConnection.getInstance();
        try {
            testDbConnection.statement.executeUpdate(
                    "DROP TABLE record");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void addRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name", "text"));
        TestDbConnection testDbConnection = TestDbConnection.getInstance();

        try {
            ResultSet resultSet = testDbConnection.statement.executeQuery("SELECT record_name, text, post_date FROM record");
            resultSet.next();
            String recordName = resultSet.getString("record_name");
            String recordText = resultSet.getString("text");
            java.util.Date postDate = resultSet.getTimestamp("post_date");
            assertNotEquals(resultSet, null);
            assertEquals(recordName, "name");
            assertEquals(recordText, "text");
            assertNotEquals(postDate, null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name", "text"));
        TestDbConnection testDbConnection = TestDbConnection.getInstance();

        try {
            ResultSet resultSet = testDbConnection.statement.executeQuery("SELECT record_name, text, post_date FROM record");
            resultSet.next();
            String recordName = resultSet.getString("record_name");
            String recordText = resultSet.getString("text");
            java.util.Date postDate = resultSet.getTimestamp("post_date");
            IRecord record = dbHandler.getRecord(1);
            assertEquals(recordName, record.getRecordName());
            assertEquals(recordText, record.getRecordText());
            assertEquals(postDate, record.getPostDate());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (RecordNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test(expected = RuntimeException.class)
    public void getNonExistRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        try {
            dbHandler.getRecord(1);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void delRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name", "text"));
        try {
            dbHandler.delRecord(1);
        } catch (RecordNotFoundException e) {
            System.out.println(e.getMessage());
        }
        TestDbConnection testDbConnection = TestDbConnection.getInstance();

        try {
            ResultSet resultSet = testDbConnection.statement.executeQuery("SELECT record_name, text, post_date FROM record");
            assertFalse(resultSet.next());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void delNonExistsRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        try {
            dbHandler.delRecord(1);
        } catch (RecordNotFoundException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void updateRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name", "text"));

        try {
            dbHandler.updateRecord(1, "newName", "newText");
        } catch (RecordNotFoundException e) {
            System.out.println(e.getMessage());
        }
        TestDbConnection testDbConnection = TestDbConnection.getInstance();

        try {
            ResultSet resultSet = testDbConnection.statement.executeQuery("SELECT record_name, text, post_date FROM record");
            resultSet.next();
            String recordName = resultSet.getString("record_name");
            String recordText = resultSet.getString("text");
            java.util.Date postDate = resultSet.getTimestamp("post_date");
            assertNotEquals(resultSet, null);
            assertEquals(recordName, "newName");
            assertEquals(recordText, "newText");
            assertNotEquals(postDate, null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void updateNonExistRecord() {
        DbHandler dbHandler = DbHandler.getInstance();
        try {
            dbHandler.updateRecord(1, "newName", "newText");
        } catch (RecordNotFoundException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void getAllRecords() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name1", "text1"));
        dbHandler.addRecord(new Record("name2", "text2"));
        LinkedList<IRecord> records = dbHandler.getAllRecords();
        assertEquals(records.size(), 2);
        assertEquals(records.get(0).getRecordName(), "name1");
        assertEquals(records.get(1).getRecordName(), "name2");
        assertEquals(records.get(0).getRecordText(), "text1");
        assertEquals(records.get(1).getRecordText(), "text2");
    }

    @Test
    public void getAllRecordsWithFilters() {
        DbHandler dbHandler = DbHandler.getInstance();
        dbHandler.switchConnection2TestDb();

        dbHandler.addRecord(new Record("name1", "text1"));
        dbHandler.addRecord(new Record("name2", "text2"));
        try {
            LinkedList<IRecord> records = dbHandler.getAllRecords("recordName", "name1");
            assertEquals(records.size(), 1);
            assertEquals(records.get(0).getRecordName(), "name1");
            assertEquals(records.get(0).getRecordText(), "text1");
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test(expected = RuntimeException.class)
    public void getAllRecordsWithWrongFilters() {
        DbHandler dbHandler = DbHandler.getInstance();
        try {
            dbHandler.getAllRecords("notExistField", "value");
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}