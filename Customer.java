import java.text.DecimalFormat; // Need this for two decimal places
public class Customer { // protected so child classes can access it
    protected String fName;
    protected String lName;
    protected String guestID;
    protected double moneySpent;

    public Customer(){ // default constructor
        fName = "";
        lName = "";
        guestID = "";
        moneySpent = 0.0;
    }

    public Customer(String ID, String F, String L, double m){ // overloaded constructor
        fName = F;
        lName = L;
        guestID = ID;
        moneySpent = m;
    }
    public double getMoney(){
        return moneySpent;
    } // Getter
    public String getGuestID(){
        return guestID;
    } // Getter
    public String getfName(){return fName;} // Getter
    public String getlName(){return lName;} // Getter
    public void spentMoney(double x){
        moneySpent+= x;
    } // Getter


    @Override
    public String toString(){ // Overridden toString method for base customer object
        DecimalFormat D = new DecimalFormat("0.00");
        return guestID + " " + fName + " " + lName + " " + D.format(moneySpent);
    }
}
