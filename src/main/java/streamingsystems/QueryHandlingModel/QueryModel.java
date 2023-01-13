package streamingsystems.QueryHandlingModel;

import streamingsystems.CommandsModel.EventStore;
import streamingsystems.CommandsModel.Meta.Event;
import streamingsystems.implemented.MovingItemDTO;
import streamingsystems.implemented.MovingItemImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The query model that gets generated from the list of events.
 */
public class QueryModel {
    private static QueryModel INSTANCE;

    /**
     * @return The singleton instance of the query model.
     */
    public static QueryModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QueryModel();
        }
        return INSTANCE;
    }


    private HashMap<String, MovingItemDTO> movingItemDTOHashMap = new HashMap<>();
    private HashMap<String, MovingItemImpl> movingItemImplHashMap = new HashMap<>();

    private QueryModel() {
        updateQueryModel();
    }


    /**
     * Update the query model from the event queue.
     */
    public void updateQueryModel() {
        recalculateQueryModelFromEvents(
                EventStore.getInstance().getEventQueue());
        movingItemDTOHashMap = convertToMovingItemDTOMap(movingItemImplHashMap);
    }

    /**
     * @param movingItemImplHashMap The moving item impl hash map to convert.
     * @return The moving item DTO hash map.
     */
    private HashMap<String, MovingItemDTO> convertToMovingItemDTOMap(
            HashMap<String, streamingsystems.implemented.MovingItemImpl> movingItemImplHashMap) {
        HashMap<String, MovingItemDTO> movingItemDTOHashMap = new HashMap<>();
        movingItemImplHashMap.forEach(
                (k, v) -> movingItemDTOHashMap.put(k, new MovingItemDTO(v)));
        return movingItemDTOHashMap;
    }


    /**
     * Recalculates the query model from the event queue.
     *
     * @param eventQueue The event queue to recalculate the query model from.
     */
    public void recalculateQueryModelFromEvents(
            LinkedBlockingQueue<Event> eventQueue) {
        eventQueue.forEach(event -> {
            if (event.apply() != null) {
                movingItemImplHashMap.put(event.getId(), event.apply());
            } else {
                movingItemImplHashMap.remove(event.getId());
            }
        });
    }


    /**
     * Get a moving item DTO by its name.
     *
     * @param name The name of the moving item to get.
     * @return The moving item DTO with the given name.
     */
    public MovingItemDTO getMovingItemDTOByName(String name) {
        if (!movingItemDTOHashMap.containsKey(name)) {
            throw new NoSuchElementException(
                    "There is no Item with this specific name!");
        }
        return movingItemDTOHashMap.get(name);
    }

    /**
     * Get a moving item impl by its name.
     *
     * @param name The name of the moving item to get.
     * @return The moving item impl with the given name.
     */
    public MovingItemImpl getMovingItemImplByName(String name) {
        return movingItemImplHashMap.get(name);
    }

    /**
     * Get all moving item DTOs.
     *
     * @return The entire moving item DTO hash map.
     */
    public Collection<MovingItemDTO> getAllMovingItems() {
        return this.movingItemDTOHashMap.values();
    }
}
