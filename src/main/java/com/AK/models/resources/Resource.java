package com.AK.models.resources;

import com.AK.models.Kingdom;
import com.AK.models.resources.dto.ResourceDTO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String type;
  private int amount;
  private int generation;
  private long updatedAt;

  @ManyToOne
  @JoinColumn(name = "kingdom_id")
  private Kingdom kingdom;

  public ResourceDTO toDTO() {
    return new ResourceDTO(type, amount, generation, updatedAt);
  }
}
