$(document).ready(function() {
    let currentConcertId = null;
    let selectedSeatId = null;
    
    // Backend API base URL - assuming backend runs on port 8081
    const API_BASE_URL = 'http://localhost:8081';

    // Load concerts when page loads
    loadConcerts();

    // ========================================
    // TASK 1: Load Concerts (20 points) - EASY VERSION
    // ========================================
    // TODO: Complete the AJAX call to fetch concerts from the API
    // API Endpoint: GET http://localhost:8081/concerts
    // 
    // HINT: Use this structure:
    // $.ajax({
    //     url: 'YOUR_URL_HERE',
    //     method: 'GET',
    //     dataType: 'json',
    //     success: function(concerts) {
    //         // Call displayConcerts with the concerts data
    //     },
    //     error: function(xhr, status, error) {
    //         // Show error message
    //     }
    // });
    function loadConcerts() {
        $.ajax({
            url: `${API_BASE_URL}/concerts`,  // TODO: Replace with correct URL
            method: 'GET',
            dataType: 'json',
            success: function(concerts) {
                // TODO: Call displayConcerts() function with concerts parameter
            },
            error: function(xhr, status, error) {
                // TODO: Show error message using $('#concerts-list').html()
                // Example: $('#concerts-list').html('<p class="error">Error message here</p>');
            }
        });
    }

    // ========================================
    // TASK 2: Display Concerts (20 points) - EASY VERSION
    // ========================================
    // TODO: Complete the displayConcerts() function to show concerts in the UI
    // 
    // HINT: Use this template for each concert card:
    // const concertCard = $(`
    //     <div class="concert-card" data-concert-id="${concert.id}">
    //         <h3>${concert.title}</h3>
    //         <p><strong>Performer:</strong> ${concert.performer}</p>
    //         <p><strong>Date:</strong> ${formatDate(concert.date)}</p>
    //         <button class="btn btn-primary view-seats-btn" data-concert-id="${concert.id}">
    //             View Seats
    //         </button>
    //     </div>
    // `);
    function displayConcerts(concerts) {
        const concertsList = $('#concerts-list');
        concertsList.empty();

        if (concerts.length === 0) {
            // TODO: Show "No concerts available" message
            concertsList.html('<p>No concerts available.</p>');
            return;
        }

        concerts.forEach(function(concert) {
            // TODO: Create concert card HTML using the template above
            // TODO: Add the card to concertsList using concertsList.append()
        });
    }

    // Helper function to format date - already implemented
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    // ========================================
    // TASK 3: Load and Display Seats (25 points) - EASY VERSION
    // ========================================
    // TODO: Complete the loadSeats() function to fetch seats for a specific concert
    // API Endpoint: GET http://localhost:8081/concerts/{concertId}/seats
    // 
    // HINT: Use similar structure as loadConcerts() but with different URL
    function loadSeats(concertId) {
        $.ajax({
            url: `${API_BASE_URL}/concerts/${concertId}/seats`,  // TODO: Complete the URL
            method: 'GET',
            dataType: 'json',
            success: function(seats) {
                // TODO: Call displaySeats() function with seats parameter
                // TODO: Hide concerts section: $('#concerts-section').hide()
                // TODO: Show seats section: $('#seats-section').show()
                // TODO: Store concert ID: currentConcertId = concertId
            },
            error: function(xhr, status, error) {
                // TODO: Show error alert: alert('Error loading seats. Please try again.');
            }
        });
    }

    // TODO: Complete the displaySeats() function to show seats organized by zones
    // 
    // HINT: Use this code to group seats by zone:
    // const seatsByZone = {};
    // seats.forEach(function(seat) {
    //     if (!seatsByZone[seat.zone]) {
    //         seatsByZone[seat.zone] = [];
    //     }
    //     seatsByZone[seat.zone].push(seat);
    // });
    function displaySeats(seats) {
        const seatsContainer = $('#seats-container');
        seatsContainer.empty();

        // TODO: Group seats by zone using the hint above

        // TODO: For each zone, create seat layout
        // HINT: Use Object.keys(seatsByZone).forEach(function(zone) { ... })
        // Inside the loop:
        // 1. Create zone section: const zoneDiv = $(`<div class="zone-section"><h4>${zone} Zone</h4><div class="seats-grid"></div></div>`)
        // 2. Get seats grid: const seatsGrid = zoneDiv.find('.seats-grid')
        // 3. For each seat in the zone, create a button:
        //    const seatClass = seat.booked ? 'seat booked' : 'seat available'
        //    const seatButton = $(`<button class="${seatClass}" data-seat-id="${seat.id}" data-seat-number="${seat.number}" data-zone="${seat.zone}" ${seat.booked ? 'disabled' : ''}>${seat.number}${seat.booked ? '<br><small>Booked</small>' : ''}</button>`)
        //    seatsGrid.append(seatButton)
        // 4. Add the zone to container: seatsContainer.append(zoneDiv)
    }

    // ========================================
    // TASK 4: Seat Selection and Booking (25 points) - EASY VERSION
    // ========================================
    // TODO: Complete the booking form submission handler
    // API Endpoint: POST http://localhost:8081/concerts/{concertId}/seats/{seatId}/book
    // Request body: { attendee: attendeeName }
    // 
    // HINT: Use this structure for the AJAX call:
    // $.ajax({
    //     url: `${API_BASE_URL}/concerts/${currentConcertId}/seats/${selectedSeatId}/book`,
    //     method: 'POST',
    //     contentType: 'application/json',
    //     data: JSON.stringify({ attendee: attendeeName }),
    //     success: function(response) { ... },
    //     error: function(xhr, status, error) { ... }
    // });
    $('#booking-form').on('submit', function(e) {
        e.preventDefault();
        
        // TODO: Get attendee name: const attendeeName = $('#attendee-name').val().trim()
        // TODO: Validate: if (!attendeeName) { alert('Please enter your name.'); return; }
        // TODO: Validate: if (!selectedSeatId || !currentConcertId) { alert('Please select a seat first.'); return; }
        
        // TODO: Make AJAX call to book seat using the hint above
        // TODO: On success: alert('Seat booked successfully!'), hide modal, reset form, reload seats
        // TODO: On error: alert('Error booking seat. Please try again.')
    });

    // ========================================
    // TASK 5: Event Handlers (10 points) - EASY VERSION
    // ========================================
    // TODO: Complete the "View Seats" button click handler
    $(document).on('click', '.view-seats-btn', function() {
        // TODO: Get concert ID: const concertId = $(this).data('concert-id')
        // TODO: Call loadSeats() with the concert ID
    });

    // TODO: Complete the available seat click handler
    $(document).on('click', '.seat.available', function() {
        // TODO: Store selected seat ID: selectedSeatId = $(this).data('seat-id')
        // TODO: Get seat info: const seatNumber = $(this).data('seat-number'), const zone = $(this).data('zone')
        // TODO: Update modal info: $('#selected-seat-info').text(`${zone} Zone - Seat ${seatNumber}`)
        // TODO: Show modal: $('#booking-modal').show()
    });

    // TODO: Complete the "Back to Concerts" button handler
    $(document).on('click', '#back-to-concerts', function() {
        // TODO: Show concerts: $('#concerts-section').show()
        // TODO: Hide seats: $('#seats-section').hide()
        // TODO: Reset variables: currentConcertId = null, selectedSeatId = null
    });

    // TODO: Complete the modal close/cancel handler
    $(document).on('click', '.close, #cancel-booking', function() {
        // TODO: Hide modal: $('#booking-modal').hide()
        // TODO: Reset selected seat: selectedSeatId = null
    });

    // Close modal when clicking outside - already implemented
    $(window).on('click', function(e) {
        if (e.target.id === 'booking-modal') {
            $('#booking-modal').hide();
            selectedSeatId = null;
        }
    });
});
