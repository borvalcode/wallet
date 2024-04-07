package com.playtomic.tests.wallet.infrastructure.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import com.playtomic.tests.wallet.domain.query.view.WalletTopUpView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component("databaseWalletTopUpView")
public final class DatabaseWalletTopUpView implements WalletTopUpView {
  private final DataSource dataSource;

  public DatabaseWalletTopUpView(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<WalletTopUpDetails> get(long walletId, long topUpId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM top_up WHERE id=? AND wallet_id=?");
      preparedStatement.setLong(1, topUpId);
      preparedStatement.setLong(2, walletId);

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(
            WalletTopUpDetails.builder()
                .id(resultSet.getLong("id"))
                .amount(resultSet.getBigDecimal("amount"))
                .paymentId(resultSet.getString("payment_id"))
                .walletId(resultSet.getLong("wallet_id"))
                .build());
      }
      return Optional.empty();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
