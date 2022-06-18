package br.com.helpdev.gzipjson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.SimpleType;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class GzipDeserializerTest {

  @Test
  void shouldRecoveryJavaTypeFromContextWhenThatNotCreatedInContextual() throws IOException {
    final var gzipDeserializer = new GzipDeserializer();
    final var context = mock(DeserializationContext.class);
    final var javaType = SimpleType.constructUnsafe(GzipCodecTest.PojoSampleWithGzipSerializable.class);
    final var parser = mock(JsonParser.class);
    final var serializable = new GzipCodecTest.PojoSampleWithGzipSerializable("teste");
    final var objectMapper = new ObjectMapper();

    when(parser.getCodec()).thenReturn(objectMapper);
    when(parser.getBinaryValue()).thenReturn(
        Gzip.compress(GzipCodec.getInstanceWithoutGzip(objectMapper)
            .writeValueAsString(serializable))
    );
    when(context.getContextualType()).thenReturn(javaType);

    gzipDeserializer.deserialize(parser, context);

    assertThat(gzipDeserializer.type)
        .isNull();
    verify(context, atLeastOnce())
        .getContextualType();
  }

  @Test
  void shouldCreateContextualWithContextualTypeNotNull() {
    final var gzipDeserializer = new GzipDeserializer();
    final var context = mock(DeserializationContext.class);
    final var bean = mock(BeanProperty.class);
    final var javaType = mock(JavaType.class);

    when(context.getContextualType()).thenReturn(javaType);

    final var deserializerContextual = (GzipDeserializer) gzipDeserializer.createContextual(context, bean);

    assertThat(deserializerContextual.type)
        .isEqualTo(javaType);
    verify(bean, never()).getMember();
    verify(context, atLeastOnce()).getContextualType();
  }


  @Test
  void shouldCreateContextualWithContextualTypeNull() {
    final var gzipDeserializer = new GzipDeserializer();
    final var context = mock(DeserializationContext.class);
    final var bean = mock(BeanProperty.class);
    final var javaType = mock(JavaType.class);
    final var annotatedMember = mock(AnnotatedMember.class);

    when(context.getContextualType()).thenReturn(null);
    when(bean.getMember()).thenReturn(annotatedMember);
    when(annotatedMember.getType()).thenReturn(javaType);

    final var deserializerContextual = (GzipDeserializer) gzipDeserializer.createContextual(context, bean);

    assertThat(deserializerContextual.type)
        .isEqualTo(javaType);
    verify(bean, atLeastOnce()).getMember();
  }

}