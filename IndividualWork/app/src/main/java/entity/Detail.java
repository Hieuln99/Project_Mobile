package entity;

public class Detail {
    public int getTrip_Id() {
        return trip_Id;
    }

    public void setTrip_Id(int trip_Id) {
        this.trip_Id = trip_Id;
    }

    public int getCost_Id() {
        return cost_Id;
    }

    public void setCost_Id(int cost_Id) {
        this.cost_Id = cost_Id;
    }

    public String getCost_name() {
        return cost_name;
    }

    public void setCost_name(String cost_name) {
        this.cost_name = cost_name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private int trip_Id;
    private int cost_Id;
    private String cost_name;
    private String cost;

    @Override
    public String toString() {
        return "Cost Name= " + cost_name + ' ' +
                ", Cost =" + cost ;
    }
}
