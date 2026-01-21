package pharmacie.dao;

import pharmacie.model.Produit;
import pharmacie.exception.DAOException;
import pharmacie.util.DatabaseConnection;
import java.util.List;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProduitDAOImpl implements ProduitDAO {

    @Override
    public void save(Produit produit) throws DAOException {
        String sql = "INSERT INTO produits (nom, prixUnitaire, quantiteStock, description, seuilMinimal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the PreparedStatement
            stmt.setString( 1, produit.getNom());
            stmt.setDouble(2, produit.getPrixUnitaire());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setString(4, produit.getDescription());
            stmt.setInt(5, produit.getSeuilMinimal());

            // Execute the insert
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();

            if(keys.next()) {
                produit.setId(keys.getInt(1));
            } else {
                throw new DAOException("Erreur lors de la récupération de l'ID du produit inséré");
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'insertion du produit", e);
        }
    }

    @Override
    public Produit findById(int id) throws DAOException {
        String sql = "SELECT * FROM produits WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            // Set the ID parameter
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery();) {
            // If a product is found, create and return a Produit object
                if (rs.next()) {
                    Produit produit = new Produit(
                        id,
                        rs.getString("nom"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("quantiteStock"),
                        rs.getString("description"),
                        rs.getInt("seuilMinimal")
                    );
                    return produit;
                } else {
                    return null; // No product found with the given ID
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du produit par ID", e);
        }
    }

    @Override
    public List<Produit> findAll() throws DAOException {
        String sql = "SELECT * FROM produits";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

            List<Produit> produits = new java.util.ArrayList<>();

            while (rs.next()) {
                Produit produit = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getDouble("prixUnitaire"),
                    rs.getInt("quantiteStock"),
                    rs.getString("description"),
                    rs.getInt("seuilMinimal")
                );
                produits.add(produit);
            }
            return produits;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de tous les produits", e);
        }
    }

    @Override
    public void update(Produit produit) throws DAOException {
        String sql = "UPDATE produits SET nom = ?, prixUnitaire = ?, quantiteStock = ?, description = ?, seuilMinimal = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the PreparedStatement
            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrixUnitaire());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setString(4, produit.getDescription());
            stmt.setInt(5, produit.getSeuilMinimal());
            stmt.setInt(6, produit.getId());

            // Execute the update
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour du produit", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM produits WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);

            // Execute the delete
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du produit", e);
        }
    }
    
}
    // You will implement other methods later...

