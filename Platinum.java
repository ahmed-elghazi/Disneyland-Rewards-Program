import java.text.DecimalFormat;
public class Platinum extends Customer{
    private int bonusBucks; // private to be specific to Platinum class access only
    public Platinum(String ID, String F, String L, double m, int b){ // overloaded constuctor
        super(ID,F,L,m);
        bonusBucks = b;
    }
    public Platinum(Customer A){
        // overloaded constructor that takes in customer object
        super(A.guestID, A.fName, A.lName, A.moneySpent);
    }
    public int getB(){
        return bonusBucks;
    } // Getter
    public void setB(int B){
        bonusBucks = B;
    } // Getter
    public void setBonusBucks(){bonusBucks = (int) (moneySpent - 200)/5;} // calculate bonus bucks, cast to int, store

    @Override
    public String toString(){ // Overridden toString method for base customer object
        DecimalFormat D = new DecimalFormat("0.00");
        return guestID + " " + fName + " " + lName + " " + D.format(moneySpent) + " " + bonusBucks;
    }
}
