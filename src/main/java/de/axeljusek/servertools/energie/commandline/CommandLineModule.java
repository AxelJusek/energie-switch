package de.axeljusek.servertools.energie.commandline;

import de.axeljusek.servertools.energie.communication.ConnectionEnergie;

public class CommandLineModule {
  private final String[] args;
  private final ConnectionEnergie connectionEnergie;

  public CommandLineModule(String[] args, ConnectionEnergie connectionEnergie) {
    this.connectionEnergie = connectionEnergie;
    this.args = args;
  }

}
