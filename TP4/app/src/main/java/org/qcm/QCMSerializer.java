package main.java.org.qcm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QCMSerializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializeQCM(QCM qcm) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(qcm);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize QCM", e);
        }
    }
}
