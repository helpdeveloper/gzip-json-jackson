package br.com.helpdev.gzipjson;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.util.concurrent.ConcurrentHashMap;

class GzipCodec {

  private static final ConcurrentHashMap<ObjectCodec, ObjectMapper> mapperInstances = new ConcurrentHashMap<>();

  private GzipCodec() {
  }

  static ObjectMapper getInstanceWithoutGzip(final ObjectCodec jsonParser) {
    return mapperInstances.computeIfAbsent(jsonParser, codec -> {
      var copy = ((ObjectMapper) codec).copy();
      copy.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
        @Override
        public Object findDeserializer(final Annotated a) {
          final var deserializer = super.findDeserializer(a);
          return GzipDeserializer.class.equals(deserializer) ? null : deserializer;
        }

        @Override
        public Object findSerializer(final Annotated a) {
          final var serializer = super.findSerializer(a);
          return GzipSerializer.class.equals(serializer) ? null : serializer;
        }
      });
      return copy;
    });

  }
}
