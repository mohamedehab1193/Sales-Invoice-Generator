package com.sales.salesinvoicegenerator.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {

    private int number;
    private String date;
    private String customerName; // Camel Case
    private double total;
    private List<Item> items = new ArrayList();

    // Access modifier    returned data type      functionName()   {}
    // Arguments
    // Parameters
    // I need to define a function that accepts two numbers and returns the sum of these two numbers
    // int firstNumber, int secondNumber ==> parameters      3, 5 ===> arguments
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
        /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }
    
    public void addItem(Item item){
            this.items.add(item);
    }
    
}
