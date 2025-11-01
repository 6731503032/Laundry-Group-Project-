package repo; // or 'repository' depending on your package name

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

    // --- THIS IS THE FIX ---
    /*
     * This query tells Hibernate to "JOIN and FETCH" the related Machine and User
     * objects in a single query. This solves the LazyInitializationException
     * (the "HibernateProxy" error) completely.
     */
    @Query("SELECT b FROM Booking b JOIN FETCH b.machine JOIN FETCH b.user WHERE b.user.id = :userId")
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);
    
    // --- Other methods your service needs ---
    
    // This is the "lazy" version. We'll keep it, but we won't use it in the dashboard.
    List<Booking> findByUserId(Long userId); 

    List<Booking> findByMachineId(Long machineId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}