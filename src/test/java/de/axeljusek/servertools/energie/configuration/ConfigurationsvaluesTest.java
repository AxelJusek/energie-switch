package de.axeljusek.servertools.energie.configuration;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConfigurationsvaluesTest {

  @Test
  void existenceTest() {
    Configurationsvalues cvPort = Configurationsvalues.PORT;
    assertEquals("port", cvPort.getName());
    assertEquals("Number", cvPort.getDataType());
    assertEquals("5", cvPort.getLength());
    assertEquals("5000", cvPort.getDefaultValue());
  }

}
