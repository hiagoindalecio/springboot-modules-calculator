package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CalculationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonCalculationTools {
    private static final Logger aLogger = LoggerFactory.getLogger(JsonCalculationTools.class);

    public static String serialize(CalculationDTO pDeserialized) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;

        try {
            jsonInString = mapper.writeValueAsString(pDeserialized);
            aLogger.debug("Serialized message payload: {}", jsonInString);
            return jsonInString;
        } catch (JsonProcessingException pException) {
            aLogger.error("Error", String.valueOf(pException));
            throw pException;
        }
    }

    public static CalculationDTO deserialize(String pSerialized) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        TypeReference<CalculationDTO> mapType = new TypeReference<CalculationDTO>() {};

        try {
            return objectMapper.readValue(pSerialized, mapType);
        } catch (IOException pException) {
            aLogger.error("Error", String.valueOf(pException));
            throw pException;
        }
    }
}
