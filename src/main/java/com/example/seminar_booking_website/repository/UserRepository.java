package com.example.seminar_booking_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import your entity class
import com.example.seminar_booking_website.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // this is the magical line ✨
}
