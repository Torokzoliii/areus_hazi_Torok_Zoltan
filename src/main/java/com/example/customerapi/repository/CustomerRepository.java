package com.example.customerapi.repository;

import com.example.customerapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByDateOfBirthBetween(java.time.LocalDate start, java.time.LocalDate end);
    boolean existsByEmail(String email);
    @Query(value = "SELECT AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, date_of_birth))) FROM clients", nativeQuery = true)
    Optional<Double> findAverageAge();
}
