package jdlf.compass.timetableIntegration.service;

import jdlf.compass.timetableIntegration.model.Clazz;
import jdlf.compass.timetableIntegration.model.CompassId;
import jdlf.compass.timetableIntegration.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClazzServiceClient implements CompassService<Clazz, CompassId> {

  private final CourseService courseService;

  @Autowired
  public ClazzServiceClient(CourseService courseService) {
    this.courseService = courseService;
  }

  @Override
  public Clazz create(Clazz entity) {

    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return new Clazz(
        new CompassId("23", "23"),
        new Course(new CompassId("23", "45"), instant, "rt", "des"),
        instant,
        "asda",
        "asddf");
  }

  @Override
  public void delete(CompassId id) {}

  @Override
  public List<Clazz> findAll(int page, int size) {
    List<Clazz> list = new ArrayList<>();
    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    list.add(
        new Clazz(
            new CompassId("23", "23"),
            new Course(new CompassId("23", "45"), instant, "rt", "des"),
            instant,
            "asda",
            "asddf"));

    return list;
  }

  @Override
  public Optional<Clazz> findById(CompassId id) {
    return Optional.empty();
  }

  @Override
  public Clazz update(Clazz entity, CompassId id) {
    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return new Clazz(
        new CompassId("23", "23"),
        new Course(new CompassId("23", "45"), instant, "rt", "des"),
        instant,
        "asda",
        "asddf");
  }
}
