package fenggetest;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.Map;

public class FortuneServiceConfiguration extends Configuration {

    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    @JsonProperty
    @NotEmpty
    public String mongohost = "35.197.11.144";

    @JsonProperty
    public int mongoport = 27017;

    @JsonProperty
    @NotEmpty
    public String mongodb = "FortuneDb";

    @JsonProperty("viewRendererConfiguration")
    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.viewRendererConfiguration = builder.build();
    }
}
