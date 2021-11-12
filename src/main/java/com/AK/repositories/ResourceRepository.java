package com.AK.repositories;

import com.AK.models.Kingdom;
import com.AK.models.resources.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

  List<Resource> findAllByKingdom(Kingdom kingdom);

  List<Resource> findAllByKingdomAndType(Kingdom kingdom, String type);
}
