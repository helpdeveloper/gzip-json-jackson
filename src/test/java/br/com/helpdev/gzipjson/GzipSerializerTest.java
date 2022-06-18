package br.com.helpdev.gzipjson;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GzipSerializerTest {

  @Test
  void shouldWriteBinaryInSerializer() throws IOException {
    final var serializer = new GzipSerializer();
    final var serializable = new GzipSerializableTest.SampleTestClass();
    serializable.uuid = UUID.randomUUID();
    final var generator = mock(JsonGenerator.class);
    final var provider = mock(SerializerProvider.class);
    final var codec = new ObjectMapper();

    when(generator.getCodec()).thenReturn(codec);
    doNothing().when(generator).writeBinary(any());

    serializer.serialize(serializable, generator, provider);

    verify(generator, atLeastOnce()).writeBinary(any());
  }

}