package br.com.helpdev.gzipjson;

import static br.com.helpdev.gzipjson.Gzip.decompress;
import static br.com.helpdev.gzipjson.GzipCodec.getInstanceWithoutGzip;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.io.IOException;

public class GzipDeserializer extends JsonDeserializer<GzipSerializable> implements ContextualDeserializer {

  final JavaType type;

  public GzipDeserializer() {
    type = null;
  }

  public GzipDeserializer(final JavaType type) {
    this.type = type;
  }

  @Override
  public GzipSerializable deserialize(final JsonParser jsonParser,
                                      final DeserializationContext context)
      throws IOException {
    final var binary = jsonParser.getBinaryValue();
    final var str = decompress(binary);
    final var typeToUse = this.type == null ? context.getContextualType() : this.type;
    return getInstanceWithoutGzip(jsonParser.getCodec()).readValue(str, typeToUse);
  }

  @Override
  public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext,
                                              final BeanProperty beanProperty) {
    final var type = deserializationContext.getContextualType() != null
        ? deserializationContext.getContextualType()
        : beanProperty.getMember().getType();
    return new GzipDeserializer(type);
  }

}
