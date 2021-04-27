package ru.dartilla.industry.resource;

public class Wood implements Resource, Fuel {

    private int itemNumber;

    public Wood(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Override
    public int getItemNumber() {
        return 0;
    }

    @Override
    public String toString() {
        return "Wood{" +
                "itemNumber=" + itemNumber +
                '}';
    }
}
