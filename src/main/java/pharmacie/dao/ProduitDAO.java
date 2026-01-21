package pharmacie.dao;

import pharmacie.model.Produit;
import java.util.List;
import pharmacie.exception.DAOException;

public interface ProduitDAO {
    void save(Produit produit) throws DAOException;
    Produit findById(int id) throws DAOException;
    List<Produit> findAll() throws DAOException;
    void update(Produit produit) throws DAOException;
    void delete(int id) throws DAOException;
}
