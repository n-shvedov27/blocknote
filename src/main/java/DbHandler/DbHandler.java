package DbHandler;

import Record.IRecord;
import Record.Record;
import Record.RecordNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class DbHandler implements IHandler {
    private static volatile DbHandler instance;

    public static DbHandler getInstance() {
        DbHandler localDbHandler = instance;
        if (localDbHandler == null) {
            synchronized (DbHandler.class) {
                localDbHandler = instance;
                if (localDbHandler == null) {
                    instance = localDbHandler = new DbHandler();
                }
            }
        }
        return localDbHandler;
    }

    private String userName = "postgres";
    private String password = "q";
    private String connectionUrl = "jdbc:postgresql://localhost:5432/java";
    private String testConnectionUrl = "jdbc:postgresql://localhost:5432/test";
    private Connection connection;
    private Statement statement;

    private DbHandler() {
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
            statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Record" +
                            "(id SERIAL PRIMARY KEY," +
                            "record_name VARCHAR (50) NOT NULL," +
                            "text VARCHAR (500) NOT NULL ," +
                            "post_date TIMESTAMP NOT NULL )");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void switchConnection2TestDb() {
        try {
            connection = DriverManager.getConnection(testConnectionUrl, userName, password);
            statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Record" +
                            "(id SERIAL PRIMARY KEY," +
                            "record_name VARCHAR (50) NOT NULL," +
                            "text VARCHAR (500) NOT NULL ," +
                            "post_date TIMESTAMP NOT NULL )");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void closeConnection(){
        try {
            connection.close();
            statement.cancel();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void addRecord(IRecord record) {

        String recordName = record.getRecordName();
        String recordText = record.getRecordText();
        java.util.Date recordDate = record.getPostDate();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO record (record_name, text, post_date)" +
                            "VALUES (?, ?, ?)");
            preparedStatement.setString(1, recordName);
            preparedStatement.setString(2, recordText);
            preparedStatement.setTimestamp(3, new Timestamp(recordDate.getTime()));
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public IRecord getRecord(Integer id) throws SQLException, RecordNotFoundException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT record_name, text, post_date FROM record " +
                        "WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            throw new RecordNotFoundException();
        }

        String recordName = resultSet.getString("record_name");
        String recordText = resultSet.getString("text");
        java.util.Date postDate = resultSet.getTimestamp("post_date");
        return new Record(recordName, recordText, postDate);
    }

    public void delRecord(Integer id) throws RecordNotFoundException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM record WHERE id = ?");
            preparedStatement.setInt(1, id);
            int r = preparedStatement.executeUpdate();
            if (r == 0)
                throw new RecordNotFoundException();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void updateRecord(Integer id, String recordName, String recordText) throws RecordNotFoundException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE record SET record_name = ?, text = ? WHERE id = ?");
            preparedStatement.setString(1, recordName);
            preparedStatement.setString(2, recordText);
            preparedStatement.setInt(3, id);
            int r = preparedStatement.executeUpdate();
            if (r == 0) {
                throw new RecordNotFoundException();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private LinkedList<IRecord> getAllRecordsFromDb() {
        LinkedList<IRecord> records = new LinkedList<IRecord>();
        try {
            ResultSet r = statement.executeQuery("SELECT record_name,text,post_date FROM record");
            while (r.next()) {
                String recordName = r.getString("record_name");
                String recordText = r.getString("text");
                java.util.Date postDate = r.getTimestamp("post_date");
                records.add(new Record(recordName, recordText, postDate));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return records;
    }


    public LinkedList<IRecord> getAllRecords() {
        return getAllRecordsFromDb();
    }

    @Override
    public LinkedList<IRecord> getAllRecords(String filterBy, Object value) throws IllegalAccessException {
        ArrayList<String> fieldNames = new ArrayList<>();

        for (Field field : Record.class.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }

        if (!fieldNames.contains(filterBy)) {
            throw new IllegalAccessException();
        }
        LinkedList<IRecord> records = getAllRecordsFromDb();


        records.removeIf(record -> {
            for (Field field : Record.class.getDeclaredFields()) {
                if (field.getName().equals(filterBy)) {
                    try {
                        if (Modifier.isPrivate(field.getModifiers())) {
                            field.setAccessible(true);
                        }
                        return !field.get(record).equals(value);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
            throw new RuntimeException("Field not found");
        });
        return records;
    }
}
