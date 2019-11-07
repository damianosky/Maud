/*
 * @(#)JSubordListPane.java created 01/01/1997 Mesiano
 *
 * Copyright (c) 1997 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is
 * provided as it is as confidential and proprietary information.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with the author.
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import it.unitn.ing.rista.util.*;
import it.unitn.ing.rista.diffr.*;
import it.unitn.ing.rista.diffr.FilePar;

/**
 * The JSubordListPane is a class
 *
 * @version $Revision: 1.8 $, $Date: 2017/08/04 10:39:02 $
 * @author Luca Lutterotti, revised by Damiano Martorelli
 * @since JDK1.1
 */

public class JSubordListPane extends JPanel {

  protected JTextField txtTotTF = null;
  protected JList lstAtomicElements;
  protected JButton btnAdd = new JIconButton("Plus.gif", "add term");
  JTextField[] valueTF = null;
  XRDcat itsparent = null;
  int theindex = 0, selected = -1;
  JPanel fieldsPanel;
  int fieldNumber;
  Frame theparent = null;
  /**
   * Class constructor.
   * 
   * @param parent
   *          is the frame parent of the panel.
   * @param showTotal
   */
  public JSubordListPane(Frame parent, boolean showTotal) {
    super();
    setFrameParent(parent);

    JPanel pnlNorth, pnlNorthParams, pnlParams, pnlBtnContainer;

    setLayout(new BorderLayout(3, 3));
    pnlNorth = new JPanel();
    pnlNorth.setLayout(new BorderLayout(3, 3));
    add("North", pnlNorth);
    if (showTotal) {
      pnlNorthParams = new JPanel();
      pnlNorthParams.setLayout(new BorderLayout(3, 3));
      pnlNorth.add("North", pnlNorthParams);
      pnlParams = new JPanel();
      pnlParams.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
      pnlNorthParams.add("East", pnlParams);
      pnlParams.add(new JLabel("Total parameter:"));
      txtTotTF = new JTextField(4);
      txtTotTF.setEditable(false);
      txtTotTF.setText("0");
      pnlParams.add(txtTotTF);
    }
    lstAtomicElements = new JList();
    lstAtomicElements.setVisibleRowCount(4);
    lstAtomicElements.setPrototypeCellValue("123456789012");
    lstAtomicElements.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    JScrollPane scrlPaneAtomicElements = new JScrollPane();
    scrlPaneAtomicElements.setViewportView(lstAtomicElements);
    pnlNorth.add(BorderLayout.CENTER, scrlPaneAtomicElements);

    JPanel pnlButtons = new JPanel();
    pnlButtons.setLayout(new FlowLayout());
    pnlNorth.add(BorderLayout.WEST, pnlButtons);
    pnlBtnContainer = new JPanel();
    pnlBtnContainer.setLayout(new GridLayout(2, 1, 3, 3));
    pnlButtons.add(pnlBtnContainer);
    pnlBtnContainer.add(btnAdd);
    final JRemoveButton btnRemove = new JRemoveButton("Minus.gif", "remove term");
    pnlBtnContainer.add(btnRemove);
    btnRemove.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        if (!Constants.confirmation || Utility.areYouSureToRemove("Remove the selected object?"))
          removeB_Clicked();
      }
    });
    pnlNorthParams = new JPanel();
    pnlNorthParams.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
    add("Center", pnlNorthParams);
    fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BorderLayout(6, 6));
    pnlNorthParams.add(fieldsPanel);
	  initListener();

	  addCustomControlsToFieldsPanel();

  }

	public void addCustomControlsToFieldsPanel() {

	}

  public void setFrameParent(Frame parent) {
    theparent = parent;
  }

  public Frame getFrameParent() {
    return theparent;
  }

  public FilePar getFileParent() {
    Frame aparent = getFrameParent();
    while (aparent != null && !(aparent instanceof principalJFrame)) {
      aparent = ((ParentFrame) aparent).getFrameParent();
    }
    if (aparent != null)
      return ((ParentFrame) aparent).getFileParent();
    else
      return null;
  }

	ActionListener buttonListener = null;
	ListSelectionListener listSelection = null;

  public void initListener() {
	  if (buttonListener == null)
    btnAdd.addActionListener(buttonListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        addB_Clicked();
      }
    });
	  if (listSelection == null)
    lstAtomicElements.addListSelectionListener(listSelection = new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        thelist_ListSelect();
      }
    });
  }

	public void removeListener() {
		btnAdd.removeActionListener(buttonListener);
		lstAtomicElements.removeListSelectionListener(listSelection);
		listSelection = null;
		buttonListener = null;
	}

	public void setFields(String[] labels) {
		if (valueTF != null) {
			for (int i = 0; i < valueTF.length; i++) {
//				System.out.println("Removing: " + i + " " + valueTF[i].getText() + " " + valueTF[i].toString());
				((myJFrame) getFrameParent()).removeComponentfromlist(valueTF[i]);
				if (valueTF[i] != null) {
					valueTF[i].removeAll();
//					valueTF[i].revalidate();
//					valueTF[i].repaint();
				}

//				valueTF[i].removeAll();
				valueTF[i] = null;
			}
		}
		fieldsPanel.removeAll();
		addCustomControlsToFieldsPanel();
//		System.out.println("New JTextField");
    valueTF = new JTextField[fieldNumber];
    JPanel jp1 = new JPanel();
    if (fieldNumber > 8)
      jp1.setLayout(new GridLayout(0, 2, 3, 3));
    else
      jp1.setLayout(new GridLayout(0, 1, 3, 3));
    fieldsPanel.add(BorderLayout.CENTER, jp1);
    for (int i = 0; i < fieldNumber; i++) {
      JPanel jp2 = new JPanel();
      jp2.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
      jp2.add(new JLabel(labels[i]));
      valueTF[i] = new JTextField(Constants.FLOAT_FIELD);
      valueTF[i].setText("0");
      jp2.add(valueTF[i]);
//	    System.out.println("Added: " + i + " " + valueTF[i].getText() + " " + valueTF[i].toString());
//	    System.out.println("Adding focus: " + i);
      valueTF[i].addFocusListener(new FocusListener() {
        public void focusLost(FocusEvent fe) {
          retrieveparlist(selected);
        }
        public void focusGained(FocusEvent fe) {
        }
      });
      jp1.add(jp2);
    }
		fieldsPanel.revalidate();
		fieldsPanel.repaint();
  }

  public void setList(XRDcat aparent, int index, int number, String[] labels) {
	  if (itsparent != null) {
//		  System.out.println("Retrieving");
		  retrieveparlist();
	  }
	  if (itsparent == aparent && theindex == index)
		  return;
//	  System.out.println("Changing list: " + aparent.getLabel() + " " + index + " " + number + " " + labels[0]);
    itsparent = aparent;
    theindex = index;
    fieldNumber = number;
    setFields(labels);
    if (itsparent != null) {
	    selected = -1;
      int numb = itsparent.subordinateloopField[theindex].setList(lstAtomicElements);
      if (txtTotTF != null)
        txtTotTF.setText(String.valueOf(numb));
      if (numb > 0) {
        setparameterlist(0);
	      selected = 0;
      }
    }
//    initListener();
  }

  public void setparameterlist(int numb) {
    int totnumb = itsparent.numberofelementSubL(theindex);
    if (totnumb > numb) {
      lstAtomicElements.setSelectedIndex(numb);
      setparameterlist();
    }
  }

	public XRDcat selectedObject = null;

  public void setparameterlist() {
    if (itsparent != null) {
      XRDcat obj = (XRDcat) itsparent.subordinateloopField[theindex].selectedElement();
      if (obj != null && obj != selectedObject)
	      selectedObject = obj;
        for (int i = 0; i < fieldNumber; i++) {
          Parameter apar = obj.parameterField[i];
          if (apar != null) {
	          ((myJFrame) getFrameParent()).removeComponentfromlist(valueTF[i]);
            ((myJFrame) getFrameParent()).addComponenttolist(valueTF[i], apar);
	          valueTF[i].setText(apar.getValue());
          } /*else {
            ((myJFrame) getFrameParent()).removeComponentfromlist(valueTF[i]);
          }*/
        }
    }
  }

  public void retrieveparlist(int numb) {
    if (numb >= 0 && itsparent != null) {
      XRDcat obj = (XRDcat) itsparent.subordinateloopField[theindex].elementAt(numb);
      if (obj != null)
        for (int i = 0; i < fieldNumber; i++)
          obj.parameterField[i].setValue(valueTF[i].getText());
    }
  }

  public void retrieveparlist() {
    retrieveparlist(selected);
  }

  public void setparameterField(String label) {
    if (itsparent != null)
      itsparent.subordinateloopField[theindex].setLabelAt(label, lstAtomicElements.getSelectedIndex());
  }

  public String gettheLabel() {
    return "Parameter label:";
  }

  void addB_Clicked() {
    // add a new parameter
    if (itsparent != null && lstAtomicElements != null) {
      retrieveparlist(selected);
      selected = -1;
      itsparent.addsubordinateloopField(theindex);
      int numb = itsparent.numberofelementSubL(theindex);
      if (txtTotTF != null)
        txtTotTF.setText(String.valueOf(numb));
      setparameterlist(numb - 1);
    }
  }

  void removeB_Clicked() {
    // remove selected parameter
    if (itsparent != null && lstAtomicElements != null)
      if (lstAtomicElements.getSelectedIndex() >= 0) {
        selected = -1;
        if (itsparent.removeselSubLField(theindex)) {
          int numb = itsparent.numberofelementSubL(theindex);
          if (txtTotTF != null)
            txtTotTF.setText(String.valueOf(numb));
        }
        setparameterlist(0);
      }
  }

  void thelist_ListSelect() {
    if (lstAtomicElements != null) {
      retrieveparlist(selected);
      selected = lstAtomicElements.getSelectedIndex();
      setparameterlist(selected);
    }
  }

  public void dispose() {
    lstAtomicElements = null;
    itsparent = null;
  }

}
