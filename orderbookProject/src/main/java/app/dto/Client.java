package app.dto;

import java.util.Objects;

public class Client {
  private Integer clientId;
  private String name;

  public Integer getClientId() {
    return clientId;
  }

  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Client client = (Client)o;
    return Objects.equals(clientId, client.clientId) && Objects.equals(name, client.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, name);
  }
}
