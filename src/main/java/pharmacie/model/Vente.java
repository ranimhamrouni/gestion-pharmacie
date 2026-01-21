package pharmacie.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Vente {
    private int id;
    private int clientId;
    private Map<Integer, Integer> produitsVendus; // Map<produitId, quantite>
    private LocalDateTime dateVente;
    public Vente(int clientId, Map<Integer, Integer> produitsVendus, LocalDateTime dateVente) {
        this.clientId = clientId;
        this.produitsVendus = produitsVendus;
        this.dateVente = dateVente;
    }
    public Vente(int id, int clientId, Map<Integer, Integer> produitsVendus, LocalDateTime dateVente) {
        this.id = id;
        this.clientId = clientId;
        this.produitsVendus = produitsVendus;
        this.dateVente = dateVente;
    }
    public int getId() {
        return id;
    }
    public int getClientId() {
        return clientId;
    }
    public Map<Integer, Integer> getProduitsVendus() {
        return produitsVendus;
    }
    public LocalDateTime getDateVente() {
        return dateVente;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public void setProduitsVendus(Map<Integer, Integer> produitsVendus) {
        this.produitsVendus = produitsVendus;
    }
    public void setDateVente(LocalDateTime dateVente) {
        this.dateVente = dateVente;
    }
}
