package br.com.helpdev.gzipjson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class GzipSerializableTest {

  @Test
  void shouldSerializerAndDeserializerWithSuccess() throws JsonProcessingException {
    final var objectToTest = generateObjectToTest();
    final var objectMapper = getDefaultObjectMapper();
    final var objectMapperWithoutGzip = GzipCodec.getInstanceWithoutGzip(objectMapper);

    final var stringWithoutEncoded = objectMapperWithoutGzip
        .writeValueAsString(objectToTest);
    final var stringEncoded = objectMapper.writeValueAsString(objectToTest);
    final var objectDecoded = objectMapper.readValue(stringEncoded, SampleTests.class);
    final var stringDecodedFromEncoded = objectMapperWithoutGzip.writeValueAsString(objectDecoded);

    assertThat(stringEncoded)
        .hasSizeLessThan(stringWithoutEncoded.length());
    assertThat(objectDecoded.listOfSamples)
        .hasSize(objectToTest.listOfSamples.size());
    assertThat(stringDecodedFromEncoded)
        .isEqualTo(stringWithoutEncoded);
  }

  private ObjectMapper getDefaultObjectMapper() {
    final var objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    return objectMapper;
  }

  private SampleTests generateObjectToTest() {
    final var samples = new ArrayList<SampleTestClass>();
    IntStream.range(0, 10).forEachOrdered(value -> samples.add(new SampleTestClass(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        value
    )));
    return new SampleTests(samples);
  }

  public static class SampleTests implements GzipSerializable {
    public SampleTests() {
      this(new ArrayList<>());
    }

    public SampleTests(final List<SampleTestClass> listOfSamples) {
      this.listOfSamples = listOfSamples;
    }

    public List<SampleTestClass> listOfSamples;
  }

  public static class SampleTestClass implements GzipSerializable {
    public SampleTestClass() {
    }

    public SampleTestClass(final UUID uuid, final String name, final int id) {
      this.uuid = uuid;
      this.name = name;
      this.id = id;
    }

    public UUID uuid;
    public String name;
    public int id;
  }

}