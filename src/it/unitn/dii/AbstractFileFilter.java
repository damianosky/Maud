package it.unitn.dii;

/*
 * I/O utilities - Copyright (C) 2017 by Damiano Martorelli
 *   
 * All rights reserved. Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 *  
 * Original version by Damiano Martorelli
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <p>
 * This class provides the filter for input/output of files.
 * </p>
 *
 * @author <a href="mailto:martorelli.damiano@gmail.com">Damiano Martorelli</a>
 * @version 1.1, January 10, 2017
 */
public class AbstractFileFilter extends FileFilter
{
  protected String sDescription;
  protected String SFile_extension;
  
  /**
   * Class constructor.
   */
  public AbstractFileFilter()
  {
    this.sDescription = "";
    this.SFile_extension = "";
  }
  
  /**
   * Class constructor where it is possible to personalize the description to be
   * showed in the FileChooser dialog.
   * 
   * @param new_description
   *          is the description to be showed in dialog form.
   */
  public AbstractFileFilter(String new_description, String new_extension)
  {
    this.sDescription = new_description;
    this.SFile_extension = new_extension;
  }
  
  /**
   * Returns the description of the extension of the file.
   * 
   * @return the string text with the description to be showed in the
   *         FileChooser dialog.
   */
  @Override
  public String getDescription()
  {
    return sDescription + String.format(" (*%s)", SFile_extension);
  }
  
  /**
   * Returns the value of the extension of the file.
   * 
   * @return the value of the extension.
   */
  public String getExtension()
  {
    return this.SFile_extension;
  }
  
  /**
   * Returns the value of the extension of the file.
   * 
   * @return the value of the extension without the "." prefix.
   */
  public String getPureExtension()
  {
    return this.SFile_extension.replace(".", "");
  }
  
  /**
   * Returns if the extension is a valid one.
   * 
   * @return true if it is valid, false otherwise.
   */
  @Override
  public boolean accept(File f)
  {
    if (f.isDirectory())
    {
      return true;
    }
    else
    {
      return f.getName().toLowerCase().endsWith(SFile_extension);
    }
  }
}
