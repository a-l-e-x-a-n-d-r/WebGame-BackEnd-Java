package com.AK.repositories;

import com.AK.models.Kingdom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Long> {

  Optional<Kingdom> findByName(String name);
}