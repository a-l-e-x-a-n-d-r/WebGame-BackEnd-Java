package com.AK.repositories;

import com.AK.models.Kingdom;
import com.AK.models.buildings.Building;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

  List<Building> findAllByKingdom(Kingdom kingdom);

}
