package jdlf.compass.timetableIntegration.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompassId implements Serializable {

  private static final long serialVersionUID = 7661028081075341454L;

  String entityId;

  @NonNull String workingSetId;

  public CompassId(String entityId, @NonNull String workingSetId) {
    this.entityId = entityId;
    this.workingSetId = workingSetId;
  }
}
