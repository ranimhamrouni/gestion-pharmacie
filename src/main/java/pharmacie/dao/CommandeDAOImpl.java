package pharmacie.dao;

import pharmacie.model.Commande;
import pharmacie.exception.DAOException;
import pharmacie.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandeDAOImpl implements CommandeDAO {

    @Override
    public void save(Commande commande) throws DAOException {
        String insertCommandeSQL = "INSERT INTO commandes (fournisseur_id, date_commande, statut) VALUES (?, ?, ?)";
        String insertCommandeProduitSQL = "INSERT INTO commandes_produits (commande_id, produit_id, quantite) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement commandeStmt = conn.prepareStatement(insertCommandeSQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement commandeProduitStmt = conn.prepareStatement(insertCommandeProduitSQL)) {

                // Insert into commandes
                commandeStmt.setInt(1, commande.getFournisseurId());
                commandeStmt.setString(2, commande.getDate());
                commandeStmt.setString(3, commande.getStatut());
                commandeStmt.executeUpdate();

                ResultSet generatedKeys = commandeStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int commandeId = generatedKeys.getInt(1);

                    // Insert all produits linked to the commande
                    for (Map.Entry<Integer, Integer> entry : commande.getProduitsCommandes().entrySet()) {
                        int produitId = entry.getKey();
                        int quantite = entry.getValue();

                        commandeProduitStmt.setInt(1, commandeId);
                        commandeProduitStmt.setInt(2, produitId);
                        commandeProduitStmt.setInt(3, quantite);
                        commandeProduitStmt.addBatch();
                    }
                    commandeProduitStmt.executeBatch();
                } else {
                    throw new DAOException("Erreur lors de la récupération de l'ID de la commande insérée.");
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'insertion de la commande", e);
        }
    }

    @Override
    public Commande findById(int id) throws DAOException {
        String selectCommandeSQL = "SELECT * FROM commandes WHERE id = ?";
        String selectProduitsSQL = "SELECT produit_id, quantite FROM commandes_produits WHERE commande_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement commandeStmt = conn.prepareStatement(selectCommandeSQL);
             PreparedStatement produitsStmt = conn.prepareStatement(selectProduitsSQL)) {

            commandeStmt.setInt(1, id);
            try (ResultSet rs = commandeStmt.executeQuery()) {
                if (rs.next()) {
                    int fournisseurId = rs.getInt("fournisseur_id");
                    String date = rs.getString("date_commande");
                    String statut = rs.getString("statut");

                    // Get produits and quantities
                    produitsStmt.setInt(1, id);
                    Map<Integer, Integer> produitsCommandes = new java.util.HashMap<>();
                    try (ResultSet rsProd = produitsStmt.executeQuery()) {
                        while (rsProd.next()) {
                            produitsCommandes.put(rsProd.getInt("produit_id"), rsProd.getInt("quantite"));
                        }
                    }

                    return new Commande(id, fournisseurId, date, statut, produitsCommandes);
                } else {
                    return null; // Not found
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche de la commande par ID", e);
        }
    }

    @Override
    public List<Commande> findAll() throws DAOException {
        String selectAllCommandeSQL = "SELECT * FROM commandes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectAllCommandeSQL);
             ResultSet rs = stmt.executeQuery()) {

            List<Commande> commandes = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                int fournisseurId = rs.getInt("fournisseur_id");
                String date = rs.getString("date_commande");
                String statut = rs.getString("statut");

                // For each commande, get products and quantities
                String selectProduitsSQL = "SELECT produit_id, quantite FROM commandes_produits WHERE commande_id = ?";
                try (PreparedStatement produitsStmt = conn.prepareStatement(selectProduitsSQL)) {
                    produitsStmt.setInt(1, id);
                    Map<Integer, Integer> produitsCommandes = new java.util.HashMap<>();
                    try (ResultSet rsProd = produitsStmt.executeQuery()) {
                        while (rsProd.next()) {
                            produitsCommandes.put(rsProd.getInt("produit_id"), rsProd.getInt("quantite"));
                        }
                    }
                    commandes.add(new Commande(id, fournisseurId, date, statut, produitsCommandes));
                }
            }
            return commandes;
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de toutes les commandes", e);
        }
    }

    @Override
    public void update(Commande commande) throws DAOException {
        String updateCommandeSQL = "UPDATE commandes SET fournisseur_id = ?, date_commande = ?, statut = ? WHERE id = ?";
        String deleteProduitsSQL = "DELETE FROM commandes_produits WHERE commande_id = ?";
        String insertProduitSQL = "INSERT INTO commandes_produits (commande_id, produit_id, quantite) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement updateStmt = conn.prepareStatement(updateCommandeSQL);
                 PreparedStatement deleteProduitsStmt = conn.prepareStatement(deleteProduitsSQL);
                 PreparedStatement insertProduitStmt = conn.prepareStatement(insertProduitSQL)) {

                // Update commandes table
                updateStmt.setInt(1, commande.getFournisseurId());
                updateStmt.setString(2, commande.getDate());
                updateStmt.setString(3, commande.getStatut());
                updateStmt.setInt(4, commande.getId());
                updateStmt.executeUpdate();

                // Delete old produits linked to this commande
                deleteProduitsStmt.setInt(1, commande.getId());
                deleteProduitsStmt.executeUpdate();

                // Insert new produits
                for (Map.Entry<Integer, Integer> entry : commande.getProduitsCommandes().entrySet()) {
                    insertProduitStmt.setInt(1, commande.getId());
                    insertProduitStmt.setInt(2, entry.getKey());
                    insertProduitStmt.setInt(3, entry.getValue());
                    insertProduitStmt.addBatch();
                }
                insertProduitStmt.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour de la commande", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String deleteProduitsSQL = "DELETE FROM commandes_produits WHERE commande_id = ?";
        String deleteCommandeSQL = "DELETE FROM commandes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteProduitsStmt = conn.prepareStatement(deleteProduitsSQL);
                 PreparedStatement deleteCommandeStmt = conn.prepareStatement(deleteCommandeSQL)) {

                deleteProduitsStmt.setInt(1, id);
                deleteProduitsStmt.executeUpdate();

                deleteCommandeStmt.setInt(1, id);
                deleteCommandeStmt.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression de la commande", e);
        }
    }
}
