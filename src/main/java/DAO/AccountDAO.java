package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    // Register a new account
    public Account registerNewAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Step 1: Check if username already exists
            String checkSql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, account.getUsername());

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                // Username already exists
                return null;
            }

            // Step 2: Insert new account
            String insertSql = "INSERT INTO account (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, account.getUsername());
                insertStmt.setString(2, account.getPassword());
                insertStmt.executeUpdate();
    
                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) account.setAccount_id(keys.getInt(1));
                return account;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

   // Login to account
   public Account loginToAccount(Account account) {
    Connection connection = ConnectionUtil.getConnection();

    try {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Matching account found
            int accountId = resultSet.getInt("account_id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            return new Account(accountId, username, password);
        }

    } catch (Exception e) {
        System.out.println("Login error: " + e.getMessage());
    }

    return null;
}
}
