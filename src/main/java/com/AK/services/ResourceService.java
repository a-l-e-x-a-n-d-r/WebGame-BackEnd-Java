package com.AK.services;

import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.models.Kingdom;
import com.AK.models.resources.Resource;
import com.AK.models.resources.dto.ResourceListDTO;

public interface ResourceService {

  ResourceListDTO getKingdomsResources(Kingdom kingdom)
      throws KingdomHasNoResourcesException;

  void setStarterResources(Kingdom kingdom)
      throws KingdomNotFoundException, KingdomHasNoResourcesException;

  void setResources(Resource resource, int amount);

  Resource findAllByKingdomAndType(Kingdom kingdom, String type)
      throws KingdomHasNoResourcesException;

  void save(Resource resource);

}
