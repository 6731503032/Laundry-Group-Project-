# Lab - Fullstack

A hands-on exercise to implement a frontend for a concert booking system using HTML, CSS, jQuery, and AJAX.

## 🎯 Learning Objectives

By the end of this exercise, you will:
- Understand how to make AJAX calls using jQuery
- Learn to handle JSON responses from REST APIs
- Implement dynamic DOM manipulation
- Create responsive web interfaces
- Work with modal dialogs and form handling

## 📋 Exercise Overview

You will implement the frontend for a concert booking system that communicates with a backend API. The backend is already provided and runs on port 8081.

### What's Already Provided:
- ✅ Backend API with all endpoints
- ✅ Database with sample concert and seat data
- ✅ Basic HTML structure
- ✅ CSS styling
- ✅ jQuery library included

### What You Need to Implement:
- 🔲 AJAX calls to load concerts
- 🔲 Dynamic concert display
- 🔲 Seat selection functionality
- 🔲 Booking form handling
- 🔲 Error handling

## 🏗️ Project Structure

```
lab-web-fullstack/
├── backend/          # Backend API (port 8081) - PROVIDED
├── frontend/         # Frontend (port 8080) - YOU IMPLEMENT
│   ├── src/main/resources/static/
│   │   ├── index.html    # Basic structure - PROVIDED
│   │   ├── styles.css    # Styling - PROVIDED
│   │   └── app.js        # JavaScript - YOU IMPLEMENT
│   └── pom.xml
└── pom.xml
```

## 🚀 Getting Started

### Method 1: Run from VS Code Classes (Recommended)

#### Step 1: Start the Backend
1. Open `backend/src/main/java/th/mfu/App.java` in VS Code
2. Click the "Run" button above the `main` method, or press `Ctrl+F5`
3. The backend will start on port 8081

#### Step 2: Verify Backend is Running
- **Local VS Code**: Open http://localhost:8081/health
- **GitHub Codespaces**: Open https://your-codespace-name/health

You should see:
```
Backend is running on port 8081
```

#### Step 3: Start the Frontend
1. Open `frontend/src/main/java/th/mfu/frontend/FrontendApplication.java` in VS Code
2. Click the "Run" button above the `main` method, or press `Ctrl+F5`
3. The frontend will start on port 8080

#### Step 4: Access the Application
- **Local VS Code**: Open http://localhost:8080
- **GitHub Codespaces**: Open https://your-codespace-name



### 🌐 URL Differences

| Environment | Backend URL | Frontend URL |
|-------------|-------------|--------------|
| **Local VS Code** | http://localhost:8081 | http://localhost:8080 |
| **GitHub Codespaces** | https://your-codespace-8081.preview.app.github.dev | https://your-codespace-8080.preview.app.github.dev |

**Note**: In GitHub Codespaces, the URLs will be different. Look for the "Ports" tab in VS Code to see the exact URLs for your codespace.

## 📝 Exercise Tasks

### Task 1: Load Concerts 

**Objective**: Implement the `loadConcerts()` function to fetch concerts from the API.

**API Endpoint**: 
- Local: `GET http://localhost:8081/concerts`
- Codespaces: `GET https://your-codespace-8081.preview.app.github.dev/concerts`

**Your Task**: Complete the AJAX call in `app.js`:

```javascript
function loadConcerts() {
    $.ajax({
        url: 'http://localhost:8081/concerts',
        method: 'GET',
        dataType: 'json',
        success: function(concerts) {
            // TODO: Call displayConcerts function
        },
        error: function(xhr, status, error) {
            // TODO: Show error message to user
        }
    });
}
```

**Expected Result**: When you open http://localhost:8080, you should see 5 concert cards displayed.

### Task 2: Display Concerts 

**Objective**: Implement the `displayConcerts()` function to show concerts in the UI.

**Your Task**: Complete the function to create concert cards:

```javascript
function displayConcerts(concerts) {
    const concertsList = $('#concerts-list');
    concertsList.empty();

    if (concerts.length === 0) {
        // TODO: Show "No concerts available" message
        return;
    }

    concerts.forEach(function(concert) {
        // TODO: Create concert card HTML
        // Include: title, performer, date, and "View Seats" button
        // Use formatDate() function for date formatting
    });
}
```

**Hint**: Use template literals to create HTML:
```javascript
const concertCard = $(`
    <div class="concert-card" data-concert-id="${concert.id}">
        <h3>${concert.title}</h3>
        <p><strong>Performer:</strong> ${concert.performer}</p>
        <p><strong>Date:</strong> ${formatDate(concert.date)}</p>
        <button class="btn btn-primary view-seats-btn" data-concert-id="${concert.id}">
            View Seats
        </button>
    </div>
`);
```

### Task 3: Load and Display Seats 

**Objective**: Implement seat loading and display functionality.

**API Endpoint**: 
- Local: `GET http://localhost:8081/concerts/{concertId}/seats`
- Codespaces: `GET https://your-codespace-8081.preview.app.github.dev/concerts/{concertId}/seats`

**Your Task**: Complete the `loadSeats()` and `displaySeats()` functions:

