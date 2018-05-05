package service.serializing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

public class PublicKeyDeserializer extends JsonDeserializer<PublicKey> {

    @Override
    public PublicKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);

        String alg = jsonNode.get("algorythm").asText();
        String form = jsonNode.get("format").asText();
        byte[] enc = Base64.getDecoder().decode(jsonNode.get("encoded").asText());

        return new PublicKey() {
            @Override
            public String getAlgorithm() {
                return alg;
            }

            @Override
            public String getFormat() {
                return form;
            }

            @Override
            public byte[] getEncoded() {
                return enc;
            }
        };
    }
}
