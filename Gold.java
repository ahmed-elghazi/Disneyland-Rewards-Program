public class Gold extends Customer{
    private double perc; // private to be specific to Gold class access only
    public Gold(String ID, String F, String L, double m, double p){ // overloaded constuctor
        super(ID,F,L,m);
        perc = p;
    }
    public double getPerc(){
        return perc/100.0;
    } // Getter. divide by 100 since stored as number not %

    @Override
    public String toString(){
        // Overridden toString method for base customer object
        return super.toString() + " " + (int)perc + "%";
    }
    public void checkPerc(){ // Method to check for if customer is eligible for higher discount percentage
        if(this.getMoney() < 100){
            perc = 5;
        }
        if(this.getMoney() >= 100 && this.getMoney() <150){
            perc = 10;
        }
        else if(this.getMoney() >= 150 && this.getMoney() < 200){
            perc = 15;
        }
        else if(this.getMoney() >= 200){
            perc = -1;
        }
    }
}
