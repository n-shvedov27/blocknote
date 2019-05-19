package TestDbConnection;

import DbHandler.DbHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDbConnection {
    private String userName = "postgres";
    private String password = "q";
    private String connectionUrl = "jdbc:postgresql://localhost:5432/test";
    private static volatile TestDbConnection instance;
    public Connection connection;
    public Statement statement;

    public static TestDbConnection getInstance() {
        TestDbConnection localDbHandler = instance;
        if (localDbHandler == null) {
            synchronized (DbHandler.class) {
                localDbHandler = instance;
                if (localDbHandler == null) {
                    instance = localDbHandler = new TestDbConnection();
                }
            }
        }
        return localDbHandler;
    }


    private TestDbConnection() {
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
            statement = connection.createStatement();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
