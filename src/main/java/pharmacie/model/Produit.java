package pharmacie.model;

public class Produit {
    private int id;
    private String nom;
    private double prixUnitaire;
    private int quantiteStock;
    private String description;
    private int seuilMinimal;

    public Produit(String nom, double prixUnitaire, int quantiteStock, String description, int seuilMinimal) {
    this.nom = nom;
    this.prixUnitaire = prixUnitaire;
    this.quantiteStock = quantiteStock;
    this.description = description;
    this.seuilMinimal = seuilMinimal;
    }

    public Produit(int id, String nom, double prixUnitaire, int quantiteStock, String description, int seuilMinimal) {
    this.id = id;
    this.nom = nom;
    this.prixUnitaire = prixUnitaire;
    this.quantiteStock = quantiteStock;
    this.description = description;
    this.seuilMinimal = seuilMinimal;
    }

    public int getId() {
        return id;}

    public String getNom() {
        return nom;}

    public double getPrixUnitaire() {
        return prixUnitaire;}

    public int getQuantiteStock() {
        return quantiteStock;}

    public int getSeuilMinimal() {
        return seuilMinimal;}

    public String getDescription() {
        return description;}
    
    public void setId(int id) {
        this.id = id;}
        
    public void setNom(String nom) {
        this.nom = nom;}

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;}
    
    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;}

    public void setDescription(String description) {
        this.description = description;}

    public void setSeuilMinimal(int seuilMinimal) {
        this.seuilMinimal = seuilMinimal;}

    @Override
    public String toString() {
        return "Produit [id=" + id + ", nom=" + nom + ", prixUnitaire=" + prixUnitaire + ", quantiteStock="
                + quantiteStock + ", description=" + description + ", seuilMinimal=" + seuilMinimal + "]";
    }
}


