package com.podata.projectk;

public class item {
    private final String title;
    private final String facility;
    private final String name;
    private final String address;
    private final String visitTime;
    private final String visitTime2;

    public item(String title, String facility, String name, String address, String visitTime, String visitTime2, String disinfection){
        this.title = title;
        this.facility = facility;
        this.name = name;
        this.address = address;
        this.visitTime = visitTime;
        this.visitTime2 = visitTime2;
    }

    public String getTitle() {
        return title;
    }
    public String getFacility() {
        return facility;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getVisitTime() {
        return visitTime;
    }
    public String getVisitTime2(){
        return visitTime2;
    }
}
