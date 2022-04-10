package de.axeljusek.servertools.energie.communication;

import java.io.IOException;


import de.axeljusek.servertools.energie.communication.impl.Verbindungsdaten;


public interface ConnectionEnergie {

  void doseSchalten(Verbindungsdaten verbindungsDaten, String doseNr, String einschalten)
      throws IOException;

  void zustandAbfragen(Verbindungsdaten verbindungsDaten, String doseNr) throws IOException;

  void schaltzustaendeAusgeben(Verbindungsdaten verbindungsDaten) throws IOException;

}
