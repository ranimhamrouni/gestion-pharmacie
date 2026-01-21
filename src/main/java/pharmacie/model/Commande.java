package pharmacie.model;

import java.util.Map;
import java.time.LocalDateTime;

public class Commande {
    private int id;
    private int fournisseurId;
    private LocalDateTime date;
    private String statut;
    private Map<Integer, Integer> produitsCommandes; // Map<produitId, quantite>
    public Commande(int fournisseurId, LocalDateTime date, String statut, Map<Integer, Integer> produitsCommandes) {
        this.fournisseurId = fournisseurId;
        this.date = date;
        this.statut = statut;
        this.produitsCommandes = produitsCommandes;
    }
    public Commande(int id, int fournisseurId, LocalDateTime date, String statut, Map<Integer, Integer> produitsCommandes) {
        this.id = id;
        this.fournisseurId = fournisseurId;
        this.date = date;
        this.statut = statut;
        this.produitsCommandes = produitsCommandes;
    }
    public int getId() {
        return id;
    }
    public int getFournisseurId() {
        return fournisseurId;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public String getStatut() {
        return statut;
    }
    public Map<Integer, Integer> getProduitsCommandes() {
        return produitsCommandes;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFournisseurId(int fournisseurId) {
        this.fournisseurId = fournisseurId;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setProduitsCommandes(Map<Integer, Integer> produitsCommandes) {
        this.produitsCommandes = produitsCommandes;
    }
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + id +
            ", fournisseurId=" + fournisseurId +
            ", date=" + date +
            ", statut='" + statut + '\'' +
            ", produitsCommandes=" + produitsCommandes +
            '}';
    }
}
