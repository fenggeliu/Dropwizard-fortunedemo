package fenggetest.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fortune {

    private String id = UUID.randomUUID().toString();

    private String fortune;

    public Fortune(){

    }

    public Fortune(String fortune){
        this.fortune = fortune;
    }

    public String getId() {
        return id;
    }

    public String getFortune() {
        return fortune;
    }

    public void setFortune(String fortune) {
        this.fortune = fortune;
    }
}
