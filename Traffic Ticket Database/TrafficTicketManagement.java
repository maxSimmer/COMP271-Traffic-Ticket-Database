import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Finished searchForPlate method (Part A)
 * Could not get searchForPlate to print out the driver information (name)
 * Attempted mostFrequentViolations (Part B)
 * No idea if mostFrequentViolations runs properly
 * Partner coded w/ a student from the other section
 */

/**
 * Application for managing traffic tickets
 */
public class TrafficTicketManagement {


    /** Keyboard input for any method in this class */
    protected static Scanner keyboard = new Scanner(System.in);


    /** Hashmaps for principal data to track */
    protected static HashMap<String, Driver> drivers = new HashMap<>();
    protected static HashMap<String, Vehicle> vehicles = new HashMap<>();
    protected static HashMap<Integer, Violation> violations = new HashMap<>();
    protected static HashMap<Integer, TrafficTicket> trafficTickets = new HashMap<>();


    /**
     * TrafficTicketManagement
     */
    public static void main(String[] args) throws ParseException, IOException {
        // Load the data from storage to the hashmaps -- method is in FileOperations class
        FileOperations.loadData();
        // run mostFrequentViolations
        mostFrequentViolation();
        // Run the menu -- method is in MenuOperations class
        MenuOperations.menu();
    }  // method main


    /**
     * Write a ticket. Method checks if driver is already in database. If so,
     * the existing data are used; otherwise method asks for more information
     * about the driver. Same for vehicle.
     *
     * @throws ParseException
     * @throws IOException
     */
    public static void writeTicket() throws ParseException, IOException {
        // obtain drivers license
        String licenseNumber = AuxiliaryOperations.citedDriver();
        // obtain plate #
        String licensePlate = AuxiliaryOperations.citedPlate();
        // Obtain violation code
        int violationCode = AuxiliaryOperations.citedViolation();
        //take address
        String address = AuxiliaryOperations.citedAddress();
        // take date
        Date ticketDate = AuxiliaryOperations.citedDate();
        // ticket number
        int ticketNumber = FileOperations.maxTicketNumber++;
        // Create ticket object
        TrafficTicket trafficTicket = new TrafficTicket(ticketNumber,
                licenseNumber, licensePlate,
                violationCode,
                address,
                ticketDate);
        // Write tickets file
        FileOperations.writeToFile(FileOperations.TRAFFIC_TICKETS, trafficTicket);
    }


    /** Inactive method - do not implement */
    public static void editTicket() {}  // method editTicket


    /** Inactive method - do not implement */
    public static void searchForTicket() {}  // method searchForTicket


    /**
     * Looks up a driver by license # and reports findings
     */
    public static void searchForDriver() {
        // Get driver info
        System.out.printf("\nEnter a driver's license number: ");
        String driverLicenseNumber = keyboard.next();
        Driver driver = drivers.get(driverLicenseNumber);
        // Driver may not have a record
        if (driver == null) {
            System.out.printf("\nThere is no record for driver license number %s\n",
                    driver.driverLicenseNumber);
        } else {
            System.out.printf("\n%s belongs to %s %s",
                    driver.driverLicenseNumber, driver.firstName, driver.lastName);

            // Header for ticket report
            System.out.printf("\n\n\tTicket date and time\t\t\tLocation\t\t\t\tPlate\t\tViolation");
            // Counter for how many tickets this driver has
            int ticketCounter = 0;
            // Search every ticket in the database but look for the ones matching the entered driver license number
            for (Map.Entry<Integer,TrafficTicket> trafficTicketEntry: trafficTickets.entrySet()) {
                // Get the value V of the <K,V> pair stored in the trafficTickets hashmap;
                // Remember it's a trafficTicket object
                TrafficTicket trafficTicket = trafficTicketEntry.getValue();
                // Is this a ticket for the driver we are looking for?
                if (trafficTicket.driverLicenseNumber.equals(driver.driverLicenseNumber)) {
                    // This is a ticket for the driver we are looking at.
                    // Pull the strings we want to print in the report and pad them for left-flush.
                    String licensePlate = AuxiliaryOperations.pad(trafficTicket.licensePlate, FileOperations.licensePlateMaxLength);
                    String date = trafficTicket.date.toString();
                    String address = AuxiliaryOperations.pad(trafficTicket.address, FileOperations.addressMaxLength);
                    String violation = AuxiliaryOperations.pad(violations.get(trafficTicket.violationCode).violationDescription, FileOperations.violationDescriptionMaxLength);
                    ticketCounter++;
                    System.out.printf("\n\t%s\t%s\t\t%s\t%s", date, address, licensePlate, violation);
                }
            }
            if (ticketCounter == 0) {
                System.out.printf("\nNo tickets found.");
            }
            System.out.printf("\n");
        }
    }  // method searchForDriver

