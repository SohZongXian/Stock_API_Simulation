
package com.mycompany.stock;


public class stock {
    private String symbol;
    private String identifier;
    private String open;
//    private int dayHigh;
//    private int dayLow;
//    private int lastPrice;
//    private int previousClose;
//    private int change;
//    private int pchange;
//    private int totalTradedVolume;
//    private int totalTradedValue;
//    private String lastUpdateTime;
    private String yearHigh;
    private String yearLow;
//    private int perChange365d;
//    private int perChange30d;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
    
    public double getVolatility()
    {
        double openValue = Double.parseDouble(this.open);
        double yearHighValue = Double.parseDouble(this.yearHigh);
        double yearLowValue = Double.parseDouble(this.yearLow);
//        System.out.println(((yearHighValue - openValue) + (openValue - yearLowValue))/2);
        
        return ((yearHighValue - openValue) + (openValue - yearLowValue))/2;
    }
    
    public double getGrowth()
    {
        double openValue = Double.parseDouble(this.open);
        double yearLowValue = Double.parseDouble(this.yearLow);
        double compute = (openValue - yearLowValue)/openValue*100;
        
        return compute;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(String yearHigh) {
        this.yearHigh = yearHigh;
    }

    public String getYearLow() {
        return yearLow;
    }

    public void setYearLow(String yearLow) {
        this.yearLow = yearLow;
    }


}
