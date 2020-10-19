package jdlf.compass.timetableIntegration.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Rotation {

    private CompassId id;

    private String name;

    private String displayName;

    private String description;

    private Integer position;

    private LocalDate startDate;

    private LocalDate endDate;

}
