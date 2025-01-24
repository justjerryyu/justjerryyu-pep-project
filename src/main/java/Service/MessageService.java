package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }


    /**
     * This is used for when a mock messageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of messageService independently of messageDAO.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
    
    /**
     * Add a new message to the database.
     * 
     * The creation of the message will be successful if and only if the 
     * message_text is not blank, is under 255 characters, and posted_by 
     * refers to a real, existing user.
     *
     * @param message an object representing a new message.
     * @return the newly added message if the add operation was successful, including the message_id. 
     */
    public Message insertMessage(Message message) {
        if (message != null && message.message_text.length() > 0 && message.message_text.length() <= 255) {
            // posted_by will be checked when querying bc message table has 
            // foreign key account_id from account table
            return messageDAO.insertMessage(message);
        }

        return null;
    }

    
    /**
     * Update an existing message from the database.
     * 
     * The update of a message should be successful if and only if the 
     * message id already exists and the new message_text is not blank and 
     * is not over 255 characters.
     * 
     * @param message_id the ID of the message to be modified.
     * @param message an object containing all data that should replace the values contained by the existing message_id.
     *         the message object does not contain a message ID.
     * @return the newly updated message if the update operation was successful. Return null if the update operation was
     *         unsuccessful.
     */
    public Message updateMessage(int message_id, Message message) {
        if (message != null && message.message_text.length() > 0 && message.message_text.length() <= 255) {
            return messageDAO.updateMessage(message_id, message);
        }
        
        return null;
    }


    /**
     * Delete the message with the specific message id.
     *
     * @param message_id a message ID.
     * @return the removed message object, if message_id exist
     */
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }


    /**
     * Retrieve all messages.
     * 
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    /**
     * Retrieve a specific message using its message ID.
     *
     * @param message_id a message ID.
     * @return the message object, null if message_id does not exist
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }


    /**
     * Retrieve all messages from a particular user (account_id).
     * 
     * @param account_id a account ID.
     * @return all messages from the user
     */
    public List<Message> getMessagesFromAccountId(int account_id) {
        return messageDAO.getMessagesFromAccountId(account_id);
    }
}