    /**
     * Created and finished searchForPlate method (Part A)
     * Used code from searchForDriver method
     * User enters plate number (used AP52915)
     * Using plate number we then display the model of the car
     * Further displayed the violations under that plate number
     * Could not get the driver information to display :(
     */
    public static void searchForPlate() {
        // plate info
        System.out.printf("Enter a plate number: ");
        String plateNumber = keyboard.next();
        Vehicle vehicle = vehicles.get(plateNumber);
        
        // plate number doesn't exist or has no record
        if (vehicle == null){
            System.out.printf("\nThere is no record for plate number %s\n",
                    vehicle.plateNumber);
        } else { // print car info related to plate number
            System.out.printf("\n%s belongs to a %s %s %s %s ",
                    vehicle.licensePlate, vehicle.color, vehicle.year, vehicle.make, vehicle.model);

            System.out.printf("\n\n\tTicket date and time\t\tLocation\t\t\tLicense Number\tViolation");

            // plate number -> car + license plate -> use license plate number to display the rest of the info

            int ticketCounter = 0;
                    // Search every ticket in the database but look for the ones matching the entered driver license number
            for (Map.Entry<Integer,TrafficTicket> trafficTicketEntry: trafficTickets.entrySet()) {
                // Get the value V of the <K,V> pair stored in the trafficTickets hashmap;
                // Remember it's a trafficTicket object
                TrafficTicket trafficTicket = trafficTicketEntry.getValue();
                        
                // Is this a ticket for the driver we are looking for?
                if (trafficTicket.licensePlate.equals(vehicle.licensePlate)) {
                    // This is a ticket for the driver we are looking at.
                    // Pull the strings we want to print in the report and pad them for left-flush.
                    String plate = AuxiliaryOperations.pad(trafficTicket.driverLicenseNumber, FileOperations.licensePlateMaxLength);
                    String date = trafficTicket.date.toString();
                    String address = AuxiliaryOperations.pad(trafficTicket.address, FileOperations.addressMaxLength);
                    String violation = AuxiliaryOperations.pad(violations.get(trafficTicket.violationCode).violationDescription, FileOperations.violationDescriptionMaxLength);
                    ticketCounter++;
                    System.out.printf("\n\t%s\t%s\t\t%s\t%s", date, address, plate, violation);
                }
            }
                if (ticketCounter == 0) {
                    System.out.printf("\nNo tickets found.");
                }
                System.out.printf("\n");
        }
    }  // method searchForPlate

    public static void mostFrequentViolation(){
        // check for violations
        if (violations == null){
            System.out.println("There are no violations present");
        } else {
            // new hashmap with (k, v) both integers 
            HashMap <Integer, Integer> violationMap = new HashMap<Integer, Integer>();
            // for loop to map through traffic tickets
            for (Map.Entry<Integer, TrafficTicket> trafficTicketEntry : trafficTickets.entrySet()){
                // new Traffic ticket
                TrafficTicket violationCurrentCount = trafficTicketEntry.getValue();
                // check current (k, v) values
                int containsViolation = violationCurrentCount.violationCode;
                if(violationMap.containsKey(containsViolation)){
                    // if violation exists in tickets, increment count
                    violationMap.put(containsViolation, violationMap.get(containsViolation + 1));
                } else { // else its the first violation so count is 1
                    violationMap.put(containsViolation, 1);
                }
            }
        }
        // return the most frequent violations
        // which element shows up the most
         int violatonCode = 0;
         // counter
         int count = 1;
         // create entry set of the violationCountMap
         HashMap <Integer, Integer> violationMap = new HashMap<Integer, Integer>();
         Set<Map.Entry<Integer, Integer>> entrySet = violationMap.entrySet();
         // for loop to iterate through (k, v) values
         for (Map.Entry<Integer, Integer> entry : entrySet) {
             // if violation appearance is greater than 1
             if(entry.getValue() > count) {
                 // change element to the most frequent element so far
                 violatonCode = entry.getKey();
             }
         }
         // pprint out which violation appears the most
         System.out.println("\nThis violation appears the most: " + violatonCode);
    } // method mostFrequentViolations


    /** Inactive method - do not implement */
    public static void searchForViolation() {}  // method searchForViolation

}  // class TrafficTicketManagement