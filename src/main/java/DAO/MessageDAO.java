package DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

import java.util.List;
import java.util.ArrayList;

public class MessageDAO {

    /**
     * Add a message record into the database which matches the values contained in the message object.
     *
     * @param message a message object. the message object does not contain an message ID.
     * @return an message object with an id
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            int numUpdated = ps.executeUpdate();
            if (numUpdated <= 0) {
                return null;
            }

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                return new Message(pkeyResultSet.getInt(1), message.getPosted_by(), 
                        message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    
    /**
     * Update the message identified by the message id to the values contained in the message object.
     *
     * @param message_id a message ID.
     * @param message a message object. the message object does not contain an message ID.
     * @return an updated message object with id
     */
    public Message updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql, new int[] {1, 2, 3, 4});
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_id);

            int numUpdated = ps.executeUpdate();
            if (numUpdated <= 0) {
                return null;
            }

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                return new Message(pkeyResultSet.getInt(1), pkeyResultSet.getInt(2), 
                        pkeyResultSet.getString(3), pkeyResultSet.getLong(4));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    /**
     * Delete the message identified by the message id from the message table.
     *
     * @param message_id a message ID.
     * @return the removed message object, if message_id exist
     */
    public Message deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message toDelete = this.getMessageById(message_id);

        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message_id);

            int numUpdated = ps.executeUpdate();
            if (numUpdated <= 0) {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return toDelete;
    }


    /**
     * Retrieve all messages from the message table.
     * 
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }


    /**
     * Retrieve a specific message using its message ID.
     *
     * @param message_id a message ID.
     * @return the message object, null if message_id does not exist
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    /**
     * Retrieve all messages from a particular user (account_id).
     * 
     * @param account_id a account ID.
     * @return all messages from the user
     */
    public List<Message> getMessagesFromAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
