# Concurrent-Hotel

Author - Ciaran Roft
Date - 11/01/2017

# Runner Class

Here is where you will find the main method and an integer array called rooms.
The only part you might need to change in here is the array of rooms. You can enter
any amount of numbers to this array, please ensure that there is no duplicate room 
numbers into this array as i have not handled this senario.


# HotelClass

Each thread is built in the begin method of the Hotel program, it is inside the run 
method of each thread that a call to the methods to book, update or cancel a booking 
are made. 

At the top of each thread you should see a string booking refference, an integer array
for days, and an (iteger or integer array) depending if you wish to use the extra credit 
methods or not. Each method uses a combination or all of these as arguments when calling 
the methods as seen below.


# bookRooms() method 
Is called like this:
    bookRooms(bookingRef1, days, room)
Where bookingRefference is a string, days is an integer array, and room is an integer array.


# updateBooking() method
Is called like this:
    updateBooking(bookingRef1,days,room)

Where bookingRefference is a string, days is an integer array, and room is an integer array.
The call to update booking must be wraped in a try catch block because it throws a 
NoSuchBookingException. The difference between this update method and the update method used
for a single room is it is passed an array of days.


# Cancel Booking() method 
Is called like this: 
cancelBooking(bookingRef1)
Is only passed in a string booking refference, and is the same method is used wether you are
using an array of rooms or a single integer room.


# bookRoom 

Is called like this:
    bookRoom(bookingRef1, days, room)
Where bookingRefference is a string, days is an integer array, and integer room.

updateBooking() method 

Is called like this:
    updateBooking(bookingRef1,days,room)

Where bookingRefference is a string, days is an integer array, and an integer room.
The call to update booking must be wraped in a try catch block because it throws a 
NoSuchBookingException. 
