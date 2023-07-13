package com.example.demo.jpaRepositories;

import com.example.demo.models.Boat;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoatRepository extends JpaRepository<Boat,Long> {
    Optional<List<Boat>> findByUser(User user);

    Optional<Boat> findByUserAndId(User user, Long id);

}
