/*
    Hotel Room Booking System.
*/
package concurrenthotel;

public class NoSuchBookingException extends Exception {
    public NoSuchBookingException (String bookingRef) {
	super("\nThere is no booking with reference " + bookingRef);
    }
}
