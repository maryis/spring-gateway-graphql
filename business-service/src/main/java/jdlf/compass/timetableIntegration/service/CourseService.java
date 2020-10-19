package jdlf.compass.timetableIntegration.service;

import jdlf.compass.timetableIntegration.common.Parser;
import jdlf.compass.timetableIntegration.model.CompassId;
import jdlf.compass.timetableIntegration.model.Course;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Log4j2
public class CourseService implements CompassService<Course, CompassId> {

  private final ObjectMapper objectMapper;

  private final RestTemplate restTemplate;

  @Autowired
  public CourseService(ObjectMapper objectMapper, RestTemplate restTemplate) {

    this.objectMapper = objectMapper;
    this.restTemplate = restTemplate;
  }

  @Override
  public Course create(Course entity) {
    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return new Course(new CompassId("fg", "sid"), instant, "12", "wed");
  }

  @Override
  public void delete(CompassId id) {}

  @Override
  public List<Course> findAll(int page, int size) {
    List<Course> list = new ArrayList<>();
    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    list.add(new Course(new CompassId("fg", "sid"), instant, "12", "wed"));
    return list;
  }

  @Override
  public Optional<Course> findById(CompassId id) {
    return Optional.empty();
  }

  public Optional<Course> findByIdJWT(CompassId id) {

    // authorization happens here

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    log.error(String.format("principal : %s", currentPrincipalName));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(
        "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJzY2hvb2wteCIsInN1YiI6Imp3dHVzZXIiLCJpc3MiOiJNYXJ6aWVoIiwibmFtZSI6Imp3dHVzZXIiLCJleHAiOjE1OTY1MjUyMzEsImlhdCI6MTU5NjUyNDkzMX0.ORRzDsLgPP6n9WaA_rqHI7j6j8ozOPs-7gDzIIzU365QOhEiPU9CkaQbbuZUJETCw4aA-6woFwgdLawo4aMpng");

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "query",
        " { courses(page:2,size:1){\n" + "    startDate,\n" + "    description\n" + " } }");
    ResponseEntity<String> responseEntity =
        restTemplate.postForEntity(
            "http://localhost:9090/graphql", new HttpEntity<>(requestBody, headers), String.class);

    Map<String, Object> response = new Parser<>().getMap(Objects.requireNonNull(responseEntity.getBody()));

    String data = "";
    String error = "";
    if (response.containsKey("data") && response.get("data") != null)
      data = response.get("data").toString();
    if (response.containsKey("errors")) error = response.get("errors").toString();

    log.error("data: " + data);
    log.error("error: " + error);

    // does not work for graphql response, should organize deserialize annotations
    //        List<Course> list=new Parser<Course>().getListObject(data);
    //        list.forEach(i-> {
    //            System.out.println(i.getDescription());
    //        });

    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return Optional.of(new Course(new CompassId("fg", "sid"), instant, "12", "wed"));
  }

  public Optional<Course> findByIdBasicAuth(CompassId id) {

    // authorization happens here

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    log.error(String.format("principal : %s", currentPrincipalName));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBasicAuth("admin", "password");

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put(
        "query",
        " { courses(page:2,size:1){\n" + "    startDate,\n" + "    description\n" + " } }");
    ResponseEntity<String> responseEntity =
        restTemplate.postForEntity(
            "http://localhost:9090/graphql", new HttpEntity<>(requestBody, headers), String.class);

    Map<String, Object> response = new Parser<>().getMap(Objects.requireNonNull(responseEntity.getBody()));

    String data = "";
    String error = "";
    if (response.containsKey("data") && response.get("data") != null)
      data = response.get("data").toString();
    if (response.containsKey("errors")) error = response.get("errors").toString();

    log.error("data: " + data);
    log.error("error: " + error);

    // does not work for graphql response, should organize deserialize annotations
    //        List<Course> list=new Parser<Course>().getListObject(data);
    //        list.forEach(i-> {
    //            System.out.println(i.getDescription());
    //        });

    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return Optional.of(new Course(new CompassId("fg", "sid"), instant, "12", "wed"));
  }

  @Override
  public Course update(Course entity, CompassId id) {
    LocalDate date = LocalDate.parse("9999-12-31");
    Instant instant = date.atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant();
    return new Course(new CompassId("fg", "sid"), instant, "12", "wed");
  }
}
