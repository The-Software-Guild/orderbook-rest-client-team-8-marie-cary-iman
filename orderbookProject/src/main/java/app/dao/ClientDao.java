package app.dao;

import app.dto.Client;

import java.util.List;

public interface ClientDao {
    List<Client> getAllClients();

    Client getClientById(int clientId);

    boolean deleteClientById(int clientId);

    Client addClient(Client newClient);
}
