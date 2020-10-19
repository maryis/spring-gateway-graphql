package jdlf.compass.timetableIntegration.service;

import jdlf.compass.timetableIntegration.common.Parser;
import jdlf.compass.timetableIntegration.model.CompassId;
import jdlf.compass.timetableIntegration.model.Rotation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.time.LocalDate.now;

@Service
@Log4j2
public class RotationService implements CompassService<Rotation, CompassId> {

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Value("${backend.graphql.server.url}")
    private String url;

    @Value("${backend.graphql.server.username}")
    private String username;

    @Value("${backend.graphql.server.password}")
    private String password;

    private final String rotationsQuery="query ($page:Int,$size:Int){ rotations( page: $page, size: $size) { id { entityId workingSetId } name  displayName  endDate  }}";
    private final String rotationQuery="query ($entityId: String!, $workingSetId: String!) { rotation(id: {entityId: $entityId, workingSetId: $workingSetId}){id { entityId workingSetId } name displayName  description position startDate endDate }}";

    @Autowired
    public RotationService(ObjectMapper objectMapper, RestTemplate restTemplate) {

        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public Rotation create(Rotation entity) {
        return null;
    }

    @Override
    public void delete(CompassId id) {

    }

    @Override
    public List<Rotation> findAll(int page, int size) {

        // authorization happens here
        /////////////////////////////////////////////////
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(String.format("principal : %s", currentPrincipalName));
        //end of authorization

        //create request and call timetable-service
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(username, password);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query",rotationsQuery);
        requestBody.put("variables",String.format("{\"page\":%d,\"size\":%d}",page,size));

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(
                        url, new HttpEntity<>(requestBody, headers), String.class);


        //process response body
        log.info("responseEntity-Body: " + responseEntity.getBody());
        JSONObject data = new JSONObject(responseEntity.getBody()).getJSONObject("data");
        List<Rotation> list = null;

        if(data!=null) {
            String rotationsStr = data.getJSONArray("rotations").toString();
            try {
                list = objectMapper.readValue(rotationsStr, new TypeReference<List<Rotation>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        //business rules, or changing received data here
        /////////////////////////////////////////////////

        return list;
    }

    @Override
    public Optional<Rotation> findById(CompassId id) {
        // authorization happens here
        /////////////////////////////////////////////////
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info(String.format("principal : %s", currentPrincipalName));
        //end of authorization

        //create request and call timetable-service
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(username, password);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query",rotationQuery);
        requestBody.put("variables",String.format("{\"entityId\":\"%s\",\"workingSetId\":\"%s\"}",id.getEntityId(),id.getWorkingSetId()));

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(
                        url, new HttpEntity<>(requestBody, headers), String.class);


        //process response body
        log.info("responseEntity-Body: " + responseEntity.getBody());
        JSONObject data = new JSONObject(responseEntity.getBody()).getJSONObject("data");
        Rotation rotation = null;

        if(data!=null) {
            String rotationStr = data.getJSONObject("rotation").toString();
            try {
                rotation = objectMapper.readValue(rotationStr, new TypeReference<Rotation>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        //business rules, or changing received data here
        /////////////////////////////////////////////////

        return Optional.ofNullable(rotation);
    }

    @Override
    public Rotation update(Rotation entity, CompassId id) {
        return null;
    }
}
