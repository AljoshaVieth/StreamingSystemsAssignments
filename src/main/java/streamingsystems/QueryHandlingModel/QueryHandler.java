package streamingsystems.QueryHandlingModel;

import streamingsystems.MovingItemImpl;
import streamingsystems.implemented.MovingItemDTO;
import streamingsystems.QueryHandlingModel.Predefined.Query;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

public class QueryHandler implements Query {
    private final QueryModel queryModel;

    public QueryHandler(QueryModel queryModel) {
        this.queryModel = queryModel;
    }

    @Override
    public MovingItemDTO getMovingItemByName(String name) {
        return new MovingItemDTO(queryModel.getMovingItemDTOByName(name));
    }

    @SuppressWarnings("Convert2MethodRef")
    public Collection<MovingItemDTO> getAllMovingItemsAsCollection() {
        return queryModel.getAllMovingItems();
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItems() {
        return Collections.enumeration(getAllMovingItemsAsCollection());
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position) {
        Collection<MovingItemDTO> movingItemDTOsAtPosition = getAllMovingItemsAsCollection()
                .stream().filter((MovingItemImpl eachMovingItem) -> eachMovingItem.getLocation() == position).toList();

        return Collections.enumeration(movingItemDTOsAtPosition);
    }
}
