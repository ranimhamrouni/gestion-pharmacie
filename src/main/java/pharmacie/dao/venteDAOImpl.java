package pharmacie.dao;

import pharmacie.model.Vente;
import pharmacie.exception.DAOException;
import pharmacie.util.DatabaseConnection;

import java.util.Map;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class venteDAOImpl implements VenteDAO {
    @Override
    public void save(Vente vente) throws DAOException {

        String insertVenteSQL =
            "INSERT INTO ventes (client_id, date_vente) VALUES (?, ?)";

        String insertVenteProduitSQL =
            "INSERT INTO ventes_produits (vente_id, produit_id, quantite) VALUES (?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement venteStmt =
                conn.prepareStatement(insertVenteSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement venteProduitStmt =
                conn.prepareStatement(insertVenteProduitSQL)
        ) {
            // Start transaction
            conn.setAutoCommit(false);

            // 1️⃣ Insert into ventes
            venteStmt.setInt(1, vente.getClientId());
            venteStmt.setString(2, vente.getDateVente());
            venteStmt.executeUpdate();

            // 2️⃣ Retrieve generated vente_id
            ResultSet rs = venteStmt.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                throw new DAOException("Impossible de récupérer l'ID de la vente");
            }
            int venteId = rs.getInt(1);

            // 3️⃣ Insert into ventes_produits
            for (var entry : vente.getProduitsVendus().entrySet()) {
                venteProduitStmt.setInt(1, venteId);
                venteProduitStmt.setInt(2, entry.getKey());   // produit_id
                venteProduitStmt.setInt(3, entry.getValue()); // quantite
                venteProduitStmt.addBatch();
            }
            venteProduitStmt.executeBatch();

            // 4️⃣ Commit transaction
            conn.commit();

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de l'enregistrement de la vente", e);
        }
    }
    @Override
    public Vente findById(int id) throws DAOException {
        String venteSql = "SELECT client_id, date_vente FROM ventes WHERE id = ?";
        String produitsSql = "SELECT produit_id, quantite FROM ventes_produits WHERE vente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement venteStmt = conn.prepareStatement(venteSql);
            PreparedStatement produitsStmt = conn.prepareStatement(produitsSql)) {
            
            // Step 1: Get vente info
            venteStmt.setInt(1, id);
            ResultSet venteRs = venteStmt.executeQuery();
            
            if (!venteRs.next()) {
                return null; // No vente found with this ID
            }
            
            int clientId = venteRs.getInt("client_id");
            String dateVente = venteRs.getString("date_vente");
            
            // Step 2: Get produits sold in this vente
            produitsStmt.setInt(1, id);
            ResultSet produitsRs = produitsStmt.executeQuery();
            
            Map<Integer, Integer> produitsVendus = new HashMap<>();
            while (produitsRs.next()) {
                int produitId = produitsRs.getInt("produit_id");
                int quantite = produitsRs.getInt("quantite");
                produitsVendus.put(produitId, quantite);
            }
            
            // Step 3: Create and return Vente object
            return new Vente(id, clientId, produitsVendus, dateVente);
            
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche de la vente par ID", e);
        }
    }
    @Override
    public List<Vente> findAll() throws DAOException {
        String ventesSql = "SELECT id, client_id, date_vente FROM ventes";
        String produitsSql = "SELECT produit_id, quantite FROM ventes_produits WHERE vente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ventesStmt = conn.prepareStatement(ventesSql);
            PreparedStatement produitsStmt = conn.prepareStatement(produitsSql);
            ResultSet ventesRs = ventesStmt.executeQuery()) {
            
            List<Vente> ventes = new java.util.ArrayList<>();
            
            while (ventesRs.next()) {
                int venteId = ventesRs.getInt("id");
                int clientId = ventesRs.getInt("client_id");
                String dateVente = ventesRs.getString("date_vente");
                
                // Get produits for this vente
                produitsStmt.setInt(1, venteId);
                ResultSet produitsRs = produitsStmt.executeQuery();
                
                Map<Integer, Integer> produitsVendus = new HashMap<>();
                while (produitsRs.next()) {
                    int produitId = produitsRs.getInt("produit_id");
                    int quantite = produitsRs.getInt("quantite");
                    produitsVendus.put(produitId, quantite);
                }
                
                Vente vente = new Vente(venteId, clientId, produitsVendus, dateVente);
                ventes.add(vente);
            }
            
            return ventes;
            
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de toutes les ventes", e);
        }
    }
    @Override
    public void update(Vente vente) throws DAOException {
        String updateVenteSQL =
            "UPDATE ventes SET client_id = ?, date_vente = ? WHERE id = ?";
        String deleteProduitsSQL =
            "DELETE FROM ventes_produits WHERE vente_id = ?";
        String insertProduitSQL =
            "INSERT INTO ventes_produits (vente_id, produit_id, quantite) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try(PreparedStatement venteStmt =
                    conn.prepareStatement(updateVenteSQL);
                PreparedStatement deleteStmt =
                    conn.prepareStatement(deleteProduitsSQL);
                PreparedStatement insertStmt =
                    conn.prepareStatement(insertProduitSQL)) {

                // 1️⃣ Update ventes table
                venteStmt.setInt(1, vente.getClientId());
                venteStmt.setString(2, vente.getDateVente());
                venteStmt.setInt(3, vente.getId());
                venteStmt.executeUpdate();

                // 2️⃣ Delete existing produits for this vente
                deleteStmt.setInt(1, vente.getId());
                deleteStmt.executeUpdate();

                // 3️⃣ Insert new produits
                for (var entry : vente.getProduitsVendus().entrySet()) {
                    insertStmt.setInt(1, vente.getId());
                    insertStmt.setInt(2, entry.getKey());   // produit_id
                    insertStmt.setInt(3, entry.getValue()); // quantite
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();

                // 4️⃣ Commit transaction
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la mise à jour de la vente", e);
        }
    }
    @Override
    public void delete(int id) throws DAOException {
        String deleteVenteSQL = "DELETE FROM ventes WHERE id = ?";
        String deleteProduitsSQL = "DELETE FROM ventes_produits WHERE vente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try (PreparedStatement deleteProduitsStmt = conn.prepareStatement(deleteProduitsSQL);
                 PreparedStatement deleteVenteStmt = conn.prepareStatement(deleteVenteSQL)) {
                
                // 1️⃣ Delete from ventes_produits
                deleteProduitsStmt.setInt(1, id);
                deleteProduitsStmt.executeUpdate();
                
                // 2️⃣ Delete from ventes
                deleteVenteStmt.setInt(1, id);
                deleteVenteStmt.executeUpdate();
                
                // 3️⃣ Commit transaction
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression de la vente", e);
        }
    }
}
