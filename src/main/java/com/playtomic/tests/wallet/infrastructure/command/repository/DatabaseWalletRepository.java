package com.playtomic.tests.wallet.infrastructure.command.repository;

import com.playtomic.tests.wallet.domain.command.entity.Wallet;
import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import com.playtomic.tests.wallet.domain.command.repository.WalletRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component("databaseWalletRepository")
public final class DatabaseWalletRepository implements WalletRepository {
  private final DataSource dataSource;

  public DatabaseWalletRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public long nextId() {
    try (Connection connection = dataSource.getConnection()) {
      ResultSet resultSet =
          connection.prepareStatement("SELECT NEXTVAL('wallet_id_seq');").executeQuery();
      if (resultSet.next()) {
        return resultSet.getLong(1);
      }
      throw new RuntimeException("Sequence error");
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void store(Wallet wallet) {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT 1 FROM wallet where id=?;");
      preparedStatement.setLong(1, wallet.getId());

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        update(connection, wallet);
      } else {
        save(connection, wallet);
      }

      connection.commit();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Optional<Boolean> update(long walletId, Consumer<Wallet> updating) {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM wallet where id=?;");
      preparedStatement.setLong(1, walletId);

      ResultSet resultSet = preparedStatement.executeQuery();
      Optional<Boolean> result = Optional.empty();
      if (resultSet.next()) {
        Wallet wallet = buildWallet(resultSet);

        updating.accept(wallet);

        update(connection, wallet);

        result = Optional.of(true);
      }

      connection.commit();

      return result;
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  private Wallet buildWallet(ResultSet resultSet) throws SQLException {
    return new Wallet(resultSet.getLong("id"), resultSet.getBigDecimal("balance"));
  }

  private void save(Connection connection, Wallet wallet) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("INSERT INTO wallet(id, balance) VALUES (?, ?)");
    preparedStatement.setLong(1, wallet.getId());
    preparedStatement.setBigDecimal(2, wallet.getAmount());

    preparedStatement.execute();

    for (WalletTopUp walletTopUp : wallet.getWalletTopUps()) {
      upsert(connection, walletTopUp, wallet.getId());
    }
  }

  private void update(Connection connection, Wallet wallet) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("UPDATE wallet SET balance=? WHERE id=?");
    preparedStatement.setBigDecimal(1, wallet.getAmount());
    preparedStatement.setLong(2, wallet.getId());

    preparedStatement.executeUpdate();

    for (WalletTopUp walletTopUp : wallet.getWalletTopUps()) {
      upsert(connection, walletTopUp, wallet.getId());
    }
  }

  private void upsert(Connection connection, WalletTopUp walletTopUp, long walletId)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(
            "MERGE INTO top_up(id, amount, payment_id, wallet_id) VALUES (?, ?, ?, ?)");
    preparedStatement.setLong(1, walletTopUp.getId());
    preparedStatement.setBigDecimal(2, walletTopUp.getAmount());
    preparedStatement.setString(3, walletTopUp.getPaymentId());
    preparedStatement.setLong(4, walletId);

    preparedStatement.execute();
  }
}
