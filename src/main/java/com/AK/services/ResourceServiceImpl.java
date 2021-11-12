package com.AK.services;

import com.AK.exceptions.KingdomHasNoResourcesException;
import com.AK.exceptions.KingdomNotFoundException;
import com.AK.models.Kingdom;
import com.AK.models.resources.Food;
import com.AK.models.resources.Gold;
import com.AK.models.resources.Resource;
import com.AK.models.resources.dto.ResourceDTO;
import com.AK.models.resources.dto.ResourceListDTO;
import com.AK.repositories.ResourceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;
  private final TimeService timeService;

  @Autowired
  public ResourceServiceImpl(
      ResourceRepository resourceRepository,
      TimeService timeService) {
    this.resourceRepository = resourceRepository;
    this.timeService = timeService;
  }

  @Override
  public ResourceListDTO getKingdomsResources(Kingdom kingdom)
      throws KingdomHasNoResourcesException {

    List<Resource> resources = resourceRepository.findAllByKingdom(kingdom);
    if (resources.isEmpty()) {
      throw new KingdomHasNoResourcesException("Kingdom has no resources.");
    }
    List<ResourceDTO> resourceDTOList = resources.stream().map(Resource::toDTO)
        .collect(Collectors.toList());
    return new ResourceListDTO(resourceDTOList);
  }

  @Override
  public void setStarterResources(Kingdom kingdom)
      throws KingdomNotFoundException{

    if (kingdom == null) {
      throw new KingdomNotFoundException("Kingdom is not found.");
    }

    Resource food = new Food(50, 1, timeService.getTime(), kingdom);
    Resource gold = new Gold(100, 1, timeService.getTime(), kingdom);
    resourceRepository.save(food);
    resourceRepository.save(gold);
    List<Resource> resources = new ArrayList<>(List.of(food, gold));

    kingdom.setResources(resources);
  }

  @Override
  public void setResources(Resource resource, int amount) {
    resource.setAmount(amount);
  }

  @Override
  public Resource findAllByKingdomAndType(Kingdom kingdom, String type)
      throws KingdomHasNoResourcesException {

    Optional<Resource> optionalGold = resourceRepository.findAllByKingdomAndType(kingdom, type)
        .stream().findFirst();

    if (optionalGold.isEmpty()) {
      throw new KingdomHasNoResourcesException("Kingdom has no resources.");
    } else {
      return optionalGold.get();
    }
  }

  @Override
  public void save(Resource resource) {
    resourceRepository.save(resource);
  }
}
