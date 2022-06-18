package br.com.helpdev.gzipjson;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class GzipTest {

  @Test
  void shouldCompressAndDecompressWithSuccess() throws IOException {
    final var loren = "haushuasussahuahusahusahu";
    final var compressed = Gzip.compress(loren);
    final var compressedBase64 = Base64.getEncoder().encodeToString(compressed);
    final var decompressed = Gzip.decompress(compressed);

    assertThat(decompressed)
        .isEqualTo(loren)
        .isNotEqualTo(compressedBase64);
  }

}