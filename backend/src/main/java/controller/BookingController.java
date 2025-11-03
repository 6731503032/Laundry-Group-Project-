package controller;

import java.time.LocalDateTime; // (ต้องมี Import นี้)
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Booking;
import model.BookingStatus;
import service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // (เมธอดอื่นๆ ... getAllBookings, getBookingById, ฯลฯ ... ไม่ต้องแก้ไข)
    // ...
    // ...

    // --- ⬇️⬇️⬇️ นี่คือเมธอดที่ต้องแก้ไข ⬇️⬇️⬇️ ---
    // Get bookings by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Booking>> getBookingsByDateRange(
            @RequestParam String startDate, // 1. เปลี่ยนจาก LocalDateTime เป็น String
            @RequestParam String endDate) {   // 2. เปลี่ยนจาก LocalDateTime เป็น String
        
        try {
            // 3. แปลง String เป็น LocalDateTime ด้วยตัวเอง
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);

            // 4. เรียกใช้ Service ด้วยข้อมูลที่แปลงแล้ว
            List<Booking> bookings = bookingService.getBookingsByDateRange(start, end);
            return ResponseEntity.ok(bookings);
            
        } catch (Exception e) {
            // (ถ้าแปลงค่าพลาด หรือมีปัญหาอื่น)
            e.printStackTrace(); // (ดู Error ใน Console)
            return ResponseEntity.badRequest().build();
        }
    }
    // --- ⬆️⬆️⬆️ จบส่วนที่แก้ไข ⬆️⬆️⬆️ ---


    // (เมธอดอื่นๆ ... startBooking, completeBooking, ฯลฯ ... ไม่ต้องแก้ไข)
    // ...
    // ...

    // (ส่วนที่เหลือของไฟล์)
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<Booking>> getBookingsByMachineId(@PathVariable Long machineId) {
        List<Booking> bookings = bookingService.getBookingsByMachineId(machineId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            List<Booking> bookings = bookingService.getBookingsByStatus(bookingStatus);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalBookings() {
        return ResponseEntity.ok(bookingService.getTotalBookings());
    }

    @GetMapping("/stats/by-status/{status}")
    public ResponseEntity<Long> getBookingCountByStatus(@PathVariable String status) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            long count = bookingService.getBookingCountByStatus(bookingStatus);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingDetails);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (bookingService.deleteBooking(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
 
    @PostMapping("/{id}/start")
    public ResponseEntity<?> startBooking(@PathVariable Long id) {
        try {
            Booking updatedBooking = bookingService.startBooking(id);
            if (updatedBooking != null) {
                return ResponseEntity.ok(updatedBooking);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage(), "INVALID_STATUS"));
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id) {
        try {
            Booking updatedBooking = bookingService.completeBooking(id);
            if (updatedBooking != null) {
                return ResponseEntity.ok(updatedBooking);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage(), "INVALID_STATUS"));
        }
    }

    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<List<Booking>> getCompletedBookingsForRating(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getCompletedBookingsForRating(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Booking>> getCompletedBookings() {
        List<Booking> bookings = bookingService.getCompletedBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/in-progress")
    public ResponseEntity<List<Booking>> getInProgressBookings() {
        List<Booking> bookings = bookingService.getInProgressBookings();
        return ResponseEntity.ok(bookings);
    }

    private Map<String, String> createErrorResponse(String message, String code) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("code", code);
        return error;
    }
}