```javascript
function loadSeats(concertId) {
    $.ajax({
        url: `http://localhost:8081/concerts/${concertId}/seats`,
        method: 'GET',
        dataType: 'json',
        success: function(seats) {
            // TODO: Call displaySeats function
            // TODO: Hide concerts section, show seats section
            // TODO: Store currentConcertId
        },
        error: function(xhr, status, error) {
            // TODO: Show error alert
        }
    });
}

function displaySeats(seats) {
    const seatsContainer = $('#seats-container');
    seatsContainer.empty();

    // TODO: Group seats by zone (Front, Middle, Rear)
    // TODO: Create seat layout for each zone
    // TODO: Handle booked vs available seats
}
```

**Hint**: Group seats by zone:
```javascript
const seatsByZone = {};
seats.forEach(function(seat) {
    if (!seatsByZone[seat.zone]) {
        seatsByZone[seat.zone] = [];
    }
    seatsByZone[seat.zone].push(seat);
});
```

### Task 4: Seat Selection and Booking 
**Objective**: Implement seat selection and booking functionality.

**API Endpoint**: 
- Local: `POST http://localhost:8081/concerts/{concertId}/seats/{seatId}/book`
- Codespaces: `POST https://your-codespace-8081.preview.app.github.dev/concerts/{concertId}/seats/{seatId}/book`

**Your Task**: Complete the booking form submission:

```javascript
$('#booking-form').on('submit', function(e) {
    e.preventDefault();
    
    // TODO: Validate selected seat and attendee name
    // TODO: Make AJAX call to book seat
    // TODO: Handle success and error responses
    // TODO: Refresh seat display after booking
});
```

**Hint**: The booking request body should be:
```javascript
{
    attendee: attendeeName
}
```

### Task 5: Event Handlers (10 points)

**Objective**: Implement click event handlers for user interactions.

**Your Task**: Add event handlers for:

```javascript
// TODO: Handle "View Seats" button clicks
$(document).on('click', '.view-seats-btn', function() {
    // TODO: Get concert ID and call loadSeats
});

// TODO: Handle available seat clicks
$(document).on('click', '.seat.available', function() {
    // TODO: Show booking modal with seat info
});

// TODO: Handle "Back to Concerts" button
$(document).on('click', '#back-to-concerts', function() {
    // TODO: Show concerts section, hide seats section
});

// TODO: Handle modal close/cancel
$(document).on('click', '.close, #cancel-booking', function() {
    // TODO: Hide modal and reset selected seat
});
```

## 🧪 Testing Your Implementation

### Test Cases:

1. **Load Concerts**: Page should display 5 concerts on load
2. **View Seats**: Clicking "View Seats" should show seat layout
3. **Seat Selection**: Clicking available seats should open booking modal
4. **Booking**: Entering name and confirming should book the seat
5. **Visual Feedback**: Booked seats should appear red, available seats green
6. **Navigation**: "Back to Concerts" should return to concert list

### Sample Data:
- **Concerts**: 5 concerts with different performers and dates
- **Seats**: Multiple seats per concert organized by zones (Front, Middle, Rear)
- **All seats start as available** (not booked)

## 🎨 UI Requirements

Your implementation should:
- ✅ Display concerts in a responsive grid
- ✅ Show seats organized by zones
- ✅ Use different colors for available (green) vs booked (red) seats
- ✅ Include a modal dialog for booking
- ✅ Show proper error messages
- ✅ Handle loading states

## 📚 API Reference

### Backend Endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/concerts` | Get all concerts |
| GET | `/concerts/{id}/seats` | Get seats for a concert |
| POST | `/concerts/{concertId}/seats/{seatId}/book` | Book a seat |
| GET | `/health` | Health check |

**Base URLs:**
- **Local**: `http://localhost:8081`
- **Codespaces**: `https://your-codespace-name`

### Sample Concert Object:
```json
{
  "id": 1001,
  "title": "Summer Music Festival",
  "performer": "The Cool Cats",
  "date": "2023-07-15"
}
```

### Sample Seat Object:
```json
{
  "id": 101,
  "number": "A1",
  "zone": "Front",
  "booked": false,
  "attendee": null
}
```

## 🏆 Success Criteria

Your implementation is complete when:
- [ ] All 5 concerts load and display correctly
- [ ] Clicking "View Seats" shows the seat layout
- [ ] Available seats are clickable and open booking modal
- [ ] Booking a seat works and updates the display
- [ ] Booked seats show as unavailable (red)
- [ ] Navigation between concerts and seats works
- [ ] Error handling works for network issues

## 🆘 Getting Help

### Common Issues:

1. **"Error loading concerts"**: 
   - Check if backend is running on port 8081
   - Verify you're using the correct URL (localhost vs codespace URL)
2. **CORS errors**: Backend is configured to allow all origins
3. **Seats not loading**: Verify concert ID is being passed correctly
4. **Booking not working**: Check that attendee name is being sent in request body
5. **URL issues in Codespaces**: 
   - Check the "Ports" tab in VS Code for the correct URLs
   - Make sure to use the codespace URL format: `https://your-codespace-8081.preview.app.github.dev`

### Debug Tips:
- Use browser developer tools (F12) to check console for errors
- Use Network tab to see AJAX requests and responses
- Add `console.log()` statements to debug your code

## 📝 Submission

Submit your completed `frontend/src/main/resources/static/app.js` file with all TODO items implemented.

**Good luck! 🎵🎫**