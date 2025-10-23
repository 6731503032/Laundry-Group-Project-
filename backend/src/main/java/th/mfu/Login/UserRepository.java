package java.th.mfu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByStudentId(String studentId);

    User findByEmail(String email);

    Boolean existsByStudentId(String studentId);
    
    List<User> findByRole(String role);
}
