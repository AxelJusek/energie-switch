package de.axeljusek.servertools.energie.communication;

import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;

public class ConnectionModule{
 
  static ConnectionEnergie provideConnectionEnergie() {
    return new VerbindungEnerGie();
  }
}
