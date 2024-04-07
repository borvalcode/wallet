package com.playtomic.tests.wallet.infrastructure.command.repository;

import com.playtomic.tests.wallet.domain.command.repository.WalletTopUpRepository;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component("databaseWalletTopUpRepository")
public class DatabaseWalletTopUpRepository implements WalletTopUpRepository {
  private final DataSource dataSource;

  public DatabaseWalletTopUpRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public long nextId() {
    try (Connection connection = dataSource.getConnection()) {
      ResultSet resultSet =
          connection.prepareStatement("SELECT NEXTVAL('top_up_id_seq');").executeQuery();
      if (resultSet.next()) {
        return resultSet.getLong(1);
      }
      throw new RuntimeException("Sequence error");
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
