package service.serializing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

public class PublicKeySerializer extends JsonSerializer<PublicKey> {

    @Override
    public void serialize(PublicKey publicKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("algorythm", publicKey.getAlgorithm());
        jsonGenerator.writeStringField("format", publicKey.getFormat());
        String bs64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        jsonGenerator.writeStringField("encoded", bs64);
        jsonGenerator.writeEndObject();
    }
}
