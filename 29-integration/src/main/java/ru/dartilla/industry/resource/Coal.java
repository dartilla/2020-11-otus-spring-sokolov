package ru.dartilla.industry.resource;

public class Coal implements Resource, Fuel {

    private int itemNumber;

    public Coal(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Override
    public int getItemNumber() {
        return itemNumber;
    }

    @Override
    public String toString() {
        return "Coal{" +
                "itemNumber=" + itemNumber +
                '}';
    }
}
