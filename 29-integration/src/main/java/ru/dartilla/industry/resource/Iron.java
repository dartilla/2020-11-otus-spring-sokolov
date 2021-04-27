package ru.dartilla.industry.resource;

public class Iron implements Resource {

    private int itemNumber;

    public Iron(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Override
    public int getItemNumber() {
        return itemNumber;
    }

    @Override
    public String toString() {
        return "Iron{" +
                "itemNumber=" + itemNumber +
                '}';
    }
}
