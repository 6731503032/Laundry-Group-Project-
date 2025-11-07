package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.AppConstants; // (เพิ่ม)
import model.Booking;
import model.BookingStatus;
import model.Machine; // (เพิ่ม)
import repo.BookingRepository;
import repo.MachineRepository; // (เพิ่ม)

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // (เพิ่ม) เราต้องการอัปเดตสถานะเครื่องจักรด้วย
    @Autowired
    private MachineRepository machineRepository; 

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
                booking.getBookingDate());
        if (isSlotTaken) {
            throw new IllegalStateException("This time slot for this machine is already taken.");
        }
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING); // สถานะเริ่มต้นคือ PENDING
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

    // (ลบ) เมธอด startBooking() เดิมถูกลบออกไป
    // เพราะ Manager จะเป็นคนเริ่มให้

    // --- (เพิ่มเมธอดใหม่) ---
    // นี่คือเมธอดที่ Manager ใช้ "อนุมัติ" และ "เริ่มใช้งาน"
    @Transactional
    public Booking approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        // ✅ FIX: ถ้ามันเป็น IN_PROGRESS อยู่แล้ว (อาจจะเพราะกดซ้ำ) 
        // ให้ถือว่าสำเร็จเลย ไม่ต้องโยน Error และไม่ต้องเริ่มจับเวลาใหม่
        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            return booking;
        }

        if (booking.getStatus() == BookingStatus.PENDING) {
            // 1. อัปเดตสถานะการจอง
            booking.setStatus(BookingStatus.IN_PROGRESS);
            
            // 2. อัปเดตสถานะเครื่องซักผ้า
            Machine machine = booking.getMachine();
            if (machine != null) {
                machine.setStatus(AppConstants.STATUS_IN_USE);
                machine.setCurrentUser(booking.getUser()); // ระบุว่าใครใช้อยู่
                // เริ่มจับเวลาเฉพาะตอนที่เปลี่ยนจาก PENDING เป็น IN_PROGRESS ครั้งแรกเท่านั้น
                machine.setUsageStartTime(LocalDateTime.now()); 
                machineRepository.save(machine);
            } else {
                 throw new IllegalStateException("Booking is not linked to a machine.");
            }
            
            return bookingRepository.save(booking);
        } else {
            // (เพิ่มรายละเอียดใน Error เพื่อให้ Debug ง่ายขึ้น)
            throw new IllegalStateException("ทำรายการไม่สำเร็จ สถานะปัจจุบันคือ: " + booking.getStatus());
        }
    }


    // --- (แก้ไขเมธอดนี้) ---
    // นี่คือเมธอดที่นักเรียนกด "เสร็จสิ้น" (Finish)
    @Transactional
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            // 1. อัปเดตสถานะการจอง
            booking.setStatus(BookingStatus.COMPLETED);

            // 2. (ที่แก้ไข) อัปเดตเครื่องซักผ้าให้ "ว่าง"
            Machine machine = booking.getMachine();
            if (machine != null) {
                machine.setStatus(AppConstants.STATUS_AVAILABLE);
                machine.setCurrentUser(null); // ล้างคนใช้งาน
                machine.setUsageStartTime(null); // ล้างเวลาเริ่ม
                machineRepository.save(machine);
            }

            return bookingRepository.save(booking);
        } else {
            throw new IllegalStateException("Only in-progress bookings can be completed");
        }
    }

    // --- (จบส่วนที่แก้ไข) ---

    //--- นี่คือเมธอดที่แก้ไข
    // Get completed bookings for a user that can be rated
    @Transactional(readOnly = true)
    public List<Booking> getCompletedBookingsForRating(Long userId) {
        // (เปลี่ยนไปเรียกเมธอดใหม่ที่มี JOIN FETCH)
        return bookingRepository.findByUserIdAndStatusWithDetails(userId, BookingStatus.COMPLETED);
    }
    // จบส่วนที่แก้ไข ↑↑↑

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