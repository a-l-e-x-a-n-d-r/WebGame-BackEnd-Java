package com.AK.models.resources.dto;

import com.AK.models.resources.Resource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceDTO {

  private String type;
  private int amount;
  private int generation;
  private long updatedAt;

  public ResourceDTO(String type, int amount, int generation, long updatedAt) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.updatedAt = updatedAt;
  }

  public ResourceDTO(Resource resource) {
    this.type = resource.getType();
    this.amount = resource.getAmount();
    this.generation = resource.getGeneration();
    this.updatedAt = resource.getUpdatedAt();
  }
}
