package br.com.helpdev.gzipjson;

import static br.com.helpdev.gzipjson.Gzip.compress;
import static br.com.helpdev.gzipjson.GzipCodec.getInstanceWithoutGzip;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class GzipSerializer extends JsonSerializer<GzipSerializable> {

  @Override
  public void serialize(final GzipSerializable serializable,
                        final JsonGenerator generator,
                        final SerializerProvider provider) throws IOException {
    final var json = getInstanceWithoutGzip(generator.getCodec())
        .writeValueAsString(serializable);
    final var jsonCompressed = compress(json);
    generator.writeBinary(jsonCompressed);
  }

}
