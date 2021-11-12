package com.AK.models.kingdomDTO;

import com.AK.models.Kingdom;
import com.AK.models.buildings.BuildingDTO;
import com.AK.models.resources.dto.ResourceDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KingdomDTO {

  private final String name;
  private final Long Id;
  private final Long userId;
  private final List<BuildingDTO> buildings;
  private final List<ResourceDTO> resources;

  public KingdomDTO(Kingdom kingdom) {
    this.name = kingdom.getName();
    this.Id = kingdom.getId();
    this.userId = kingdom.getUser().getId();
    this.buildings = makeBuildingDTOList(kingdom);
    this.resources = makeResourceDTOList(kingdom);
  }

  private List<BuildingDTO> makeBuildingDTOList(Kingdom kingdom) {

    return kingdom.getBuildings().stream().map(BuildingDTO::new).collect(Collectors.toList());
  }

  private List<ResourceDTO> makeResourceDTOList(Kingdom kingdom) {

    return kingdom.getResources().stream().map(ResourceDTO::new).collect(Collectors.toList());
  }
}
