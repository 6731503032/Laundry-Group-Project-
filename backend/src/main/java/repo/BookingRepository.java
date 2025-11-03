package repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.Booking;
import model.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // --- (เพิ่ม Override นี้) ---
    // (แก้ปัญหา Lazy Error ใน DashboardController -> getAllBookings)
    @Override
    @Query("SELECT b FROM Booking b JOIN FETCH b.machine m JOIN FETCH b.user u")
    List<Booking> findAll();

    // --- เมธอดสำหรับดึง Booking ของ User (หน้า Dashboard) ---
    @Query("SELECT b FROM Booking b JOIN FETCH b.machine JOIN FETCH b.user WHERE b.user.id = :userId")
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);
    
    // --- เมธอดพื้นฐาน ---
    List<Booking> findByUserId(Long userId); 
    List<Booking> findByMachineId(Long machineId);
    List<Booking> findByStatus(BookingStatus status);
    
    // (เมธอดนี้คือเมธอดพื้นฐาน)
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * เมธอดสำหรับตรวจสอบว่าช่องเวลา (Time Slot) นี้ถูกจองหรือยัง
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.machine.id = :machineId " +
           "AND b.bookingDate = :bookingDate " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')")
    boolean existsActiveBookingForMachineAtTime(
        @Param("machineId") Long machineId, 
        @Param("bookingDate") LocalDateTime bookingDate
    );

    /**
     * (สำหรับ Admin Panel)
     * ดึงข้อมูลการจองที่ยัง Active ในช่วงเวลาที่กำหนด
     * พร้อมกับ JOIN FETCH ทั้ง Machine และ User
     */
    @Query("SELECT b FROM Booking b JOIN FETCH b.machine m JOIN FETCH b.user u " +
           "WHERE b.bookingDate BETWEEN :startDate AND :endDate " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')")
    List<Booking> findActiveBookingsByDateRangeWithMachine(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * (สำหรับ Student Dashboard)
     * ดึงข้อมูล Booking ตาม User และ Status
     * พร้อมกับ JOIN FETCH ทั้ง Machine และ User (สำหรับหน้า Rating)
     */
    @Query("SELECT b FROM Booking b JOIN FETCH b.machine m JOIN FETCH b.user u " +
           "WHERE b.user.id = :userId AND b.status = :status " +
           "ORDER BY b.bookingDate DESC")
    List<Booking> findByUserIdAndStatusWithDetails(
        @Param("userId") Long userId, 
        @Param("status") BookingStatus status
    );
    
    // (ส่วนที่ซ้ำกัน 2 บรรทัดสุดท้ายถูกลบออกไปแล้ว)
}