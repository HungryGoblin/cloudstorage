package databasetools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.CryptoHelper;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class Connection implements Closeable {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "Aviks131";

    private static final Logger LOG = LoggerFactory.getLogger(Connection.class);

    private java.sql.Connection connection;
    private Statement statement;

    public boolean isConnected() throws SQLException {
        return !(connection == null || connection.isClosed());
    }

    public boolean connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        return isConnected();
    }

    public static Connection createConnection() throws SQLException {
        return new Connection();
    }

    public Statement getStatement() throws SQLException {
        if (statement == null) statement = connection.createStatement();
        return statement;
    }

    public Connection() throws SQLException {
        connect();
        statement = connection.createStatement();
    }

    public boolean userExists(String login) throws SQLException {
        Statement statement = getStatement();
        ResultSet resultSet = statement.executeQuery(String.format(
                "SELECT FROM _user WHERE _user.login = '%s' LIMIT 1", login));
        return resultSet.next();
    }

    public boolean userLogin(String login, String password) throws SQLException {
        Statement statement = getStatement();
        ResultSet resultSet = statement.executeQuery(String.format(
                "SELECT FROM _user WHERE _user.login = '%s' AND _user.password = '%s' LIMIT 1",
                login, CryptoHelper.getChecksum(password)));
        return resultSet.next();
    }

    public boolean createUser(String login, String password, String name) throws SQLException {
        if (userExists(login)) throw new SQLDataException(String.format(
                "User '%s' exists", login));
        Statement statement = getStatement();
        statement.executeUpdate(String.format(
                "INSERT INTO _user (login, password, name) VALUES ('%s','%s','%s')",
                login, CryptoHelper.getChecksum(password), name));
        return true;
    }

    public static void main(String[] args) {

        try {
            Connection connection = new Connection();
            //connection.createUser("sozykin", "130161", "Andrey Goblin");
            System.out.println(connection.userLogin("sozykin", "123"));
            System.out.println(connection.userLogin("sozykin", "130161"));
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.error(e.getMessage() + " " + e.getSQLState() + " " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (isConnected()) connection.close();
        } catch (SQLException e) {
            LOG.error(e.getMessage() + " " + e.getSQLState() + " " + e.getStackTrace());
        }
    }
}
