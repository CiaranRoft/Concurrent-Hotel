/*
 * Programmer - Ciaran Roft
 * Date - 30/11/2016
 * Description - The Hotel java program that will allow users to book a room/rooms for any number of days. 
 * It will also allow existing bookings to be updated, and canceled.
 * The constructor of the Hoel class take an array of rooms and adds them to the room_day hash map, creating the Hotel room available.  
 */
package concurrenthotel;

/**
 *
 * @author Roft
 * 
 */
import java.util.*;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hotel{
    
    Map<Integer, int[] > room_day = new HashMap(); // Key: Room Number  Value: Days
    Map<String, int[]> book_room = new HashMap(); // Key: Booking Reference Value: Room Number 
    Map<String, int[]> book_day = new HashMap(); // Key: Booking Reference Value: Days
    
    // Reentrant lock is a mutual exclusive lock, similar to implicit locking provided by the synchronised keyword in java
    private Lock lock = new ReentrantLock(); // Creating the Reentrant lock object lock.
    
    public void begin () throws InterruptedException{
     
        Thread customer1 = new Thread(new Runnable(){
            String bookingRef1 = "A"; // Booking Reference 
            int [] days = {4,5}; // Days the customer wants to book
            int [] room = {7,10}; // Room/s the customer wants to book
            public void run(){
                bookRooms(bookingRef1, days, room);
                //bookRooms(bookingRef1, days, rooms);
            }
        });
        
        Thread customer2 = new Thread(new Runnable(){
            String bookingRef1 = "B"; // Booking Reference 
            int [] days = {1,2}; // Days the customer wants to book
            int room = 7;// Room/s the customer wants to book
            public void run(){
                try{
                    updateBooking(bookingRef1,days,room); // Update existing booking
                }catch(NoSuchBookingException e){
                   System.out.println(e.getMessage());
                } 
               //bookRoom(bookingRef1, days, room); // Booking a room
            }   
        });
        
       /*Thread customer3 = new Thread(new Runnable(){
            String bookingRef1 = "A"; // Booking Reference 
            int [] days = {1,2,3}; // Days the customer wants to book
            int [] room = {50,100}; // Room/s the customer wants to book
            public void run(){
                try{
                    updateBooking(bookingRef1,days,room); // Updating a booking
                }catch(NoSuchBookingException e){
                   System.out.println(e.getMessage());
                }
                //bookRoom(bookingRef1, days, room); // Booking a room
            }
        });  */
        
        /*Thread customer4 = new Thread(new Runnable(){
            String bookingRef1 = "B"; // Booking Reference 
            int [] days = {5,6}; // Days the customer wants to book
            int [] room = {7,10,50}; // Room/s the customer wants to book
            public void run(){
                try{
                    updateBooking(bookingRef1,days,room); // Update a booking
                }catch(NoSuchBookingException e){
                   System.out.println(e.getMessage());
                }
                //bookRooms(bookingRef1, days, room); // Booking a room
            }
        }); */
        
        customer1.setName("Thread1");// Naming the thread
        customer1.start(); // Starting the thread
        customer1.join(); // All other thread pause execution until this thread terminates 
        
        customer2.setName("Thread2");// Naming the thread
        customer2.start(); // Starting the thread
        customer2.join(); // All other thread pause execution until this thread terminates 
        
        /*customer3.setName("Thread3");// Naming the thread
        customer3.start(); // Starting the thread
        customer3.join(); // All other thread pause execution until this thread terminates */
        
        /*customer4.setName("Thread4"); // Naming the thread
        customer4.start(); //Starting the thread
        customer4.join(); // All other thread pause execution until this thread terminates */
    }
    
    /**
     * The constructor that is passed an array of rooms. It then populates my room_day hash map with 
     * each of the Hotel rooms as the key and null as the values. Every room that is put in to this hash
     * then becomes available for a customer to book. A customer can only book a room that is put in to 
     * the hash map by the constructor.
     */
    public Hotel(int[] roomNums){
        // Creatiung the Hotels in my Hotel by adding the rooms to the room_day hash map
        for(int i = 0; i < roomNums.length; i++){
            room_day.put(roomNums[i], null);
        }
    }

    /************************************** Extra Credit Portion **************************************/
    /*Returns true if any of the rooms in roomNums is booked on any of the days specified in days otherwise return false.*/
    boolean roomsBooked(int[] days, int[] roomsNums){
        
        // If the room_day hasmap is empty the is no booking
        if(room_day.isEmpty()){
            return false;
        }
        
        // Loop to loop through the array of rooms
        for(int i = 0; i < roomsNums.length; i++){
            
            // If there is a key value pair in room day then there is a booking for that room
            if(room_day.get(roomsNums[i]) != null){ 
                int [] days_booked = room_day.get(roomsNums[i]); // Storing the days that the room is already booked for
                
                // Finding out what days the room is booked by comparing days_booked and days
                for(int j = 0; j < days_booked.length; j++){
                    for(int k = 0; k < days.length; k++){
                        // If one of the days is booked already return true                       
                        if(days_booked[j] == days[k]){
                            return true; // Returning so i cant stop checking as soon as i find a match
                        }
                    }
                }
            }
        }
        return false; // Return to say the rooms are not booked
    }
    
    /** 
     * Create booking reference bookignRef so that it now refers to the specified roomNums for each of the days specified in days.
     * Returns true if it is possible to book the rooms on the given days otherwise return false
    **/
    boolean bookRooms(String bookingRef, int[] days, int[] roomNums){
        //lock() locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed. Creating mutual exclusion within my program. 
        lock.lock();
        try{
            // Check to see if the room is already booked
            if(roomsBooked(days, roomNums) == true){
                System.out.println("\n" + Thread.currentThread().getName() + " Room is booked already book on one of the specified days.");
                return false;
            }
            for(int i = 0; i < roomNums.length; i++){
                // Ensuring that only the Hotel room is in the Hotel
                if(!room_day.containsKey(roomNums[i])){
                    System.out.println("\n"+Thread.currentThread().getName() + " Hotel room "+roomNums[i]+" does not exist.");
                    return false;
                }
                // Making a booking by filling in my three HashMaps
                else{                  
                    //If this is the first time a room is booked then just add it straight to the Hash Table
                    if(room_day.get(roomNums[i]) == null){
                        room_day.put(roomNums[i], days);
                    }
                    //If a room already contains a booking we need to add to the array
                    else{

                        int[] old_days = room_day.get(roomNums[i]); // Getting the days already stored in the Hash table
                        int size = (old_days.length) + (days.length); // Getting the size of the array for the new amount of days
                        int[] new_days = new int[size]; // Creating that array to store the new days
                     
                        System.arraycopy(old_days, 0, new_days, 0, old_days.length); // Copying the days already booked
                        System.arraycopy(days, 0, new_days, old_days.length, days.length); // Copying the new days to booked
                        room_day.put(roomNums[i],new_days); // Adding the new days to the hash table
                    }
                    book_room.put(bookingRef, roomNums); // Populating the book_room hash map   
                    book_day.put(bookingRef, days); // Populating the book_day hash map    
                }
            }
            printHash(bookingRef, days, roomNums); //Method to print the HashMaps in a readable format.s
        }finally{
            lock.unlock(); // Unlocking the Lock instance in finally block so no matter what happenes in the method the Lock will always be unlocked
        }        
        return true;
    }
    /**
     * Updates the booking with reference bookingRef so that it now refers to the specified roomNums for each of the days specified in days
     * Returns true if it is possible to update the booking, meaning that the new booking does not clash with an existing booking. 
     * Otherwise returns false and leaves the original booking unchanged. If there is no booking with the specified reference throws NoBookingException.
     */
    boolean updateBooking(String bookingRef, int[] days, int[] roomNums) throws NoSuchBookingException{
        //lock() locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed. Creating mutual exclusion within my program.
        lock.lock();
        try{          
            // Making sure the booking reference matches the booking to be updated.
            if(book_room.get(bookingRef) == null || book_day.get(bookingRef) == null){
                throw new NoSuchBookingException(bookingRef); // Throwing the NoSuchBooking exception
            }
            else{
                int [] old_days = book_day.get(bookingRef); // Storing the days that the booking refference has already booked
                int [] old_rooms = book_room.get(bookingRef); // Storing the rooms that the booking refference has already booked
                cancelBooking(bookingRef); // Canceling the previous booking.
                
                // If there is a booking that collides with the update rebook with the old booking
                if(roomsBooked(days, roomNums) == true){ 
                    System.out.println("\nSorry update not possible. Your old booking has been saved!");
                    bookRooms(bookingRef, old_days, old_rooms); // booking the room witht the old data
                    return false;
                }
                
            System.out.println("\n" + Thread.currentThread().getName() + " Updating booking reference " + bookingRef); 
            bookRooms(bookingRef, days, roomNums); // Creating the new booking.
            System.out.println("\n"+Thread.currentThread().getName() + " Booking reference " + bookingRef +" has been updated.");
           }          
        }finally{
            lock.unlock(); // Unlocking the Lock instance in finally block so no matter what happenes in the method the Lock will always be unlocked
        }
        return true;
    }
    /**
     * Cancels the booking with reference bookingRef. The room booked under this booking reference become unbooked for the 
     * days of the booking. If there is no booking with the specified reference throws NoSuchBookingException.
     **/
    void cancelBooking(String bookingRef) throws NoSuchBookingException{
        
        // Making sure there is a booking with the specifed booking reference.
        if(book_room.get(bookingRef) == null || book_day.get(bookingRef) == null){
            throw new NoSuchBookingException(bookingRef); // Throwing No bookind exception
        }

        // Canceling the booking
        else{
            //lock() locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed. Creating mutual exclusion within my program.
            lock.lock();
            try{
                System.out.println("\n"+Thread.currentThread().getName() + " Canceling booking reference " + bookingRef);
                int [] room = book_room.get(bookingRef); // Storing the room number of the booking
                // Looping for every room
                for(int x = 0; x < room.length; x++){
                    int [] days_to_delete = book_day.get(bookingRef); // Contains the days to be deleted
                    int [] overall_days = room_day.get(room[x]); // All of the days a room is booked for
                    
                    // Error checking to see if the Hotel room exists before trying to cancel a booking
                    if(overall_days == null){
                        System.out.println("\n"+Thread.currentThread().getName() + " Hotel room "+room[x]+" does not exist.");
                    }
                    
                    else{
                        int size = (overall_days.length - days_to_delete.length); // Calculating the size of the new days array
                        int [] new_days = new int[size]; // Array to hold the new days
                        int count = 0; // a counter variable
                        
                        //Looping for the full number of days
                        for(int i = 0; i < overall_days.length; i++){
                            boolean check = true; // Check variable to keep track of what days to add to the new days array
                            for (int j = 0; j < days_to_delete.length; j++){
                                // If a day is in the days to delete array Check is set to false
                                if(overall_days[i] == days_to_delete[j]){
                                    check = false; 
                                }
                            }
                            // Only if check is true will the day be added to the new days array
                            if(check){
                                new_days[count] = overall_days[i];
                                count++; // Count incremented everytime a new days is added
                            }
                        }
                        room_day.remove(room[x]); // Removing the room_day mapping matching the room numebr
                        
                        // If the booking being canceled is the only booking for that room then add null for the days the room is booked
                        if(new_days.length == 0){
                            room_day.put(room[x], null);
                        }
                        //If other booking have booked that room then add the days thoes booking have the room booked
                        else{
                            room_day.put(room[x], new_days);
                        }
                    }
                }
                // Removing everything to do with the booking reference 
                book_room.remove(bookingRef); 
                book_day.remove(bookingRef);
                // Print message to notify the user to a cancelation
                System.out.println(Thread.currentThread().getName() + " Booking reference " + bookingRef + " has been canceled.");
            }finally{
              lock.unlock();
            }
        }
    }
    /************************************** Non-Extra Credit Portion **************************************/
    
    /**
     * Returns true if the room roomNum is booked on any of the days specified in days otherwise return false
     **/
    public boolean roomBooked(int[] days, int roomNum){
        // If the room_day hasmap is empty return false
        if(room_day.isEmpty()){
            return false;
        }
        
        // If there are no days that matches the room number return false. If the room is not already booked
        if(room_day.get(roomNum) == null){
            return false;
        }
        
        int [] days_booked = room_day.get(roomNum); // Array to hold the days the room is booked
        
        // Finding out what days the room is booked by comparing the days_booked and days array
        for(int i = 0; i < days_booked.length; i++){
            for(int j = 0; j < days.length; j++){
                // If one of the days is booked already return true
                if(days_booked[i] == days[j]){
                    return  true; // Returning so i cant stop checking as soon as i find a match
                }
            }
        }
        return false;
    }
    /**
     * Create a booking with reference bookingRef for the room roomNum for each of the days specified in days.
     * Returns true if it is possible to book the room on the given days otherwise (if the room is booked) returns false
     **/
    
    boolean bookRoom(String bookingRef, int[] days, int roomNum){
        //lock() locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed. Creating mutual exclusion within my program.
        lock.lock();
        try{
            // Ensuring that only the Hotel room is in the Hotel
            if(!room_day.containsKey(roomNum)){
                System.out.println("\n"+Thread.currentThread().getName() + " Hotel room "+roomNum+" does not exist.");
                return false;
            }
            if(book_room.containsKey(bookingRef)){
                System.out.println("\n" + Thread.currentThread().getName() + " Booking refference already exists.");
                return false;
            }
            // Check to see if the room is already booked
            if(roomBooked(days, roomNum) == true){ 
                System.out.println("\n" + Thread.currentThread().getName() + " Room is booked already book on one of the specified days.");
                return false;
            }
            
            // Making a booking by filling in my three HashMaps
            else{
                //If it is the first booking for a room just add to the HashMap
                if(room_day.get(roomNum) == null){
                    room_day.put(roomNum, days);
                }
                // If a booking already exists for that room when need to add to the days array
                else{  
                    int[] old_days = room_day.get(roomNum); // Calculating the days the room is already booked
                    int size = (old_days.length) + (days.length); // Calculating the size of the new_days array
                    int[] new_days = new int[size]; // Initializing array
                    
                    System.arraycopy(old_days, 0, new_days, 0, old_days.length); // Copying over the array of old days
                    System.arraycopy(days, 0, new_days, old_days.length, days.length); // Coying over the array of new days
                    room_day.put(roomNum,new_days); // Ad the new days array to the Hashmap
                }
                
                int [] roomNums = {roomNum};// The room number has to be an array before getting added to the Hashmap
                book_room.put(bookingRef, roomNums); //Updating hashmap         
                book_day.put(bookingRef, days); //Updating hashmap 
                
                printHash(bookingRef, days, roomNums); //Method to print the HashMaps in a readable format.
            }
        }finally{
            lock.unlock(); // Unlocking the Lock instance in finally block so no matter what happenes in the method the Lock will always be unlocked
        }        
        return true;
    }
    
    /**
     * Updates the booking with reference bookingRef so that it now refers to the specified roomNum for each of the days
     * specified in days. Returns true if it is possible to update the booking (the new booking does not clash with an existing booking)
     * otherwise returns false and leaves the original booking unchanged If there is no booking with the specified 
     * reference throws NoSuchBookingException 
     */
    boolean updateBooking(String bookingRef, int[] days, int roomNum) throws NoSuchBookingException{
        //lock() locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed. Creating mutual exclusion within my program.
        lock.lock();
        try{
                       
            // Making sure the booking reference matches the booking to be updated.
            if(book_room.get(bookingRef) == null || book_day.get(bookingRef) == null){
                throw new NoSuchBookingException(bookingRef); // Throwing NoSuchBooking exception
            }
            else{
                int [] old_days = book_day.get(bookingRef); // Storing the days associated with that booking reference
                int [] old_room = book_room.get(bookingRef); // Storing the room associated with that booking reference
                
                cancelBooking(bookingRef); // Canceling the previous booking.
                
                // If there is a booking that collides with the update rebook with the old booking
                if(roomBooked(days, roomNum) == true){
                    System.out.println("\nThe room is already booked for one of the days");
                    bookRoom(bookingRef, old_days, old_room[0]);
                    return  false;
                }
                
                System.out.println("\n" + Thread.currentThread().getName() + " Updating booking reference " + bookingRef);              
                bookRoom(bookingRef, days, roomNum); // Creating the new booking.
                System.out.println(Thread.currentThread().getName() + " Booking reference " + bookingRef +" has been updated.");
            }         
            
        }finally{
            lock.unlock(); // Unlocking the Lock instance in finally block so no matter what happenes in the method the Lock will always be unlocked
        }
        return true;
    }
    
    // A method to print out the HashMaps nicely formatted. Gives the user some feedback of bookings, usefull for debugging.
    void printHash(String bookingRef, int[] days, int [] roomNum){
        int [] days_room_booked = new int[roomNum.length];
        for(int i = 0; i < roomNum.length; i++){
            days_room_booked = room_day.get(roomNum[i]);
        }
         // Getting the array of days that a room is booked
        int [] rooms_booked = book_room.get(bookingRef); // Getting the room a booking reference has booked
        int [] days_bookignRef_booked = book_day.get(bookingRef); // Getting the days a booking reference has booked

        // Printing out the messages seen by the user. Using the Arrays.toString to print the array in one go. No loop required
        System.out.println("\n" + Thread.currentThread().getName() + " Booking refference " + bookingRef + " has booked room " + Arrays.toString(rooms_booked) + " on days" + Arrays.toString(days_bookignRef_booked));
        System.out.println(Thread.currentThread().getName() + " Rooms " + Arrays.toString(rooms_booked) + " are booked on Days " + Arrays.toString(days_room_booked));    
    }
}