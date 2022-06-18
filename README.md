![Issues](https://img.shields.io/github/issues/helpdeveloper/gzip-json-jackson.svg)
![Forks](https://img.shields.io/github/forks/helpdeveloper/gzip-json-jackson.svg)
![Stars](https://img.shields.io/github/stars/helpdeveloper/gzip-json-jackson.svg)
![Release Version](https://img.shields.io/github/release/helpdeveloper/gzip-json-jackson.svg)
![WorkFlow](https://github.com/helpdeveloper/gzip-json-jackson/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d492d9f4f03941f1aadfb4094536ef76)](https://www.codacy.com/gh/helpdeveloper/gzip-json-jackson/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=helpdeveloper/gzip-json-jackson&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/helpdeveloper/gzip-json-jackson/badge.svg?branch=main)](https://coveralls.io/github/helpdeveloper/gzip-json-jackson?branch=main)

# GZIP JSON Jackson

**GZIP JSON Compressor with Jackson library.** 

This library uses Jackson library to convert your 
POJO to a compressed JSON with GZIP.

The goal of this library is to **reduce the size of JSONs generated**.
However, if you have shorter JSON you don't have benefits in size, 
because the compressed output will be transformed in BASE64 and 
get more extensive than the original JSON. 

# Dependency

```xml
    <dependencies>
        <dependency>
            <groupId>com.github.helpdeveloper</groupId>
            <artifactId>gzip-json-jackson</artifactId>
            <version>{last-release}</version>
        </dependency>
    </dependencies>
        
    <!-- Include the Jitpack repository -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

# How to use

Just adding the `GzipSerializable` interface in your 
Object that will be converted and use your own `ObjectMapper` 
to convert. As we commonly do.

### Sample

- Include `GzipSerializable` in your object:
```java
import br.com.helpdev.gzipjson.GzipSerializable;

class MyPojoClass implements GzipSerializable{
  private String name;
  //getters and setters
}
```
- Sample using Jackson `ObjectMapper`:

```java
import com.fasterxml.jackson.databind.ObjectMapper;

class MyServiceSample {
  
  private final ObjectMapper objectMapper;
  
  public MyService(final ObjectMapper objectMapper){
    this.objectMapper = objectMapper;
  }
  
  public String getCompressedJsonFromObject(final MyPojoClass pojo){
    return objectMapper.writeValueAsString(pojo);
  }
  
  public MyPojoClass getObjectFromCompressedJson(final String compressedJson){
    return objectMapper.readValue(compressedJson, MyPojoClass.class);
  }
}
```

# Contribute
Pull Requests are welcome. For important changes, open an 'issue' first to discuss what you would like to change. Be sure to update tests as appropriate.

# Developer
Guilherme Biff Zarelli

Blog/Site - https://helpdev.com.br
LinkedIn - https://linkedin.com/in/gbzarelli/
GitHub - https://github.com/gbzarelli
Medium - https://medium.com/@guilherme.zarelli
Email - gbzarelli@helpdev.com.br