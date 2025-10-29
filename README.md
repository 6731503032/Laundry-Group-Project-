TO DO THIS WEEK(BOOKING AND TIMER PAGE)***(KEEP THE COLOR CONSISTENT FROM THE LOGIN PAGE)**:

BOOKING PAGE will be in columns(as shown in wireframe.png with 3 hours interval for each column) 
When the column booking is clicked, it takes you to confirm and fill form(Automated fill if possible from username)
<img width="609" height="374" alt="image" src="https://github.com/user-attachments/assets/15c66020-41d7-4611-af02-f0077d0ce2c6" />

After confirming the fill ---> Timer Page

<img width="494" height="243" alt="image" src="https://github.com/user-attachments/assets/2b2aeede-886d-4594-9432-06c422fe4d14" />


Tmer Page:
clock icon with countdown (15 mins) middle of the screen just like login(include in the dashboard or not, YOUR CHOICE)
Admin will have to approve the timer so it can complete and move forward.

The data will then be stored in a new database for the queue after approved by admin




After confirming, show and display your booked schedule in the dashboard.








-
-
-
-
-
-
-
--
-
-
-
-
-
-
-
-
-
--

private String studentId;

@Column(nullable = false)
private String name;

@Column(nullable = false, unique = true)
private String email;

@Column(nullable = false)
private String password;

@Enumerated(EnumType.STRING)
private Role role;
THINGS TO WORK ON:

Customer  Create new account Login Front page(Time-slot) Book slot page(which Machine)  Timer page after confirm Completion Page End​ Service Page(rating system)  Machine details Page

Admin The money is given Editing Page for Washing Machine Editing Page for Students booked Confirm Page

Database :  Booking Payment Confirmed Students Rating Registered Student

FRONTEND CODING: AJAX/THYMELEAF/REACTJS



