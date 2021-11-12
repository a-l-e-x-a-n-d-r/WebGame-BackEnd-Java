package com.AK.models;

import com.AK.models.buildings.Building;
import com.AK.models.resources.Resource;
import com.AK.models.troops.Troop;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @OneToOne(mappedBy = "kingdom")
  private User user;

  @OneToMany(mappedBy = "kingdom")
  private List<Building> buildings;

  @OneToMany(mappedBy = "kingdom")
  private List<Resource> resources;

  @OneToMany(mappedBy = "kingdom")
  private List<Troop> troops;

  public Kingdom(String name) {
    this.name = name;
    this.buildings = new ArrayList<>();
    this.resources = new ArrayList<>();
    this.troops = new ArrayList<>();
  }

  public void addBuilding(Building building) {
    buildings.add(building);
  }

  public void addTroops(Troop troop) {
    troops.add(troop);
  }
}