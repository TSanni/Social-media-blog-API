package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
     ObjectMapper mapper = new ObjectMapper();


     AccountService accountService;
     MessageService messageService;

     public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/", this::exampleHandler);
        app.post("/register", this::registerNewAccountHandler); // Create a new account
        app.post("/login", this::loginHandler); // Verify login
        app.post("/messages", this::postMessagesHandler); // Post new message
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesbyAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerNewAccountHandler(Context context) throws JsonProcessingException {
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.registerNewAccount(account);

        if(addedAccount!=null){
            context.json(addedAccount);
            context.status(200);
        }else{
            context.status(400);
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException {
        Account account = mapper.readValue(context.body(), Account.class);
        Account logIn = accountService.loginAccount(account);
        if (logIn != null) {
            context.json(mapper.writeValueAsString(logIn));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    private void postMessagesHandler(Context context) throws JsonProcessingException {
        Message newMessage = mapper.readValue(context.body(), Message.class); 
        Message postedMessage = messageService.createMessage(newMessage);
        if (postedMessage != null) {
            context.json(postedMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);
        if (message != null) {
            context.json(messageService.getMessageByID(message_id)); 
            context.status(200);
        } else {
            context.status(200);

        }
    }

    private void deleteMessageByIdHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByID(message_id);

        if (deletedMessage != null) {
            context.json(deletedMessage);
            context.status(200);
        } else {
            context.result("");
            context.status(200);
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        Message messageToUpdate = mapper.readValue(context.body(), Message.class); 
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageByID(message_id, messageToUpdate);

        if (updatedMessage != null) {
            context.json(updatedMessage);
            context.status(200);
        } else {
            context.status(400);
        }

    }

    private void getAllMessagesbyAccountHandler(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messagesByAccountID = messageService.getAllMessagesbyAccountID(account_id);


        if (messagesByAccountID == null) {
            context.status(200);
        } else {
            context.json(messagesByAccountID);
            context.status(200);
        }
    }
}