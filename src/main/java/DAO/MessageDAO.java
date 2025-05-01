package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;


import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message postNewMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            String insertSql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
    
            insertStmt.setInt(1, message.getPosted_by());
            insertStmt.setString(2, message.getMessage_text());
            insertStmt.setLong(3, message.getTime_posted_epoch());
    
            int rowsAffected = insertStmt.executeUpdate();
    
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1);
                    message.setMessage_id(messageId);
                }
                return message;
            }

            
    
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int messageID) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageID);
    
            ResultSet rs = statement.executeQuery();
    
            if (rs.next()) {
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
    
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    
        return message;
    }

    // public Message deleteMessageByID(int messageID) {
    //     Message deletedMessage = null;
    //     Connection connection = ConnectionUtil.getConnection();

    //     try {
    //         String sql = "DELETE FROM message WHERE message_id = ?";
    //         PreparedStatement statement = connection.prepareStatement(sql);
    //         statement.setInt(1, messageID);

    //         ResultSet rs = statement.executeQuery();
    
    //         if (rs.next()) {
    //             deletedMessage = new Message(
    //                 rs.getInt("message_id"),
    //                 rs.getInt("posted_by"),
    //                 rs.getString("message_text"),
    //                 rs.getLong("time_posted_epoch")
    //             );
    //         }
    //     } catch(SQLException e){
    //         System.out.println(e.getMessage());
    //     }

    //     return deletedMessage;
    // }

    public Message deleteMessageByID(int messageID) {
        Message deletedMessage = null;
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // Step 1: Select the message
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            selectStmt.setInt(1, messageID);
            ResultSet rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
    
                // Step 2: Delete the message
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
                deleteStmt.setInt(1, messageID);
                deleteStmt.executeUpdate();
            }
    
        } catch(SQLException e){
            System.out.println("Error deleting message: " + e.getMessage());
        }
    
        return deletedMessage;
    }
    
    public Message updateMessageByID(int messageID, Message updatedMessageData) {
        Message updatedMessage = null;
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // Step 1: Check if message exists
            String checkSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, messageID);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                // Step 2: Update the message
                String updateSql = "UPDATE message SET message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setString(1, updatedMessageData.getMessage_text());
                updateStmt.setLong(2, updatedMessageData.getTime_posted_epoch());
                updateStmt.setInt(3, messageID);
    
                updateStmt.executeUpdate();
    
                // Step 3: Return the updated message
                updatedMessage = new Message(
                    messageID,
                    rs.getInt("posted_by"), // Keep original poster
                    updatedMessageData.getMessage_text(),
                    rs.getLong("time_posted_epoch")
                );
            }
    
        } catch (SQLException e) {
            System.out.println("Error updating message: " + e.getMessage());
        }
    
        return updatedMessage;
    }
    
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
    
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching messages by account: " + e.getMessage());
        }
    
        return messages;
    }
    
}
