package repo;

import model.Booking;
import model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByMachineId(Long machineId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}
