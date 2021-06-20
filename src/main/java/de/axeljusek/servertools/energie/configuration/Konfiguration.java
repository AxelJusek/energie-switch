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
 * not, see <http://www.gnu.org/licenses/>.
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
 * Copyright 2021 Axel Jusek
 * 
 *******************************************************************************/
package de.axeljusek.servertools.energie.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Konfiguration {

  private static Logger log = LogManager.getLogger("de.axeljusek.servertools.energenie");
  private static String defaultKonfigurationFile = "konfiguration.conf";
  private static Properties properties;
  private static String konfDir = ".energenie_switch";
  private static String pathToHome = System.getProperty("user.home");
  private static String fileSeparator = System.getProperty("file.separator");

  private static Konfiguration konf;


  public static Konfiguration getInstance() {
    if (null == konf) {
      konf = new Konfiguration();
      String standardKonfigFile =
          pathToHome + fileSeparator + konfDir + fileSeparator + defaultKonfigurationFile;
      var konfFile = getKonfigurationFile(standardKonfigFile);
      loadKonfiguration(konfFile);
    }
    return konf;
  }

  public static Konfiguration getInstanceForConfigFilename(String filename) {
    if (null == konf) {
      konf = new Konfiguration();
    }
    var url =  konf.getClass().getClassLoader().getResource(filename);
    var konfFile = getKonfigurationFile(url.getPath());
    loadKonfiguration(konfFile);
    return konf;
  }

  private Konfiguration() {
    // Intentionally empty, for Singleton-Pattern
  }

  protected static File getKonfigurationFile(String fileName) {
    var defaultKonfigurationFile = new File(fileName);
    if (!defaultKonfigurationFile.exists()) {
      var directoryOfEnergenie = new File(pathToHome + fileSeparator + konfDir);
      if (!directoryOfEnergenie.exists()) {
        directoryOfEnergenie.mkdir();
        log.warn("Verzeichnis fuer die Konfiguration musste angelegt werden:"
            + directoryOfEnergenie.getAbsolutePath());
      }

      try {
        boolean created = defaultKonfigurationFile.createNewFile();
        if (created) {
          log.warn("Die Standard-Konfigurationsdatei musste angelegt werden:"
              + defaultKonfigurationFile.getAbsolutePath());
        } else {
          log.error(
              "Es wurde versucht die Standard-Konfigurationsdatei anzulegen, obwohl sie bereit existiert:"
                  + defaultKonfigurationFile.getAbsolutePath());
        }
      } catch (IOException e) {
        log.error("Die Datei " + defaultKonfigurationFile.getAbsolutePath()
            + " konnte nicht erstellt werden.");
        e.printStackTrace();
      }
    }
    return defaultKonfigurationFile;
  }

  public String getValueForKey(String key) {
    return properties.getProperty(key);
  }

  private static void loadKonfiguration(File konfFile) {
    if (konfFile.exists()) {
      if (konfFile.isFile()) {
        try (var inStream = new FileInputStream(konfFile);) {
          loadKonfigurationFromFileToProperties(inStream);
        } catch (IOException e) {
          log.error(
              "Die Datei " + konfFile.getAbsolutePath() + " warf beim Schließen ein Exception", e);
        }
      } else {
        log.error("Die Datei " + konfFile.getAbsolutePath() + " ist keine Datei");
      }
    } else {
      log.error("Die Datei " + konfFile.getAbsolutePath() + " gibt es nicht.");
    }

    addMissingProperties();
  }

  private static void addMissingProperties() {
    if (null != properties) {
      for (Konfigurationswerte konfW : Konfigurationswerte.values()) {
        if (!properties.containsKey(konfW.getName())) {
          properties.put(konfW.getName(), konfW.getDefaultValue());
        }
      }
    } else {
      log.error("Das Properties-Objekt ist null. Bitte die Software löschen!");
    }
  }

  private static void loadKonfigurationFromFileToProperties(FileInputStream inStream) {
    properties = new Properties();
    try {
      properties.load(inStream);
    } catch (IOException e) {
      log.error("Es gab eine IO-Exception beim Laden der Konfiguration.", e);
    }
  }

}
