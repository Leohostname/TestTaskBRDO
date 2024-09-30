package candidate.verification.TestTaskBRDO.repository;

import candidate.verification.TestTaskBRDO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
