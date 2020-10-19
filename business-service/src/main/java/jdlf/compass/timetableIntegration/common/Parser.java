package jdlf.compass.timetableIntegration.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser<T> {

  final ObjectMapper objectMapper = new ObjectMapper();

  public T getObject(String json) {

    T obj = null;
    try {
      obj = objectMapper.readValue(json, new TypeReference<T>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return obj;
  }

  public Map<String, T> getMap(String json) {

    Map<String, T> map = new HashMap<>();
    try {

      map = objectMapper.readValue(json, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return map;
  }

  public List<T> getListObject(String json) {

    List<T> obj = new ArrayList<>();
    try {

      obj = objectMapper.readValue(json, new TypeReference<List<T>>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return obj;
  }

  public T getResponse(String json) {

    T obj = null;
    try {
      obj = objectMapper.readValue(json, new TypeReference<T>() {
      });
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return obj;
  }
}
