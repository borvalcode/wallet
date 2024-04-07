package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component("databaseWalletView")
public class DatabaseWalletView implements WalletView {
  private final DataSource dataSource;

  public DatabaseWalletView(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<WalletDetails> get(long walletId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM wallet WHERE id=?");
      preparedStatement.setLong(1, walletId);

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(
            new WalletDetails(resultSet.getLong("id"), resultSet.getBigDecimal("balance")));
      }
      return Optional.empty();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
