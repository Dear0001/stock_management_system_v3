package org.group3.pp.model;

import java.time.LocalDate;

public class ProductModels {
    private String name;
    private int qty;
    private double price;
    private LocalDate importedDate;

    public ProductModels(String name, int qty, double price, LocalDate importedDate) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.importedDate = importedDate;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getImportedDate() {
        return importedDate;
    }
}
