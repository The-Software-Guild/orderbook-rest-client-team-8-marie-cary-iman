package orderBook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import orderBook.dto.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


@SpringBootApplication
public class App {
    @Autowired
    private JdbcTemplate jdbc;
    private static Scanner sc;

    public static void main(String args[]) {
        System.out.println("hello");
        SpringApplication.run(App.class, args);
    }

    private void displayClientList() throws SQLException {
        List<Client> clients = jdbc.query("SELECT * FROM client", new ClientMapper());
        for (Client cl : clients) {
            System.out.printf("%s: %s ",
                    cl.getClientId(),
                    cl.getName()
            );
        }
        System.out.println("");
    }

    private static final class ClientMapper implements RowMapper<Client> {

        @Override
        public Client mapRow(ResultSet rs, int index) throws SQLException {
            Client cl = new Client();
            cl.setClientId(rs.getInt("clientId"));
            cl.setName(rs.getString("name"));

            return cl;
        }
    }

}




