package com.AK.models.resources;

import com.AK.models.Kingdom;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("Food")
public class Food extends Resource {

  public Food(int amount, int generation, long updatedAt,
      Kingdom kingdom) {
    super(null, "Food", amount, generation, updatedAt, kingdom);
  }
}
