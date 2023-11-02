import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {
    public static double getCost(char S,String Drank, double sip, int quant){ // char Size, String TypeOfDrink, double Square Inch Price, int Quantity
        double Cost = 0; // Initialize variable
        if(S == 'S'){ // If the drink is size Small
            Cost = 2*Math.PI*2*4.5 * sip; // Surface area * Square Inch Price (SIP)
            if(Drank.equals("punch")){
                Cost+=(0.15 * 12); // Drink Type * Drink Size
            }
            else if(Drank.equals("soda")){
                Cost+=(0.2 * 12); // Drink Type * Drink Size
            }
            else{
                Cost+=(0.12 * 12); // Drink Type * Drink Size
            }
        }
        else if(S == 'M'){ // If the drink size is Medium
            Cost = 2*Math.PI*2.25*5.75 * sip; // Surface area * Square Inch Price (SIP)
            if(Drank.equals("punch")){
                Cost+=(0.15 * (20)); // Drink Type * Drink Size
            }
            else if(Drank.equals("soda")){
                Cost+=(0.2 * 20); // Drink Type * Drink Size
            }
            else{
                Cost+=(0.12 * 20); // Drink Type * Drink Size
            }
        }
        else{
            Cost = 2*Math.PI*2.75*7 * sip; // Surface area * Square Inch Price (SIP)
            if(Drank.equals("punch")){
                Cost+=(0.15 * (32)); // Drink Type * Drink Size
            }
            else if(Drank.equals("soda")){
                Cost+=(0.2 * 32); // Drink Type * Drink Size
            }
            else{
                Cost+=(0.12 * 32); // Drink Type * Drink Size
            }
        }
        return Cost * quant; // Cost is cost of one, multiply by quantity for final cost of Order
    }
    public static Customer[] loadCustomerData(String reg) throws FileNotFoundException{
        File A = new File(reg); // String reg is the name of the file being loaded in
        if(!(A.exists())){
            return null; // If the file does not exist, the array points to null
        }
        else{ // Otherwise, begin to read in values to create the array
            Scanner in = new Scanner(A); // Scanner to read from File A
            int S = sizeOF(A); // Read number of lines in File
            Customer[] Reg= new Customer[S]; // Declare an Array of Customer type with siae S
            for(int i = 0; i < S; i++){ // Iterate through all lines within the file
                String Parse = in.nextLine(); // Read in string to be parsed
                String[] data = Parse.split(" "); // Parse the string at every space, storing in String array
                if(data.length == 4){ // If there are only 4 fields,no discount % or bonus bucks
                    Reg[i] = new Customer(data[0], data[1], data[2], Double.parseDouble(data[3]));
                }
                else if(data[4].contains("%")){ //Only gold customers have a percentage sign in their line being parsed
                    data[4] = data[4].substring(0, data[4].length() - 1); // remove the % symbol to make the string convertable
                    Reg[i] = new Gold(data[0], data[1], data[2], Double.parseDouble(data[3]), Double.parseDouble(data[4]));
                }
                else{ // No % symbol and more than 4 fields, this is a Platinum customer
                    Reg[i] = new Platinum(data[0], data[1], data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]));
                }
            }
            return Reg; // after reading data from file, return populated array
        }
    }
    public static boolean isAllDigits(String str) {
        // String must follow this format to be legal by documentation standards
        return str.matches("^\\d*\\.?\\d+$");
    }
    public static Customer[][] buildOrders(String orders, Customer[] Reg, Customer[] Pref)throws FileNotFoundException {
        File C = new File(orders); // Create Older file to be read in from
        Scanner in = new Scanner(C); // Read in file from Order File
        Customer[][] Both = {Reg, Pref}; // 2D array to return to main to maintain data
        while(in.hasNextLine()){ // So long as there are orders to process
            String D = in.nextLine(); // Read in line containing order
            String [] OCD = D.split(" "); // Parse the line
            Boolean Valid = false; // Initialize Booleans to help with flow of program
            Boolean inReg = false; // Initialize Booleans to help know what Array value was found in, if any
            int foundAt = 0; // theoertical index that the Customer was found at
            if(OCD.length != 5){ // Incorrect amount of fields, invalid line
                continue;
            }
            for(int i = 0; i < Reg.length; i++){ // Check if the customer is in the Regular Customer Array
                if (Reg[i].getGuestID().equals(OCD[0])){  // If guest ID found
                    Valid = true; // Line is valid thus far
                    inReg = true; // The customer was found in Regular array
                    foundAt = i; // The customer was found at i Index
                }
            }
            if(!Valid && Pref != null){ // If the guestID was NOT found and Preferred Customer Array is NOT empty
                for(int i = 0; i < Pref.length; i++){ // iterate through Preferred Customer Array
                    if (Pref[i].getGuestID().equals(OCD[0])){ // If guest ID found
                        Valid = true; // Line is valid thus far
                        foundAt = i; // The customer was found at i Index
                    } // No line updating inReg since that was defaulted to False
                }
            }
            if(!Valid){ // If the customer was not found in neither Regular nor Preferred arrays
                continue;
            }
            if (!(OCD[1].equals("S") || OCD[1].equals("M") || OCD[1].equals("L"))){ // Invalid size
                continue;
            }
            if(!(OCD[2].equals("punch") || OCD[2].equals("soda") || OCD[2].equals("tea"))){ // Invalid drink
                continue;
            }
            if(!(isAllDigits(OCD[3]))){ // Invalid cost per square inch
                continue;
            }
            if(Double.parseDouble(OCD[4]) % 1 != 0){ // check if the value is an integer, cant have 0.75 drink
                continue;
            }
            Both = ProcessOrders(OCD, Both, foundAt, inReg); // Process the order, returning 2D array
            inReg = false; // reset values
            Valid = false; // reset values
            Reg = Both[0]; // update Reg pointer for next iteration
            Pref = Both[1]; // update Pref pointer for next iteration
        }
        return Both; // return 2D array to main to maintain data
    }
    public static int sizeOF(File B)throws FileNotFoundException{
        Scanner in = new Scanner(B); // Read in from file B
        int count = 0; // Initialize Variable
        while(in.hasNextLine()){ // While there is a line to be read in the file
            count++; // increment the count of lines in file
            in.nextLine(); // Read that line in, so it does not get read twice
        }
        return count; // return number of lines
    }
    public static Customer[] resizeArray(Customer[] A){
        if(A == null){ // if Array is null
           return new Customer[1]; // make an array of size 1
        } // need to handle this if statement first since A,length would lead to NullPointerException
        Customer[] B = new Customer[A.length + 1]; // Increase size by 1
        for(int i = 0; i < B.length - 1; i++){ // Populate array till last element
            B[i] = A[i]; // Copy Array
        }
        return B; // return resized array
    }
    public static void writeReg(Customer[] B) throws FileNotFoundException { // Customer pointer since must point to both Gold & Platinum
        File out = new File("customer.dat"); // File we will be writing results to
        PrintWriter C = new PrintWriter(out); // Create PrintWriter to write to that file
        for(int i = 0; i < B.length; i++){ // Write every element within array
            C.println(B[i].toString()); // use overriden toString method
        }
        C.close(); // Close output file
    }
    public static void writePref(Customer[] B) throws FileNotFoundException {
        File out = new File("preferred.dat"); // File we will be writing results to
        PrintWriter C = new PrintWriter(out); // Create PrintWriter to write to that file
        if (B != null) { // If there are customers in the Preferred array
            for (int i = 0; i < B.length; i++) { // Write every element within array
                C.println(B[i].toString()); // use overriden toString method
            }
            C.close(); // Close output file
        }
    }
    public static Customer[] shortenArray(int index, Customer [] A){
        Customer[] Short = new Customer[A.length - 1]; // Create array that is one smaller
        int j = 0; // separate index for Short array
        for(int i = 0; i < A.length; i++){ // i will iterate through old array
            if (i == index){ // if i = index, ignore this element since it is the one being removed
                continue;
            }
            Short[j] = A[i]; // copy from bigger array
            j++; // iterate through smaller array
        }
        return Short; // return shortened array
    }
    public static Customer[][] ProcessOrders(String[] A, Customer[][] Both, int foundAt, boolean inReg){
        double total = getCost(A[1].charAt(0),A[2], Double.parseDouble(A[3]), Integer.parseInt(A[4])); // this function returns the total cost of the order
        Customer Ordering; // initialize a pointer
        if(inReg){ // if the Custmoer was found in the regular array
            Ordering = Both[0][foundAt]; // Pointer now points to that customer
            Ordering.spentMoney(total); // Increase the amount of money they spent by the cost of the present order
            if(Ordering.getMoney() > 200){ // If the regular customer has now spent enough to become a platinum customer
                Platinum lol = new Platinum(Ordering.getGuestID(), Ordering.getfName(), Ordering.getlName(), Ordering.getMoney(), 0); // Create Platinum Customer using overloaded constructor
                Both[0] = shortenArray(foundAt, Both[0]); // Remove customer from regular array
                Both[1] = resizeArray(Both[1]); // Create new space in Preferred array for new Platinum Customer
                Both[1][Both[1].length - 1] = lol; // Put new Platinum customer into the end of the Preferred Array
                lol.setBonusBucks(); // Calculate how many bonus bucks the customer earned from this transaction
            }
            else if(Ordering.getMoney() >= 50){ // If the customer has now spent enough to become a gold customer
                Gold lol = new Gold(Ordering.getGuestID(), Ordering.getfName(), Ordering.getlName(), Ordering.getMoney(), 0); // Create Gold Customer using overloaded constructor, percentage at 0 for now
                Both[0] = shortenArray(foundAt, Both[0]); // Remove customer from regular array
                Both[1] = resizeArray(Both[1]); // Create new space in Preferred array for new Gold Customer
                Both[1][Both[1].length - 1] = lol; // Put new Gold customer into the end of the Preferred Array
                lol.checkPerc(); // Update customer percentage from prior defaulted value of 0
            }
        }
        else{ // If the customer was not found in regular
            Ordering = Both[1][foundAt]; // Pointer to that new customer
            if(Ordering instanceof Gold){ // If the customer is a Gold customer, apply discount percentage
                Gold Orderer = (Gold) Ordering; // Cast the customer to be a Gold Customer to be able to invoke Gold Methods
                Ordering.spentMoney(total * (1-Orderer.getPerc())); // Calculate money spent, accounting for discount percentage
                Orderer.checkPerc(); // Check and update customer percentage if they got promoted
                if(Orderer.getPerc() < 0){ // Discount Percentage will be -1 if the Gold Customer is ready to be promoted
                    Platinum B = new Platinum(Orderer); // Update the Customer to be a Platinum Customer via overloaded constructor
                    B.setBonusBucks(); // Calculate how many bonus bucks the customer earned from this transaction
                    Both[1][foundAt] = B; // Since Gold became Platinum, only need to update element and not change size of Array
                }
            }
            else{ // If the customer is a Platinum customer
                Platinum Orderer = (Platinum) Ordering; // Cast the customer to be a Platinum Customer to be able to invoke Platinum Methods
                if(Orderer.getB() > Math.ceil(total)){ // Does the customer have enough Bonus Bucks to make the order Free?
                    int newB = Orderer.getB() - (int)Math.ceil(total); // Does the customer have left over Bonus Bucks after the order?
                    Orderer.setB(newB); // Update value of Bonus Bucks
                }
                else{ // Order cannot be taken free
                    total -= Orderer.getB(); // new total accounting for use of Bonus bucks
                    int Post = (int)((Orderer.getMoney() + total)/5); // Track amout of bonus bucks including present transaction
                    int Pre = (int)((Orderer.getMoney()/5)); // Track amount of bonus bucks PRIOR to current transaction
                    Post-=Pre; // Calculate net increase in Bonus bucks
                    Orderer.spentMoney(total); // Update customer's total money spent
                    Orderer.setB(Post); // Update value fo Bonus bucks
                }
            }
        }
    return Both; // Return 2D array to main to maintain data
}
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in); // initialize Scanner to read in file names
        String reg, pref, orders; // intiialize strings to hold file names
        reg = in.nextLine(); // read in Regular Customer file name
        pref = in.nextLine(); // read in Preferred Customer file name
        orders = in.nextLine(); // read in Order file name
        Customer[] Regulars = loadCustomerData(reg); // Load regular customers into array
        Customer[] Preferred = loadCustomerData(pref); // Load preferred customers into array
        Customer[][] BigOne = buildOrders(orders, Regulars, Preferred); // Catch 2D array to maintain data
        Regulars = BigOne[0]; // Update Regular pointer
        Preferred = BigOne[1]; // Update Preferred pointer
        writeReg(Regulars); // Write updated data to Regular Customer file
        writePref(Preferred); // Write updated data to Preferred Customer file
        // Gold customer is done via paring the string but platinum is done via customer object, be consistent
        // Could get rid of Regulars & Preferred and just say Big[0] and Big[1]
        // Do not forget to do comments on classes
    }
}
