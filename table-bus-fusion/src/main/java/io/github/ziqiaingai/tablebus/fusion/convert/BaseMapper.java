package io.github.ziqiaingai.tablebus.fusion.convert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Named;

public interface BaseMapper {
     ObjectMapper objectMapper = new ObjectMapper();

    default JsonNode map(String s) {
        try {
            return objectMapper.readTree(s);
        } catch (Exception e) {
            return null;
        }
    }

    default String map(JsonNode json) {
        if (json == null) {
            return null;
        }
        return json.toString();
    }

    default java.util.Date map(java.lang.Long value) {
        if ( value == null ) {
            return null;
        }

        return new java.util.Date( value );
    }

    default java.lang.Long map(java.util.Date value) {
        if ( value == null ) {
            return null;
        }

        return value.getTime();
    }
}
