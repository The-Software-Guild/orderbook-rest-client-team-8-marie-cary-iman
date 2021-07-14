package app.dao;

import app.dto.Client;
import app.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class ClientDaoDB implements ClientDao{

  private final JdbcTemplate jdbc;

  @Autowired
  public ClientDaoDB(JdbcTemplate jdbcTemplate) { this.jdbc = jdbcTemplate; }

  @Override
  public List<Client> getAllClients() {
    final String SELECT_ALL_CLIENTS = "SELECT * FROM client";
    return jdbc.query(SELECT_ALL_CLIENTS, new ClientMapper());
  }

  @Override
  public Client getClientById(int clientId) {
    try{
      final String SELECT_CLIENT_BY_ID = "SELECT * FROM client WHERE clientId = ?";
      return jdbc.queryForObject(SELECT_CLIENT_BY_ID, new ClientMapper(), clientId);
    } catch (DataAccessException ex) {
      return null;
    }
  }

  @Override
  public boolean deleteClientById(int clientId) {
    final String DELETE_CLIENT = "DELETE FROM client WHERE clientId = ?";
    return jdbc.update(DELETE_CLIENT, clientId) > 0;
  }

  @Override
  @Transactional
  public Client addClient(Client newClient) {
    final String INSERT_CLIENT = "INSERT INTO client(name) VALUES(?)";

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update((Connection conn) -> {
      PreparedStatement statement = conn.prepareStatement(
              INSERT_CLIENT,
              Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, newClient.getName());
      return statement;
    }, keyHolder);

    newClient.setClientId(keyHolder.getKey().intValue());

    return newClient;
  }

  private static final class ClientMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int index) throws SQLException {

      Client client = new Client();
      client.setClientId(rs.getInt("clientId"));
      client.setName(rs.getString("name"));

      return client;
    }
  }
}
