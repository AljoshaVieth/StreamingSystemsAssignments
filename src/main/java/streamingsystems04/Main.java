package streamingsystems04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamingsystems04.commandsmodel.CommandHandler;
import streamingsystems04.queryhandlingmodel.QueryHandler;
import streamingsystems04.queryhandlingmodel.QueryModel;
import streamingsystems04.implemented.MovingItemImpl;

/**
 * The main class of the application.
 */
public class Main {
    /**
     * The main method of the application.
     * @param args The arguments of the application.
     */
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Starting...");
        CommandHandler commandHandlerInstance = CommandHandler.getInstance();
        commandHandlerInstance.createItem(new MovingItemImpl("Moving Item 1"));
        commandHandlerInstance.createItem(new MovingItemImpl("Moving Item 2"));
        commandHandlerInstance.createItem(new MovingItemImpl("Moving Item 3"));
        commandHandlerInstance.changeValue("Moving Item 1", 42);
        commandHandlerInstance.changeValue("Moving Item 2", 69);
        commandHandlerInstance.changeValue("Moving Item 3", 4711);
        commandHandlerInstance.moveItem("Moving Item 1", new int[]{1, 2, 3});
        commandHandlerInstance.deleteItem("Moving Item 1");

        logger.info("Creating Query model");
        QueryModel queryModel = QueryModel.getInstance();
        queryModel.updateQueryModel();
        logger.info("Finished creating Query model");
        QueryHandler queryHandler = new QueryHandler(queryModel);
        //        queryModel.updateQueryModel();

        queryModel.getAllMovingItems().forEach(x -> logger.info(x.toString()));
        logger.info(
                queryHandler.getMovingItemByName("Moving Item 2").toString());

        logger.info("Terminating...");
    }
}