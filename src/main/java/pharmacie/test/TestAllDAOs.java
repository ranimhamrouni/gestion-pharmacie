package pharmacie.test;

import pharmacie.dao.*;
import pharmacie.model.*;
import pharmacie.exception.DAOException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAllDAOs {
    public static void main(String[] args) {
        try {
            // Test ProduitDAO
            ProduitDAO produitDAO = new ProduitDAOImpl();
            Produit p = new Produit("Paracetamol", 2.5, 100, "Pain reliever", 10);
            produitDAO.save(p);
            System.out.println("Produit saved.");

            List<Produit> produits = produitDAO.findAll();
            System.out.println("All produits:");
            for (Produit prod : produits) {
                System.out.println(prod.getNom());
            }

            // Test ClientDAO
            ClientDAO clientDAO = new ClientDAOImpl();
            Client c = new Client("John Doe", "123 Street", "555-1234");
            clientDAO.save(c);
            System.out.println("Client saved.");

            List<Client> clients = clientDAO.findAll();
            System.out.println("All clients:");
            for (Client client : clients) {
                System.out.println(client.getNom());
            }

            // Test VenteDAO
            VenteDAO venteDAO = new VenteDAOImpl();
            Map<Integer, Integer> produitsVendus = new HashMap<>();
            produitsVendus.put(p.getId(), 2); // Selling 2 units of the product
            Vente vente = new Vente(c.getId(), produitsVendus, LocalDateTime.of(2026, 1, 21, 10, 30));
            venteDAO.save(vente);
            System.out.println("Vente saved.");

            // Similarly, test CommandeDAO, FournisseurDAO, etc.

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
