package service.serializing;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
public class SerializerService extends SimpleModule{

    public SerializerService() {
        this.addDeserializer(PublicKey.class, new PublicKeyDeserializer());
        this.addSerializer(PublicKey.class, new PublicKeySerializer());
    }
}
