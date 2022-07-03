package de.axeljusek.servertools.energie.configuration;

enum ConfigurationLoggingTexts {

  ATTEMPT_TO_CREATE_DEFAULT_CONFIG_CONFLICT("Attempt to create the default configuration, while the file already exists: {}"),
  THE_CONFIG_FILE_WAS_CREATED("The default configuration needed to be created: {}"),
  THE_FILE_COULDNOT_BE_CREATED("The file {} couldn't be created. "),
  THE_FILE_DOES_NOT_EXIST("The file {} does NOT exist. "),
  THE_FILE_NOT_A_FILE("The file {} is not a file. "),
  THE_FILE_EXCEPTION("The file {} throw an Exception: "),
  THE_DIRECTORY_NEEDED_TO_BE_CREATED("The directory for the configuration needed to be created: {}"),
  PROPERTIES_OBJECT_NULL("The properties-object is null, please delete the software!"),
  IO_EXCEPTION_AT_LOADING("Es gab eine IO-Exception beim Laden der Konfiguration.");
  
  private String text;
  
  private ConfigurationLoggingTexts(String text) {
    this.text = text;
  }
  
  public String getText() {
    return this.text;
  }
}
