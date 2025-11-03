package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Booking;
import model.BookingStatus;
import repo.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Get all bookings
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        // (แก้ไข) เรียก findAll ที่เรา Override ไว้ใน Repository
        return bookingRepository.findAll(); 
    }

    // Get booking by ID
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Get bookings by user ID
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserIdWithDetails(userId);
    }

    // Get bookings by machine ID
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByMachineId(Long machineId) {
        return bookingRepository.findByMachineId(machineId);
    }

    // Get bookings by status
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    // Get booking statistics
    public Long getTotalBookings() {
        return bookingRepository.count();
    }

    public Long getBookingCountByStatus(BookingStatus status) {
        return (long) bookingRepository.findByStatus(status).size();
    }

    // (เมธอดนี้ถูกต้องแล้ว)
    @Transactional
    public Booking createBooking(Booking booking) {
        
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (booking.getMachine() == null || booking.getMachine().getId() == null) {
            throw new IllegalArgumentException("Machine ID is required");
        }
        if (booking.getBookingDate() == null) {
            throw new IllegalArgumentException("Booking date and time is required");
        }

        boolean isSlotTaken = bookingRepository.existsActiveBookingForMachineAtTime(
            booking.getMachine().getId(),
            booking.getBookingDate()
        );

        if (isSlotTaken) {
            throw new IllegalStateException("This time slot for this machine is already taken.");
        }
        
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING); 
        }
        
        return bookingRepository.save(booking);
    }


    // Update booking
    @Transactional
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));

        if (bookingDetails.getBookingDate() != null) {
            booking.setBookingDate(bookingDetails.getBookingDate());
        }
        if (bookingDetails.getStatus() != null) {
            booking.setStatus(bookingDetails.getStatus());
        }
        if (bookingDetails.getAmount() != null) {
            booking.setAmount(bookingDetails.getAmount());
        }
        if (bookingDetails.getService() != null) {
            booking.setService(bookingDetails.getService());
        }
        return bookingRepository.save(booking);
    }

    // Delete booking
    @Transactional
    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // (เมธอดนี้ถูกต้องแล้ว)
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findActiveBookingsByDateRangeWithMachine(startDate, endDate);
    }

    // Mark booking as in progress
    @Transactional
    public Booking startBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.IN_PROGRESS);
            return bookingRepository.save(booking);
        } else {
            throw new IllegalStateException("Only confirmed bookings can be started");
        }
    }

    // Mark booking as completed
    @Transactional
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            booking.setStatus(BookingStatus.COMPLETED);
            return bookingRepository.save(booking);
        } else {
            throw new IllegalStateException("Only in-progress bookings can be completed");
        }
    }

    // --- ⬇️⬇️⬇️ นี่คือเมธอดที่แก้ไข ⬇️⬇️⬇️ ---
    // Get completed bookings for a user that can be rated
    @Transactional(readOnly = true)
    public List<Booking> getCompletedBookingsForRating(Long userId) {
        // (เปลี่ยนไปเรียกเมธอดใหม่ที่มี JOIN FETCH)
        return bookingRepository.findByUserIdAndStatusWithDetails(userId, BookingStatus.COMPLETED);
    }
    // --- ⬆️⬆️⬆️ จบส่วนที่แก้ไข ⬆️⬆️⬆️ ---

    // Get all completed bookings
    @Transactional(readOnly = true)
    public List<Booking> getCompletedBookings() {
        return bookingRepository.findByStatus(BookingStatus.COMPLETED);
    }

    // Get in-progress bookings
    @Transactional(readOnly = true)
    public List<Booking> getInProgressBookings() {
        return bookingRepository.findByStatus(BookingStatus.IN_PROGRESS);
    }
}