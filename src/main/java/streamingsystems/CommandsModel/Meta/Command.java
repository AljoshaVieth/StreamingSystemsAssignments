package streamingsystems.CommandsModel.Meta;

/**
 * Represents a command to be handled by the command handler.
 */
public abstract class Command {
    /**
     * Execute the command's action
     */
    public abstract void handle();
}
