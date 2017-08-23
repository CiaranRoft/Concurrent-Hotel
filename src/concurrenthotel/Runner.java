/*
 * Programmer - Ciaran Roft
 * Date - 30/11/2016
 * Description - The Runner.java class contains the main method of my program that starts of the execution of the program by passing a room array
 * to the constructor creating my Hotel with the desired rooms. 
 */
package concurrenthotel;
/**
 *
 * @author Roft
 */
public class Runner {
    public static void main (String [] args){
        // Creating a room array to pass to the constructor
        int[] rooms = {10, 50, 7, 100}; 
        
        try {
            new Hotel(rooms).begin(); // Passing the room array to the constructor to create a Hotel with our desired rooms
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
