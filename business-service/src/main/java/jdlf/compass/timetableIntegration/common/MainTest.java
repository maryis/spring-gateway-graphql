package jdlf.compass.timetableIntegration.common;

import jdlf.compass.timetableIntegration.common.Parser;
import jdlf.compass.timetableIntegration.model.Rotation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainTest {

    final static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        String rotationJson=" {\n" +
                "        \"id\": {\n" +
                "          \"entityId\": \"5f86392bdfbe981af1821ef1\",\n" +
                "          \"workingSetId\": \"5f863929dfbe981af1821ed9\"\n" +
                "        },\n" +
                "        \"name\": \"ROATION_1\",\n" +
                "        \"displayName\": \"Rotation 1\",\n" +
                "        \"endDate\": \"2020-06-01\"\n" +
                "      }";

        String rotationListJson=" [\n" +
                "      {\n" +
                "        \"id\": {\n" +
                "          \"entityId\": \"5f86392bdfbe981af1821ef1\",\n" +
                "          \"workingSetId\": \"5f863929dfbe981af1821ed9\"\n" +
                "        },\n" +
                "        \"name\": \"ROATION_1\",\n" +
                "        \"displayName\": \"Rotation 1\",\n" +
                "        \"endDate\": \"2020-06-01\"\n" +
                "      }\n" +
                "    ]";

        String rotationListHeaderJson=" {\"rotations\": [\n" +
                "      {\n" +
                "        \"id\": {\n" +
                "          \"entityId\": \"5f86392bdfbe981af1821ef1\",\n" +
                "          \"workingSetId\": \"5f863929dfbe981af1821ed9\"\n" +
                "        },\n" +
                "        \"name\": \"ROATION_1\",\n" +
                "        \"displayName\": \"Rotation 1\",\n" +
                "        \"endDate\": \"2020-06-01\"\n" +
                "      }\n" +
                "    ]}";

        System.out.println(new Parser<Rotation>().getObject(rotationJson));
        System.out.println(new Parser<Rotation>().getObject(rotationListJson));

        Map<String, Object> response = new Parser<>().getMap(Objects.requireNonNull(rotationListHeaderJson));
        System.out.println(response.get("rotations").toString());

        try {
            List<Rotation> participantJsonList = objectMapper.readValue(response.get("rotations").toString(),
                    new TypeReference<List<Rotation>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//        List<Rotation> list= (List<Rotation>) new Parser<Rotation>().getObject(response.get("rotations").toString());



    }
}
