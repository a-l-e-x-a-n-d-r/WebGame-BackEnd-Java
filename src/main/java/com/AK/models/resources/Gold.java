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
@DiscriminatorValue("Gold")
public class Gold extends Resource {

  public Gold(int amount, int generation, long updatedAt,
      Kingdom kingdom) {
    super(null, "Gold", amount, generation, updatedAt, kingdom);
  }
}
