package de.axeljusek.servertools.energie.communication;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import de.axeljusek.servertools.energie.communication.impl.VerbindungEnerGie;

public class ConnectionModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ConnectionEnergie.class).to(VerbindungEnerGie.class);
  }

  @Provides
  static ConnectionEnergie provideConnetionEnergie() {
    return new VerbindungEnerGie();
  }
}
