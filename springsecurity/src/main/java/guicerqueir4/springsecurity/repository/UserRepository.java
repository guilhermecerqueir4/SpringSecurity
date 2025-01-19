package guicerqueir4.springsecurity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import guicerqueir4.springsecurity.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
