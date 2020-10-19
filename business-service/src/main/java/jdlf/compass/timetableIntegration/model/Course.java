package jdlf.compass.timetableIntegration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Component
public class Course {

  private CompassId id;
  private Instant startDate;
  private String code;
  private String description;
}
