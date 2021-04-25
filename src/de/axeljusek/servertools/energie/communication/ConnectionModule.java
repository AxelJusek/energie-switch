package de.axeljusek.servertools.energie.communication;

import com.google.inject.AbstractModule;

public class ConnectionModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ConnectionEnergie.class).to(VerbindungEnerGie.class);
	}
}
