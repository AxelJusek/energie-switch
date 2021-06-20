/*******************************************************************************
 *******************************************************************************/
package de.axeljusek.servertools.energie.commandline;

import com.google.inject.AbstractModule;
import de.axeljusek.servertools.energie.communication.ConnectionEnergie;
import de.axeljusek.servertools.energie.communication.ConnectionModule;

public class CommandLineModule extends AbstractModule {
  private final String[] args;
  private final ConnectionEnergie connectionEnergie;

  public CommandLineModule(String[] args, ConnectionEnergie connectionEnergie) {
    this.connectionEnergie = connectionEnergie;
    this.args = args;
  }

  @Override
  protected void configure() {
    install(new ConnectionModule());
    bind(CommandoLineInterpreter.class)
        .toInstance(new CommandoLineInterpreter(args, connectionEnergie));
  }
}
