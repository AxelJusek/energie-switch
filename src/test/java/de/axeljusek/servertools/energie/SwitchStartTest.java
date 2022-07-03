package de.axeljusek.servertools.energie;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SwitchStartTest {

  @Test
  void testSwitchStart() {
    SwitchStart switchStart = new SwitchStart();
    assertNotNull(switchStart);
  }

  /**
   * if no E.-Switch is connected this test throws a Connection timed out - ConnectException that is
   * logged but not thrown.
   */
  @Test
  void testMain() {
    String[] arguments = new String[] {};
    try {
      SwitchStart.main(arguments);
    } catch (Exception e) {
      fail();
    }
  }

}
