package jdlf.compass.timetablegateway.configs;

import org.springframework.util.MultiValueMap;

public class GatewayContext {

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    /**
     * cache json body
     */
    private String cacheBody;
    /**
     * cache formdata
     */
    private MultiValueMap<String, String> formData;
    /**
     * cache reqeust path
     */
    private String path;


    public String getCacheBody() {
        return cacheBody;
    }

    public void setCacheBody(String cacheBody) {
        this.cacheBody = cacheBody;
    }

    public MultiValueMap<String, String> getFormData() {
        return formData;
    }

    public void setFormData(MultiValueMap<String, String> formData) {
        this.formData = formData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}