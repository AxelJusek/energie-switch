package de.axeljusek.servertools.energie.commandline;

import com.google.inject.AbstractModule;

public class CommandLineModule extends AbstractModule {
	private final String[] args;
	
	public CommandLineModule(String[] args) {
		this.args = args;
	}
	
	@Override
	protected void configure() {
		bind(CommandoLineInterpreter.class).toInstance(new CommandoLineInterpreter(args));
	}
}
