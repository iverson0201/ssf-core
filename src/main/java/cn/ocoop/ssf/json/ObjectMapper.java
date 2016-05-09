package cn.ocoop.ssf.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

import java.io.IOException;

/**
 * Created by liolay on 15-7-28.
 */
public class ObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
    {
        DefaultSerializerProvider.Impl sp = new DefaultSerializerProvider.Impl();
        sp.setNullValueSerializer(new NullSerializer());
        this.setSerializerProvider(sp);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public class NullSerializer extends JsonSerializer<Object> {
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {

            jgen.writeString("");
        }
    }
}
