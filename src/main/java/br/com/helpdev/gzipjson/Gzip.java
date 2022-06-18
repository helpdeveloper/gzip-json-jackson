package br.com.helpdev.gzipjson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

interface Gzip {

  static byte[] compress(String data) throws IOException {
    try (final var bos = new ByteArrayOutputStream(data.length())) {
      try (final var gzip = new GZIPOutputStream(bos)) {
        gzip.write(data.getBytes(StandardCharsets.UTF_8));
      }
      return bos.toByteArray();
    }
  }

  static String decompress(byte[] compressed) throws IOException {
    try (final var bis = new ByteArrayInputStream(compressed);
         final var gis = new GZIPInputStream(bis);
         final var br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {
      return br.lines().collect(Collectors.joining());
    }
  }

}
