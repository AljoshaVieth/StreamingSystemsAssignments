package streamingsystems.implemented;

import streamingsystems.Helpers;
import streamingsystems.MovingItem;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The implementation of a moving item used in the domain model, commands and
 * events.
 */
public class MovingItemImpl implements MovingItem, Serializable {
    private final String name;
    private int[] location;
    private int numberOfMoves;
    private int value;

    /**
     * @param name          The name of the moving item.
     * @param location      The location of the moving item.
     * @param numberOfMoves The number of moves the moving item has made.
     * @param value         The value of the moving item.
     */
    public MovingItemImpl(String name, int[] location, int numberOfMoves,
                          int value) {
        this.name = name;
        this.location = location;
        this.numberOfMoves = numberOfMoves;
        this.value = value;
    }

    /**
     * @param movingItem The moving item to use as a template.
     */
    public MovingItemImpl(MovingItem movingItem) {
        this.name = movingItem.getName();
        this.location = movingItem.getLocation();
        this.numberOfMoves = movingItem.getNumberOfMoves();
        this.value = movingItem.getNumberOfMoves();
    }

    /**
     * @param name The name of the moving item.
     */
    public MovingItemImpl(String name) {
        this(name, new int[]{0, 0, 0}, 0, 0);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    @Override
    public int getValue() {
        return value;
    }

    /**
     * @param location The new location of the moving item.
     */
    public void setLocation(int[] location) {
        this.location = location;
    }

    /**
     * @param location The vector to add to the current location.
     */
    public void move(int[] location) {
        this.location = Helpers.addArrays(this.location, location);
    }


    /**
     * Increments the number of moves the moving item has made by one.
     */
    public void addMoveToMoveCounter() {
        this.numberOfMoves++;
    }

    /**
     * Set the value of the moving item.
     *
     * @param value The new value of the moving item.
     */
    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "MovingItemDTO{" + "name='" + name + '\'' + ", location=" + Arrays.toString(
                location) + ", numberOfMoves=" + numberOfMoves + ", value=" + value + '}';
    }
}
