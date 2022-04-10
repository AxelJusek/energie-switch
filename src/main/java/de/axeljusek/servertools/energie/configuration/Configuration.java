/*******************************************************************************
 *
 * Licensed under GPL-3.0-only or GPL-3.0-or-later
 * 
 * This file is part of EnergieSwitch.
 * 
 * EnergieSwitch is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * EnergieSwitch is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with EnergieSwitch. If
 * not, see <https://www.gnu.org/licenses/>.
 * 
 * Diese Datei ist Teil von EnergieSwitch.
 * 
 * EnergieSwitch ist Freie Software: Sie können es unter den Bedingungen der GNU General Public
 * License, wie von der Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl) jeder
 * neueren veröffentlichten Version, weiter verteilen und/oder modifizieren.
 * 
 * EnergieSwitch wird in der Hoffnung, dass es nützlich sein wird, aber OHNE JEDE GEWÄHRLEISTUNG,
 * bereitgestellt; sogar ohne die implizite Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN
 * BESTIMMTEN ZWECK. Siehe die GNU General Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem Programm erhalten
 * haben. Wenn nicht, siehe <https://www.gnu.org/licenses/>.
 * 
 * Copyright 2021, 2022 Axel Jusek
 * 
 *******************************************************************************/

package de.axeljusek.servertools.energie.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
  private static Logger log = LoggerFactory.getLogger(Configuration.class);
  private static String defaultKonfigurationFile = "konfiguration.conf";
  private static Properties properties;
  private static String konfDir = ".energie_switch";
  private static String pathToHome = System.getProperty("user.home");
  private static String fileSeparator = System.getProperty("file.separator");

  private static final String ATTEMPT_TO_CREATE_DEFAULT_CONFIG_CONFLICT =
      "Attempt to create the default configuration, while the file already exists: {}";
  private static final String THE_CONFIG_FILE_WAS_CREATED =
      "The default configuration needed to be created: {}";
  private static final String THE_FILE_COULDNOT_BE_CREATED = "The file {} couldn't be created. ";
  private static final String THE_FILE_DOES_NOT_EXIST = "The file {} does NOT exist. ";
  private static final String THE_FILE_NOT_A_FILE = "The file {} is not a file. ";
  private static final String THE_FILE_EXCEPTION = "The file {} throw an Exception: ";
  private static final String THE_DIRECTORY_NEEDED_TO_BE_CREATED =
      "The directory for the configuration needed to be created: {}";
  private static final String PROPERTIES_OBJECT_NULL =
      "The properties-object is null, please delete the software!";
  private static final String IO_EXCEPTION_AT_LOADING =
      "Es gab eine IO-Exception beim Laden der Konfiguration.";

  private static Configuration konf;


  public static Configuration getInstance() {
    if (null == konf) {
      konf = new Configuration();
      String standardKonfigFile =
          pathToHome + fileSeparator + konfDir + fileSeparator + defaultKonfigurationFile;
      var konfFile = getConfigurationFile(standardKonfigFile);
      loadConfiguration(konfFile);
    }
    return konf;
  }

  public static Configuration getInstanceForConfigFilename(String filename) {
    if (null == konf) {
      konf = new Configuration();
    }
    File konfFile;
    
    var url = konf.getClass().getClassLoader().getResource(filename);
    if (null == url) {
      konfFile = getConfigurationFile(filename);
    } else {
      konfFile = getConfigurationFile(url.getPath());
    }
    loadConfiguration(konfFile);
    return konf;
  }

  /**
   * This method is only for testing purposes, do NOT use ever!
   */
  protected void clearConf() {
    Configuration.konf = null;
  }

  private Configuration() {
    // Intentionally empty, for Singleton-Pattern
  }

  protected static File getConfigurationFile(String fileName) {
    var defaultConfigurationFile = new File(fileName);
    if (!defaultConfigurationFile.exists()) {
      var directoryOfEnergenie = new File(pathToHome + fileSeparator + konfDir);
      if (!directoryOfEnergenie.exists()) {
        directoryOfEnergenie.mkdir();
        log.warn(THE_DIRECTORY_NEEDED_TO_BE_CREATED, directoryOfEnergenie.getAbsolutePath());
      }

      try {
        boolean created = defaultConfigurationFile.createNewFile();
        if (created) {
          log.warn(THE_CONFIG_FILE_WAS_CREATED, defaultConfigurationFile.getAbsolutePath());
        } else {
          log.error(ATTEMPT_TO_CREATE_DEFAULT_CONFIG_CONFLICT,
              defaultConfigurationFile.getAbsolutePath());
        }
      } catch (IOException e) {
        log.error(THE_FILE_COULDNOT_BE_CREATED, defaultConfigurationFile.getAbsolutePath());
        e.printStackTrace();
      }
    }
    return defaultConfigurationFile;
  }

  public String getValueForKey(String key) {
    return properties.getProperty(key);
  }

  private static void loadConfiguration(File konfFile) {
    if (konfFile.exists()) {
      if (konfFile.isFile()) {
        try (var inStream = new FileInputStream(konfFile);) {
          loadConfigurationFromFileToProperties(inStream);
        } catch (IOException e) {
          log.error(THE_FILE_EXCEPTION, konfFile.getAbsolutePath(), e);
        }
      } else {
        log.error(THE_FILE_NOT_A_FILE, konfFile.getAbsolutePath());
      }
    } else {
      log.error(THE_FILE_DOES_NOT_EXIST, konfFile.getAbsolutePath());
    }

    addMissingProperties();
  }

  private static void addMissingProperties() {
    if (null != properties) {
      for (Configurationsvalues konfW : Configurationsvalues.values()) {
        if (!properties.containsKey(konfW.getName())) {
          properties.put(konfW.getName(), konfW.getDefaultValue());
        }
      }
    } else {
      log.error(PROPERTIES_OBJECT_NULL);
    }
  }

  private static void loadConfigurationFromFileToProperties(FileInputStream inStream) {
    properties = new Properties();
    try {
      properties.load(inStream);
    } catch (IOException e) {
      log.error(IO_EXCEPTION_AT_LOADING, e);
    }
  }

}
