package com.epay.validator.validator_epay.pojo;

/**
 * Created by AQSA SHaaPARR on 4/18/2017.
 */

public class TransactionData {
    public TransactionData(){}

    String date;

//    public String getRoute_added_date() {
//        return route_added_date;
//    }
//
//    public void setRoute_added_date(String route_added_date) {
//        this.route_added_date = route_added_date;
//    }

    String route_added_date;

    String Fare_Price;

    public String getTrans_id() {
        return Trans_id;
    }

    public void setTrans_id(String trans_id) {
        Trans_id = trans_id;
    }

    String Trans_id;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

    public String getFare_Price() {
        return Fare_Price;
    }

    public void setFare_Price(String fare_Price) {
        Fare_Price = fare_Price;
    }

    public String getRoute_destinition() {
        return route_destinition;
    }

    public void setRoute_destinition(String route_destinition) {
        this.route_destinition = route_destinition;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPersonTravelling() {
        return personTravelling;
    }

    public void setPersonTravelling(String personTravelling) {
        this.personTravelling = personTravelling;
    }
    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    String route_destinition;
    String routeStart;
    String time;
    String personTravelling;
    String customer_id;

    public TransactionData(String Route_destinition,
                           String fare_Price,
                           String RouteStart,
                           String Time,
                           String PersonTravelling,String customer_id){

        this.Fare_Price = fare_Price;
        this.time = Time;
        this.routeStart = RouteStart;
        this.route_destinition = Route_destinition;
        this.personTravelling = PersonTravelling;
        this.customer_id=customer_id;


    }
}
