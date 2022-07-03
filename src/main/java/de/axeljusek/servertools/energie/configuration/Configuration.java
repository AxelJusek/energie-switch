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
  private static String defaultConfigurationFile = "konfiguration.conf";
  private Properties properties;
  private static String confDir = "energie_switch";
  private static String pathToHome = System.getProperty("user.home");
  private static String fileSeparator = System.getProperty("file.separator");

  private static Configuration configuration;

  public static Configuration getInstance() {
    if (null == configuration) {
      configuration = new Configuration();
      var confFile = getConfigurationFile(pathToHome + fileSeparator + confDir + fileSeparator,
          defaultConfigurationFile);
      configuration.properties = configuration.loadConfiguration(confFile);
      log.info(ConfigurationLoggingTexts.STARTING_WITH_CONFIG.getText(), confFile.getAbsolutePath());
    }
    
    return configuration;
  }

  public static Configuration getInstanceForConfigFilename(String filenameWithFullPath) {
    if (null == configuration) {
      configuration = new Configuration();
    }
    File confFile;
    int indexOfFilenameStart = filenameWithFullPath.lastIndexOf(fileSeparator) > -1
        ? filenameWithFullPath.lastIndexOf(fileSeparator)
        : 0;
    String filename =
        filenameWithFullPath.substring(indexOfFilenameStart);

    var url = configuration.getClass().getClassLoader().getResource(filenameWithFullPath);
    if (null == url) {
      confFile = getConfigurationFile(getPathFromFilename(filenameWithFullPath), filename);
    } else {
      confFile = getConfigurationFile(url.getPath(), filename);
    }
    configuration.properties = configuration.loadConfiguration(confFile);
    return configuration;
  }

  /**
   * This method is only for testing purposes, do NOT use ever!
   */
  protected void clearConf() {
    Configuration.configuration = null;
  }

  private Configuration() {
    // Intentionally empty, for Singleton-Pattern
  }

  protected static File getConfigurationFile(String configurationDir, String fileName) {
    var defaultConfigurationFile = new File(configurationDir + fileName);
    if (!defaultConfigurationFile.exists()) {
      var directoryOfEnergenie = new File(configurationDir);
      if (!directoryOfEnergenie.exists()) {
        directoryOfEnergenie.mkdirs();
        log.warn(ConfigurationLoggingTexts.THE_DIRECTORY_NEEDED_TO_BE_CREATED.getText(),
            directoryOfEnergenie.getAbsolutePath());
      }

      try {
        boolean created = defaultConfigurationFile.createNewFile();
        if (created) {
          log.warn(ConfigurationLoggingTexts.THE_CONFIG_FILE_WAS_CREATED.getText(),
              defaultConfigurationFile.getAbsolutePath());
        } else {
          log.error(ConfigurationLoggingTexts.ATTEMPT_TO_CREATE_DEFAULT_CONFIG_CONFLICT.getText(),
              defaultConfigurationFile.getAbsolutePath());
        }
      } catch (IOException e) {
        log.error(ConfigurationLoggingTexts.THE_FILE_COULDNOT_BE_CREATED.getText(),
            defaultConfigurationFile.getAbsolutePath());
        e.printStackTrace();
      }
    }
    return defaultConfigurationFile;
  }

  public String getValueForKey(String key) {
    return properties.getProperty(key);
  }

  private Properties loadConfiguration(File configurationFile) {
    Properties properties = new Properties();;
    if (configurationFile.exists()) {
      if (configurationFile.isFile()) {
        try (var inStream = new FileInputStream(configurationFile);) {
          properties = loadConfigurationFromFileIntoProperties(inStream);
        } catch (IOException e) {
          log.error(ConfigurationLoggingTexts.THE_FILE_EXCEPTION.getText(),
              configurationFile.getAbsolutePath(), e);
        }
      } else {
        log.error(ConfigurationLoggingTexts.THE_FILE_NOT_A_FILE.getText(),
            configurationFile.getAbsolutePath());
      }
    } else {
      log.error(ConfigurationLoggingTexts.THE_FILE_DOES_NOT_EXIST.getText(),
          configurationFile.getAbsolutePath());
    }

    addMissingProperties(properties);
    
    return properties;
  }

  private static void addMissingProperties(Properties properties) {
    if (null != properties) {
      for (Configurationsvalues configurationValues : Configurationsvalues.values()) {
        if (!properties.containsKey(configurationValues.getName())) {
          properties.put(configurationValues.getName(), configurationValues.getDefaultValue());
        }
      }
    } else {
      log.error(ConfigurationLoggingTexts.PROPERTIES_OBJECT_NULL.getText());
    }
  }

  private static Properties loadConfigurationFromFileIntoProperties(FileInputStream inStream) {
    Properties properties = new Properties();
    try {
      properties.load(inStream);
    } catch (IOException e) {
      log.error(ConfigurationLoggingTexts.IO_EXCEPTION_AT_LOADING.getText(), e);
    }
    return properties;
  }

  private static String getPathFromFilename(String filenameWithPath) {
    String path = "";
    if (filenameWithPath.contains(fileSeparator)) {
      path = filenameWithPath.substring(0, filenameWithPath.lastIndexOf(fileSeparator));
    }
    return path;
  }

}
