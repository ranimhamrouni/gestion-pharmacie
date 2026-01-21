package pharmacie.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pharmacie.exception.DAOException;

public class DatabaseConnection {

    // Database URL: replace 'pharmacie_db' with your actual database name
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacie_db";

    // MySQL username and password - update these to match your setup
    private static final String USER = "pharmacie_user";
    private static final String PASSWORD = "password";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    /**
     * Returns a Connection object to the database.
     * 
     * @return Connection object
     * @throws DAOException if connection fails
     */
    public static Connection getConnection() throws DAOException {
        try {
            // Try to get a connection using JDBC DriverManager
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Wrap SQLException in a custom DAOException
            throw new DAOException("Erreur de connexion à la base de données", e);
        }
    }
}
