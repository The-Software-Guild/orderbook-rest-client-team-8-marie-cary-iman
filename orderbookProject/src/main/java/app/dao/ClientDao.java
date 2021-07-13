package app.dao;

import app.dto.Client;

import java.util.List;

public interface ClientDao {
    List<Client> getAllClient();
    Client deleteClient();
    Client addClient();

}
