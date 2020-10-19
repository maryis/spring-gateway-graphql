package jdlf.compass.timetableIntegration.common;

import jdlf.compass.timetableIntegration.model.Rotation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;

import java.util.List;

public class MainTest1 {

    final static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        objectMapper.registerModule(new JavaTimeModule());


        String jsonString = " {\n" +
                "  \"data\": {\n" +
                "    \"rotations\": [\n" +
                "      {\n" +
                "        \"id\": {\n" +
                "          \"entityId\": \"5f86392bdfbe981af1821ef1\",\n" +
                "          \"workingSetId\": \"5f863929dfbe981af1821ed9\"\n" +
                "        },\n" +
                "        \"name\": \"ROATION_1\",\n" +
                "        \"displayName\": \"Rotation 1\",\n" +
                "        \"endDate\": \"2020-06-01\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JSONObject obj = new JSONObject(jsonString);
        String listStr = obj.getJSONObject("data").getJSONArray("rotations").toString();

        List<Rotation> lr = null;
        try {
            lr = objectMapper.readValue(listStr, new TypeReference<List<Rotation>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(lr);


    }

}
