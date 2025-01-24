package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesFromAccountIdHandler);

        return app;
    }


    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    /**
     * Handler to register an account.
     * POST /register
     * 
     * The body will contain a representation of a JSON Account, but will not contain an account_id.
     * 
     * The response body should contain a JSON of the Account, including its account_id. 
     * The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     * If the registration is not successful, the response status should be 400. (Client error)
     *
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);

        Account output = accountService.insertAccount(account);
        if (output != null) {
            context.json(output);
            return;
        }

        context.status(400);
    }


    /**
     * Handler to login to an account.
     * POST /login
     * 
     * The request body will contain a JSON representation of an Account, not 
     * containing an account_id. In the future, this action may generate a 
     * Session token to allow the user to securely use the site. We will not worry about this for now.
     *
     * If successful, the response body should contain a JSON of the account in the 
     * response body, including its account_id. The response status should be 200 OK, which is the default.
     * If the login is not successful, the response status should be 401. (Unauthorized)

     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(jsonString, Account.class);

        Account output = accountService.login(account);
        if (output != null) {
            context.json(output);
            return;
        }

        context.status(401);
    }


    /**
     * Handler to create a message.
     * POST /message
     * 
     * The request body will contain a JSON representation of a message, which 
     * should be persisted to the database, but will not contain a message_id.
     * 
     * 
     * If successful, the response body should contain a JSON of the message, 
     * including its message_id. The response status should be 200, which is 
     * the default. The new message should be persisted to the database.
     *
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(context.body(), Message.class);

        Message output = messageService.insertMessage(message);
        if (output != null) {
            context.json(output);
            return;
        }

        context.status(400);
    }


    /**
     * Handler to delete a message.
     * DELETE /message/{message_id}
     * 
     * The request body will contain a JSON representation of a message, which 
     * should be persisted to the database, but will not contain a message_id.
     * 
     * The deletion of an existing message should remove an existing message 
     * from the database. If the message existed, the response body should 
     * contain the now-deleted message. The response status should be 200, which 
     * is the default. If the message did not exist, the response status should 
     * be 200, but the response body should be empty. This is because the 
     * DELETE verb is intended to be idempotent, ie, multiple calls to the 
     * DELETE endpoint should respond with the same type of response.
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            Message output = messageService.deleteMessage(message_id);
            if (output != null) {
                context.json(output);
                return;
            }

        } catch (Exception e) {
            // message_id parsed incorrectly
        }
    }


    /**
     * Handler to update a message.
     * PATCH /message/{message_id}
     * 
     * The request body should contain a new message_text values to replace the 
     * message identified by message_id. The request body can not be guaranteed 
     * to contain any other information.
     * 
     * If the update is successful, the response body should contain the full 
     * updated message (including message_id, posted_by, message_text, and 
     * time_posted_epoch), and the response status should be 200, which is the 
     * default. The message existing on the database should have the 
     * updated message_text. If the update of the message is not successful 
     * for any reason, the response status should be 400. (Client error)
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(jsonString, Message.class);

        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            Message output = messageService.updateMessage(message_id, message);
            if (output != null) {
                context.json(output);
                return;
            } 

        } catch (Exception e) {
            // message_id parsed incorrectly
        }

        context.status(400);
    }


    /**
     * Handler to get all messages.
     * GET /message
     * 
     * The response body should contain a JSON representation of a list 
     * containing all messages retrieved from the database. It is expected for 
     * the list to simply be empty if there are no messages. The response 
     * status should always be 200, which is the default.
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        List<Message> output = messageService.getAllMessages();

        context.json(output);
    }


    /**
     * Handler to get all messages.
     * GET /message/{message_id}
     * 
     * The response body should contain a JSON representation of the message 
     * identified by the message_id. It is expected for the response body to 
     * simply be empty if there is no such message. The response status 
     * should always be 200, which is the default.
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            Message output = messageService.getMessageById(message_id);
            if (output != null) {
                context.json(output);
            }

            // if message_id not found in database, then request would be considered
            // "fulfilled", and by default return 200

        } catch (Exception e) {
            // message id parsed incorrectly
        }
    }

    
    /**
     * Handler to get all messages from an specific account.
     * GET /accounts/{account_id}/messages
     * 
     * The response body should contain a JSON representation of a list 
     * containing all messages posted by a particular user, which is retrieved 
     * from the database. It is expected for the list to simply be empty if 
     * there are no messages. The response status should always be 200, which
     * is the default
     * 
     * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessagesFromAccountIdHandler(Context context) throws JsonProcessingException {
        try {
            int account_id = Integer.parseInt(context.pathParam("account_id"));

            List<Message> output = messageService.getMessagesFromAccountId(account_id);
            if (output != null) {
                context.json(output);
            }

            // if message_id not found in database, then request would be considered
            // "fulfilled", and by default return 200

        } catch (Exception e) {
            // account id parsed incorrectly
        }
    }
}