/*
 * @(#)MaudPreferences.java created 25/04/2000 Casalino
 *
 * Copyright (c) 2000 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is provided as
 * it is as confidential and proprietary information. You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Luca Lutterotti.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 *
 */

package it.unitn.ing.rista.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import it.unitn.ing.rista.interfaces.PreferencesInterface;

/**
 * <p>
 * This class manages Maud preferences.
 * </p>
 *
 * @version $Revision: 1.7 $, $Date: 2017/07/19 14:45:59 $
 * @author Luca Lutterotti, revised by Damiano Martorelli
 * @since JDK1.1
 */

public class MaudPreferences extends PreferencesInterface {

  public static Preferences prefs;

  /**
   * Class constructor.
   */
  public MaudPreferences() {
  }

  /**
   * Resets MAUD preferences.
   */
  public static void resetPreferences() {
    try {
      prefs.clear();
    }
    catch (BackingStoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load MAUD preferences.
   */
  public static void loadPreferences() {
    prefs = Preferences.userRoot().node(MaudPreferences.class.getName());
  }

  public Object getValue(String key) {
    return MaudPreferences.getPref(key, "");
  }

  public void setValue(String key, Object value) {
    MaudPreferences.setPref(key, value.toString());
  }

  /**
   * Checks if the key exists.
   * 
   * @param key
   *          the key to be checked.
   * @return <code>true</code> if the key is defined, <code>false</code>
   *         otherwise.
   */
  public static boolean contains(String key) {
    if (prefs != null) {
      return prefs.get(key, null) != null;
    }
    else
      return false;
  }

  public static void setPref(String key, String value) {
    prefs.put(key, value);
  }

  public static void setPref(String key, int intvalue) {
    prefs.putInt(key, intvalue);
  }

  public static void setPref(String key, long longvalue) {
    prefs.putLong(key, longvalue);
  }

  public static void setPref(String key, double value) {
    prefs.putDouble(key, value);
  }

  public static void setPref(String key, boolean value) {
    prefs.putBoolean(key, value);
  }

  /**
   * Gets value if a specified key is already mapped in the preferences,
   * otherwise add it with the specified value.
   * 
   * @param key
   *          the key to be retrieved.
   * @param defaultValue
   *          the value to be assigned is key is missing.
   * @return the stored value if the key is defined, <code>defaultValue</code>
   *         otherwise.
   */
  public static String getPref(String key, String defaultValue) {
    if (prefs != null) {
      if (!contains(key))
        prefs.put(key, defaultValue);
      return prefs.get(key, defaultValue);
    }
    else
      return defaultValue;
    // if (!contains(key))
    // prefs.put(key, defaultValue);
    // return prefs.get(key, defaultValue);
  }

  /**
   * Gets value if a specified key is already mapped in the preferences,
   * otherwise add it with the specified value.
   * 
   * @param key
   *          the key to be retrieved.
   * @param defaultValue
   *          the value to be assigned is key is missing.
   * @return the stored value if the key is defined, <code>defaultValue</code>
   *         otherwise.
   */
  public static int getInteger(String key, int defaultValue) {
    if (prefs != null) {
      if (!contains(key))
        prefs.putInt(key, defaultValue);
      return prefs.getInt(key, defaultValue);
    }
    else
      return defaultValue;
    // if (!contains(key))
    // prefs.putInt(key, defaultValue);
    // return prefs.getInt(key, defaultValue);
  }

  /**
   * Gets value if a specified key is already mapped in the preferences,
   * otherwise add it with the specified value.
   * 
   * @param key
   *          the key to be retrieved.
   * @param defaultValue
   *          the value to be assigned is key is missing.
   * @return the stored value if the key is defined, <code>defaultValue</code>
   *         otherwise.
   */
  public static double getDouble(String key, double defaultValue) {
    if (prefs != null) {
      if (!contains(key))
        prefs.putDouble(key, defaultValue);
      return prefs.getDouble(key, defaultValue);
    }
    else
      return defaultValue;
    // if (!contains(key))
    // prefs.putDouble(key, defaultValue);
    // return prefs.getDouble(key, defaultValue);
  }

  /**
   * Gets value if a specified key is already mapped in the preferences,
   * otherwise add it with the specified value.
   * 
   * @param key
   *          the key to be retrieved.
   * @param defaultValue
   *          the value to be assigned is key is missing.
   * @return the stored value if the key is defined, <code>defaultValue</code>
   *         otherwise.
   */
  public static long getLong(String key, long defaultValue) {
    if (prefs != null) {
      if (!contains(key))
        prefs.putLong(key, defaultValue);
      return prefs.getLong(key, defaultValue);
    }
    else
      return defaultValue;
    // if (!contains(key))
    // prefs.putLong(key, defaultValue);
    // return prefs.getLong(key, defaultValue);
  }

  public static boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  /**
   * Gets value if a specified key is already mapped in the preferences,
   * otherwise add it with the specified value.
   * 
   * @param key
   *          the key to be retrieved.
   * @param defaultValue
   *          the value to be assigned is key is missing.
   * @return <code>true</code> if the key is defined, <code>false</code>
   *         otherwise.
   */
  public static boolean getBoolean(String key, boolean defaultValue) {
    if (prefs != null) {
      if (!contains(key))
        prefs.putBoolean(key, defaultValue);
      return prefs.getBoolean(key, defaultValue);
    }
    else
      return defaultValue;
    // if (!contains(key))
    // prefs.putBoolean(key, defaultValue);
    // return prefs.getBoolean(key, defaultValue);
  }

}
