package br.com.helpdev.gzipjson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = GzipDeserializer.class)
@JsonSerialize(using = GzipSerializer.class)
public interface GzipSerializable {
}
