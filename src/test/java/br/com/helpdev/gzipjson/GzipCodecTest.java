package br.com.helpdev.gzipjson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class GzipCodecTest {

  @Test
  void shouldCopyAndConfigureFromInputCodecWithoutAlterThan() {
    final var mapper = mock(ObjectMapper.class);
    final var copiedExpected = mock(ObjectMapper.class);

    when(mapper.copy()).thenReturn(copiedExpected);

    final var copied = GzipCodec.getInstanceWithoutGzip(mapper);

    assertThat(copied)
        .isEqualTo(copiedExpected);
    verify(copied, atLeastOnce())
        .setAnnotationIntrospector(any());
    verify(mapper, never())
        .setAnnotationIntrospector(any());
    verify(mapper, atLeastOnce())
        .copy();
  }

  @Test
  void shouldChangeIntrospectFromSerialize() {
    final var mapper = new ObjectMapper();
    final var copied = GzipCodec.getInstanceWithoutGzip(mapper);
    final var annotatedFromSerializer = mock(Annotated.class);
    final var annotationSerialize = mock(JsonSerialize.class);

    when(annotatedFromSerializer.getAnnotation(any())).thenReturn(annotationSerialize);
    when(annotationSerialize.using()).thenAnswer(invocationOnMock -> GzipSerializer.class);

    final var findFromCopiedSerializer =
        copied.getSerializationConfig().getAnnotationIntrospector().findSerializer(annotatedFromSerializer);
    final var findFromMapperSerializer =
        mapper.getSerializationConfig().getAnnotationIntrospector().findSerializer(annotatedFromSerializer);

    assertThat(findFromCopiedSerializer)
        .isNull();
    assertThat(findFromMapperSerializer)
        .isEqualTo(GzipSerializer.class);
  }

  @Test
  void shouldDontChangeIntrospectFromSerialize() {
    final var mapper = new ObjectMapper();
    final var copied = GzipCodec.getInstanceWithoutGzip(mapper);
    final var annotatedFromSerializer = mock(Annotated.class);
    final var annotationSerialize = mock(JsonSerialize.class);

    when(annotatedFromSerializer.getAnnotation(any())).thenReturn(annotationSerialize);
    when(annotationSerialize.using()).thenAnswer(invocationOnMock -> JsonSerialize.class);

    final var findFromCopiedSerializer =
        copied.getSerializationConfig().getAnnotationIntrospector().findSerializer(annotatedFromSerializer);
    final var findFromMapperSerializer =
        mapper.getSerializationConfig().getAnnotationIntrospector().findSerializer(annotatedFromSerializer);

    assertThat(findFromCopiedSerializer)
        .isEqualTo(JsonSerialize.class);
    assertThat(findFromMapperSerializer)
        .isEqualTo(JsonSerialize.class);
  }

  @Test
  void shouldDontChangeIntrospectFromDeserializer() {
    final var mapper = new ObjectMapper();
    final var copied = GzipCodec.getInstanceWithoutGzip(mapper);
    final var annotatedFrom = mock(Annotated.class);
    final var annotation = mock(JsonDeserialize.class);

    when(annotatedFrom.getAnnotation(any())).thenReturn(annotation);
    when(annotation.using()).thenAnswer(invocationOnMock -> JsonDeserialize.class);

    final var findFromCopiedDeserializer =
        copied.getSerializationConfig().getAnnotationIntrospector().findDeserializer(annotatedFrom);
    final var findFromMapperDeserializer =
        mapper.getSerializationConfig().getAnnotationIntrospector().findDeserializer(annotatedFrom);

    assertThat(findFromCopiedDeserializer)
        .isEqualTo(JsonDeserialize.class);
    assertThat(findFromMapperDeserializer)
        .isEqualTo(JsonDeserialize.class);
  }

  @Test
  void shouldChangeIntrospectFromDeserialize() {
    final var mapper = new ObjectMapper();
    final var copied = GzipCodec.getInstanceWithoutGzip(mapper);
    final var annotatedFromDeserializer = mock(Annotated.class);
    final var annotationDeserialize = mock(JsonDeserialize.class);

    when(annotatedFromDeserializer.getAnnotation(any())).thenReturn(annotationDeserialize);
    when(annotationDeserialize.using()).thenAnswer(invocationOnMock -> GzipDeserializer.class);

    final var findFromCopiedDeserializer =
        copied.getSerializationConfig().getAnnotationIntrospector().findDeserializer(annotatedFromDeserializer);
    final var findFromMapperDeserializer =
        mapper.getSerializationConfig().getAnnotationIntrospector().findDeserializer(annotatedFromDeserializer);

    assertThat(findFromCopiedDeserializer)
        .isNull();
    assertThat(findFromMapperDeserializer)
        .isEqualTo(GzipDeserializer.class);
  }

  @Test
  void shouldCopyOneTimeWhenTheSameCodecPassedInInput() {
    final var mapper = mock(ObjectMapper.class);
    final var copiedExpected = mock(ObjectMapper.class);

    when(mapper.copy()).thenReturn(copiedExpected);

    final var copiedOne = GzipCodec.getInstanceWithoutGzip(mapper);
    final var copiedTwo = GzipCodec.getInstanceWithoutGzip(mapper);

    assertThat(copiedExpected)
        .isEqualTo(copiedOne)
        .isEqualTo(copiedTwo);
    assertThat(copiedOne)
        .isEqualTo(copiedTwo);
    verify(mapper, atLeastOnce())
        .copy();
  }

  @Test
  void shouldSaveDifferentCopiesFromDifferentCodecsInput() {
    final var mapperOne = mock(ObjectMapper.class);
    final var mapperTwo = mock(ObjectMapper.class);
    final var copiedExpectedOne = mock(ObjectMapper.class);
    final var copiedExpectedTwo = mock(ObjectMapper.class);

    when(mapperOne.copy()).thenReturn(copiedExpectedOne);
    when(mapperTwo.copy()).thenReturn(copiedExpectedTwo);

    final var copiedOne = GzipCodec.getInstanceWithoutGzip(mapperOne);
    final var copiedTwo = GzipCodec.getInstanceWithoutGzip(mapperTwo);

    assertThat(copiedOne)
        .isNotEqualTo(copiedTwo);
    assertThat(mapperOne)
        .isNotEqualTo(mapperTwo)
        .isNotEqualTo(copiedOne)
        .isNotEqualTo(copiedTwo);
    assertThat(mapperTwo)
        .isNotEqualTo(mapperOne)
        .isNotEqualTo(copiedOne)
        .isNotEqualTo(copiedTwo);
    verify(mapperOne, atLeastOnce())
        .copy();
    verify(mapperTwo, atLeastOnce())
        .copy();
  }

  @Test
  void shouldUseInternalObjectMapperToMapObjectsWithoutGzipSerializable() throws JsonProcessingException {
    final var pojoSample = new PojoSampleWithoutGzipSerializable();
    pojoSample.name = "sample";
    final var expectedJson = "{\"name\":\"sample\"}";
    final var objectMapper = new ObjectMapper();
    final ObjectMapper instanceWithoutGzip = GzipCodec.getInstanceWithoutGzip(objectMapper);

    var jsonWithoutCompressedPassedByDefaultMapper = objectMapper.writeValueAsString(pojoSample);
    var json = instanceWithoutGzip.writeValueAsString(pojoSample);
    var obj = instanceWithoutGzip.readValue(json, PojoSampleWithoutGzipSerializable.class);

    assertThat(jsonWithoutCompressedPassedByDefaultMapper)
        .isEqualTo(json)
        .isEqualTo(expectedJson);
    assertThat(json)
        .isEqualTo(expectedJson);
    assertThat(obj)
        .isEqualTo(pojoSample);
  }

  @Test
  void shouldUseInternalObjectMapperToMapObjectsWithGzipSerializable() throws JsonProcessingException {
    final var pojoSample = new PojoSampleWithGzipSerializable();
    pojoSample.name = "sample";
    final var expectedJson = "{\"name\":\"sample\"}";
    final var objectMapper = new ObjectMapper();
    final var instanceWithoutGzip = GzipCodec.getInstanceWithoutGzip(objectMapper);

    var jsonCompressed = objectMapper.writeValueAsString(pojoSample);
    var json = instanceWithoutGzip.writeValueAsString(pojoSample);
    var obj = instanceWithoutGzip.readValue(json, PojoSampleWithGzipSerializable.class);

    assertThat(jsonCompressed)
        .isNotEqualTo(expectedJson);
    assertThat(json)
        .isEqualTo(expectedJson);
    assertThat(obj)
        .isEqualTo(pojoSample);
  }

  static class PojoSampleWithoutGzipSerializable {
    public String name;

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final PojoSampleWithoutGzipSerializable that = (PojoSampleWithoutGzipSerializable) o;

      return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
      return name != null ? name.hashCode() : 0;
    }
  }

  static class PojoSampleWithGzipSerializable implements GzipSerializable {
    public PojoSampleWithGzipSerializable() {
    }

    public PojoSampleWithGzipSerializable(final String name) {
      this.name = name;
    }

    public String name;

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final PojoSampleWithGzipSerializable that = (PojoSampleWithGzipSerializable) o;

      return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
      return name != null ? name.hashCode() : 0;
    }
  }

}