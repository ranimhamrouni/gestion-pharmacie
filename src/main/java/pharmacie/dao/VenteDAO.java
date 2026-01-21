package pharmacie.dao;

import pharmacie.model.Vente;
import pharmacie.exception.DAOException;

import java.util.List;
public interface VenteDAO {
    public void save(Vente vente) throws DAOException;
    public Vente findById(int id) throws DAOException;
    public List<Vente> findAll() throws DAOException;
    public void update(Vente vente) throws DAOException;
    public void delete(int id) throws DAOException;
}
