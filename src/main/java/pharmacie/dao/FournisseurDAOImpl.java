package pharmacie.dao;

import pharmacie.model.Fournisseur;
import pharmacie.exception.DAOException;
import pharmacie.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAOImpl implements FournisseurDAO {
    @Override
    public void save(Fournisseur fournisseur) throws DAOException {
        String sql = "INSERT INTO fornisseurs (nom, adresse, telephone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()) {
                fournisseur.setId(keys.getInt(1));
            } else {
                throw new DAOException("Erreur lors de la récupération de l'ID du fornissuer inséré");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'insertion du fornissuer", e);
        }
    }
    @Override
    public Fournisseur findById(int id) throws DAOException {
        String sql = "SELECT * FROM fornisseurs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            // Set the ID parameter
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery();) {
            // If a fournisseur is found, create and return a Fournisseur object
                if (rs.next()) {
                    Fournisseur fournisseur = new Fournisseur(
                        id,
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                    );
                    return fournisseur;
                } else {
                    return null; // No fournisseur found with the given ID
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du fornissuer par ID", e);
        }
    }
    @Override
    public List<Fournisseur> findAll() throws DAOException {
        String sql = "SELECT * FROM fornisseurs";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Fournisseur> fournisseurs = new ArrayList<>();
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("adresse"),
                    rs.getString("telephone")
                );
                fournisseurs.add(fournisseur);
            }
            return fournisseurs;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de tous les fornisseurs", e);
        }
    }
    @Override
    public void update(Fournisseur fournisseur) throws DAOException {
        String sql = "UPDATE fornisseurs SET nom = ?, adresse = ?, telephone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getAdresse());
            stmt.setString(3, fournisseur.getTelephone());
            stmt.setInt(4, fournisseur.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour du fornissuer", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM fornisseurs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du fornissuer", e);
        }
    }
}