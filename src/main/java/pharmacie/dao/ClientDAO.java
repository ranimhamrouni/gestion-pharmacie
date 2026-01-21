package pharmacie.dao;

import java.util.List;

import pharmacie.exception.DAOException;
import pharmacie.model.Client;

public interface ClientDAO {
    void save(Client client) throws DAOException;
    Client findById(int id) throws DAOException;
    List<Client> findAll() throws DAOException;
    void update(Client client) throws DAOException;
    void delete(int id) throws DAOException;
}
