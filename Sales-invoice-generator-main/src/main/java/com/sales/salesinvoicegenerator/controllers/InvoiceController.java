package com.sales.salesinvoicegenerator.controllers;
// Public => Any class can see this class from anywhere
// Protected => Only classes in the same package can see this class (class without any access modifier)

import com.sales.salesinvoicegenerator.models.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceController {

    public static int invoiceNumber = 1;
    public static List<Invoice> invoicesList = new ArrayList();

    public void createNewInvoice(String customerName, String date, double total, List<Item> items) {
        Invoice newInvoice = new Invoice();
        newInvoice.setNumber(invoiceNumber);
        newInvoice.setCustomerName(customerName);
        newInvoice.setDate(date);
        newInvoice.setTotal(total);
        newInvoice.setItems(items);
        invoicesList.add(newInvoice);
        // To increment next invoice no.
        invoiceNumber++;

    }

    public Item createNewItem(int itemNumber, String name, Date date, double price, int count) {
        Item newItem = new Item();
        newItem.setNumber(itemNumber);
        newItem.setName(name);
        newItem.setPrice(price);
        newItem.setCount(count);
        return newItem;
    }

    public void addItemToInvoice(int invoiceNumber, Item item) {
        // Getting oldItems
        List<Item> invoiceItems = invoicesList.get(invoiceNumber - 1).getItems();
        invoiceItems.add(item);
    }

    public boolean addInvoicesListToCSVFiles() {
        boolean isSuccessful = false;
        // First we will create invoices csv file (headerLine.csv)
        try (
                PrintWriter writerInvoices = new PrintWriter("InvoiceHeader.csv")) {
            StringBuilder sbInvoices = new StringBuilder();
            for (Invoice invoice : invoicesList) {
                sbInvoices.append(invoice.getNumber());
                sbInvoices.append(',');
                sbInvoices.append(invoice.getDate());
                sbInvoices.append(',');
                sbInvoices.append(invoice.getCustomerName());
                sbInvoices.append(',');
                sbInvoices.append(invoice.getTotal());
                sbInvoices.append('\n');
            }
            writerInvoices.write(sbInvoices.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try (
                PrintWriter writerItems = new PrintWriter("InvoiceLine.csv")) {
            StringBuilder sbItems = new StringBuilder();
            for (Invoice invoice : invoicesList) {
                for (Item item : invoice.getItems()) {
                    sbItems.append(invoice.getNumber());
                    sbItems.append(',');
                    sbItems.append(item.getName());
                    sbItems.append(',');
                    sbItems.append(item.getPrice());
                    sbItems.append(',');
                    sbItems.append(item.getCount());
                    sbItems.append('\n');
                }
            }
            writerItems.write(sbItems.toString());
            isSuccessful = true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        return isSuccessful;
    }
    
    public boolean loadInvoicesFromCSVFiles (){
        boolean isSuccessful = false;
        
     // Create invoices list
        List<Invoice> invoicesList = new ArrayList();

        Path pathToInvoices = Paths.get("InvoiceHeader.csv");

        try (BufferedReader br = Files.newBufferedReader(pathToInvoices, StandardCharsets.US_ASCII)) {

            String line = br.readLine();

            while (line != null) {
                Invoice invoice = new Invoice();
                String[] attributes = line.split(",");
                invoice.setNumber(Integer.parseInt(attributes[0]));
                invoice.setDate(attributes[1]);
                invoice.setCustomerName(attributes[2]);
                invoice.setTotal(Double.parseDouble(attributes[3]));
                invoicesList.add(invoice);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Path pathToItems = Paths.get("InvoiceLine.csv");

        try (BufferedReader br = Files.newBufferedReader(pathToItems, StandardCharsets.US_ASCII)) {

            String line = br.readLine();

            while (line != null) {
                Item item = new Item();
                String[] attributes = line.split(",");
                // Get invoice index that this item will be added to
                item.setName(attributes[1]);
                item.setPrice(Double.parseDouble(attributes[2]));
                item.setCount(Integer.parseInt(attributes[3]));
                Invoice oldInvoice = null;
                for (Invoice x : invoicesList) {
                    System.out.println(x.getNumber());
                    System.out.println(Integer.parseInt(attributes[0]));
                    if (x.getNumber() == Integer.parseInt(attributes[0])) {
                        // get old one
                        oldInvoice = x;
                    }
                }
                // replace old one with new one
                int index = invoicesList.indexOf(oldInvoice);
                // remove from list
                invoicesList.remove(index);
                oldInvoice.addItem(item);
                // add into index
                invoicesList.add(index, oldInvoice);
                line = br.readLine();
            }
            isSuccessful = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
       
     InvoiceController.invoicesList = invoicesList;
     return isSuccessful;
    }
     
}
