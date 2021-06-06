package ml.jc.magneto;

import java.util.HashMap;
import java.util.Map;

public class PojoResponse {

    private int statusCode;
    private String body;
    private Map<String, String> headers;

    public PojoResponse() {
        this.headers = new HashMap<>();
        this.headers.put("Content-Type","application/json");
        this.headers.put("Access-Control-Allow-Origin","*");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
