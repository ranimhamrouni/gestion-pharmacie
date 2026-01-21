package pharmacie.dao;

import pharmacie.model.Fournisseur;
import pharmacie.exception.DAOException;

import java.util.List;

public interface FournisseurDAO {
    void save(Fournisseur fournisseur) throws DAOException;
    Fournisseur findById(int id) throws DAOException;
    List<Fournisseur> findAll() throws DAOException;
    void update(Fournisseur fournisseur) throws DAOException;
    void delete(int id) throws DAOException;
}
