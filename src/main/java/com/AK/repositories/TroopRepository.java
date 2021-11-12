package com.AK.repositories;

import com.AK.models.Kingdom;
import com.AK.models.troops.Troop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopRepository extends JpaRepository<Troop, Long> {

  List<Troop> findAllByKingdom(Kingdom kingdom);
}