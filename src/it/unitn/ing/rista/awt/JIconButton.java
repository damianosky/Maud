/*
 * @(#)JIconButton.java created 17/08/1998 Mesiano
 *
 * Copyright (c) 1998 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is
 * provided as it is as confidential and proprietary information.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Luca Lutterotti.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package it.unitn.ing.rista.awt;

import java.net.*;
import java.net.URL;
import java.awt.*;
import javax.swing.*;

import it.unitn.ing.rista.util.*;

/**
 * <p>
 * The JIconButton is an extension of the JButton class to include an icon on
 * the button. The path is defined by MAUD preferences, while the icon is passed
 * through the name.
 * </p>
 *
 *
 * @version $Revision: 1.5 $, $Date: 2017/07/18 09:30:48 $
 * @author Luca Lutterotti, revised by Damiano Martorelli.
 * @since JDK1.1
 */

public class JIconButton extends JButton {
  /**
   * Class constructor.
   * 
   * @param icon_name
   *          a valid icon name.
   * @param text
   *          a text to be showd on the button.
   */
  public JIconButton(String icon_name, String text) {
    super(text);
    if (icon_name != null &&
          (text.equals("") || MaudPreferences.getBoolean("gui.useIconsInButtons", !Constants.macosx)))
    {  
      //setIcon(new ImageIcon(Misc.getResourceURL(Constants.imagesJar, Constants.iconfolder + icon)));
      URL url = Misc.getResourceURL(Constants.imagesJar, Constants.iconfolder + icon_name);
      if (url != null)
      {
        try
        {
          setIcon(new ImageIcon(url));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  /**
   * Class constructor.
   * 
   * @param icon_name
   *          a valid icon name.
   */
  public JIconButton(String icon_name) {
    this(icon_name, "");
  }
  
  /**
   * Default constructor.
   */
  public JIconButton() {
    super();
  }
  
  /**
   * Class constructor.
   * 
   * @param icon_name
   *          a valid icon name.
   * @param text
   *          a text to be showd on the button.
   * @param actionCommand
   */
  public JIconButton(String icon_name, String text, String actionCommand) {
    this(icon_name, text);
    setActionCommand(actionCommand);
  }

  /**
   * Class constructor.
   * 
   * @param icon_name
   *          a valid icon name.
   * @param text
   *          a text to be showd on the button.
   * @param actionCommand
   * @param toolTipText
   */
  public JIconButton(String icon_name, String text, String actionCommand, String toolTipText) {
    this(icon_name, text, actionCommand);
    setToolTipText(toolTipText);
  }
  
  /**
   * Assigns icon according to the name passed.
   * 
   * @param icon_name
   *          a valid icon name.
   */
  public void setIcon(String icon_name) {
    URL url = Misc.getResourceURL(Constants.imagesJar, Constants.iconfolder + icon_name);
    if (url != null)
    {
      try
      {
        setIcon(new ImageIcon(url));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }    
    //setIcon(new ImageIcon(Misc.getResourceURL(Constants.imagesJar, Constants.iconfolder + icon)));
  }

}
