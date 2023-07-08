package com.example.demo.jpaRepositories;

import com.example.demo.models.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatRepository extends JpaRepository<Boat,Long> {


}
