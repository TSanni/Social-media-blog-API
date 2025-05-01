package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }


    public Message createMessage(Message message) {
        Message newMessage = null;

        if ((message.message_text != "") && (message.message_text.length() <= 255)) {
            newMessage = messageDAO.postNewMessage(message);
        }

        return newMessage;
    }


    public List<Message> getAllMessages() {
        List<Message> allMessages;
        allMessages = messageDAO.getAllMessages();
        return allMessages;
    }


    public Message getMessageByID(int messageID) {
        Message messageByID = messageDAO.getMessageByID(messageID);
        return messageByID;
    }

    public Message deleteMessageByID(int messageID) {
        Message deletedMessage = null;
        deletedMessage = messageDAO.deleteMessageByID(messageID);
        return deletedMessage;
    }

    public Message updateMessageByID(int messageID, Message message) {
        Message updatedMessage = null;
        if (message.message_text == "") {
            return null;
        }
        updatedMessage = messageDAO.updateMessageByID(messageID, message);

        return updatedMessage;
    }

    public List<Message> getAllMessagesbyAccountID(int messageID) {
        List<Message> messagesByID = null;
        messagesByID = messageDAO.getMessagesByAccountId(messageID);


        return messagesByID;

    }
     
}
