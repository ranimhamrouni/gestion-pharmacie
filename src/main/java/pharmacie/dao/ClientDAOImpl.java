package pharmacie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pharmacie.exception.DAOException;
import pharmacie.model.Client;
import pharmacie.util.DatabaseConnection;

public class ClientDAOImpl implements ClientDAO {
    @Override
    public void save(Client client) throws DAOException {
        String sql = "INSERT INTO clients (nom, adresse, telephone) VALUES (?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getAdresse());
            stmt.setString(3, client.getTelephone());

            stmt.executeUpdate();
            
            ResultSet keys = stmt.getGeneratedKeys();

            if(keys.next()) {
                client.setId(keys.getInt(1));
            } else {
                throw new DAOException("Erreur lors de la récupération de l'ID du client inséré");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'insertion du client", e);
        }
    }

    @Override
    public Client findById(int id) throws DAOException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            // Set the ID parameter
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery();) {
            // If a client is found, create and return a Client object
                if (rs.next()) {
                    Client client = new Client(
                        id,
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                    );
                    return client;
                } else {
                    return null; // No client found with the given ID
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du client par ID", e);
        }
    }

    @Override
    public List<Client> findAll() throws DAOException {
        String sql = "SELECT * FROM clients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("adresse"),
                    rs.getString("telephone")
                );
                clients.add(client);
            }
            return clients;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de tous les clients", e);
        }
    }

    @Override
    public void update(Client client) throws DAOException {
        String sql = "UPDATE clients SET nom = ?, adresse = ?, telephone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getAdresse());
            stmt.setString(3, client.getTelephone());
            stmt.setInt(4, client.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour du client", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du client", e);
        }
    }
}
