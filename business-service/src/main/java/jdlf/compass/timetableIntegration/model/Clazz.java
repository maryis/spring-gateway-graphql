package jdlf.compass.timetableIntegration.model;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Clazz implements Serializable {

  private static final long serialVersionUID = 1044098251667352039L;
  private CompassId id;
  private Course course;
  private Instant startDate;
  private String code;
  private String description;
}
