package pharmacie.dao;

import pharmacie.model.Commande;
import pharmacie.exception.DAOException;

import java.util.List;

public interface CommandeDAO {
    public void save(Commande commande) throws DAOException;
    public Commande findById(int id) throws DAOException;
    public List<Commande> findAll() throws DAOException;
    public void update(Commande commande) throws DAOException;
    public void delete(int id) throws DAOException;
}
