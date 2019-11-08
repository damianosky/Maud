/*
 * @(#)JSubordListPane.java created 01/01/1997 Mesiano
 *
 * Copyright (c) 1997 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of Luca Lutterotti and it is provided as
 * it is as confidential and proprietary information. You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with the author.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 *
 */

package it.unitn.ing.rista.awt;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import it.unitn.ing.rista.diffr.FilePar;
import it.unitn.ing.rista.diffr.Parameter;
import it.unitn.ing.rista.diffr.XRDcat;
import it.unitn.ing.rista.util.Constants;

/**
 * The JSubordListPane is a class
 *
 * @version $Revision: 1.8 $, $Date: 2017/08/04 10:39:02 $
 * @author Luca Lutterotti, revised by Damiano Martorelli
 * @since JDK1.1
 */

public class JSubordListPane extends JPanel {

  protected JTextField            txtTotTF          = null;
  protected JList                 lstAtomicElements;
  protected JButton               btnAdd            = new JIconButton("Plus.gif", "add term");
  protected JTextField[]          txtValue          = null;
  protected XRDcat                objParent         = null;
  protected int                   theindex          = 0;
  protected int                   selected          = -1;
  protected JPanel                pnlCenter         = new JPanel();
  protected JPanel                pnlSouth          = new JPanel();
  protected int                   fieldNumber;
  protected Frame                 frmParent         = null;
  protected ActionListener        actButtonListener = null;
  protected ListSelectionListener listSelection     = null;

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
          btnRemove_Clicked();
      }
    });
    //
    add(pnlCenter, BorderLayout.CENTER);
    //
    pnlSouth.setLayout(new BorderLayout(6, 6));
    pnlCenter.add(pnlSouth);
    initListener();
    //
    addCustomControlsToFieldsPanel();
  }

  public void addCustomControlsToFieldsPanel() {

  }

  /**
   * Set the parent frame.
   * 
   * @param parent
   *          the reference to parent frame.
   */
  public void setFrameParent(Frame parent) {
    frmParent = parent;
  }

  /**
   * Get the parent frame.
   * 
   * @return parent the reference to parent frame.
   */
  public Frame getFrameParent() {
    return frmParent;
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

  /**
   * Init button and list listeners
   */
  public void initListener() {
    if (actButtonListener == null)
      btnAdd.addActionListener(actButtonListener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          btnAdd_Clicked();
        }
      });
    if (listSelection == null)
      lstAtomicElements.addListSelectionListener(listSelection = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent event) {
          doAtomicElements_ListSelect();
        }
      });
  }

  /**
   * Remove listeners
   */
  public void removeListener() {
    btnAdd.removeActionListener(actButtonListener);
    lstAtomicElements.removeListSelectionListener(listSelection);
    listSelection = null;
    actButtonListener = null;
  }

  public void setFields(String[] labels) {
    if (txtValue != null) {
      for (int i = 0; i < txtValue.length; i++) {
        // System.out.println("Removing: " + i + " " + valueTF[i].getText() + "
        // " + valueTF[i].toString());
        ((myJFrame) getFrameParent()).removeComponentfromlist(txtValue[i]);
        if (txtValue[i] != null) {
          txtValue[i].removeAll();
          // valueTF[i].revalidate();
          // valueTF[i].repaint();
        }

        // valueTF[i].removeAll();
        txtValue[i] = null;
      }
    }
    pnlSouth.removeAll();
    addCustomControlsToFieldsPanel();
    // System.out.println("New JTextField");
    txtValue = new JTextField[fieldNumber];
    JPanel jp1 = new JPanel();
    if (fieldNumber > 8)
      jp1.setLayout(new GridLayout(0, 2, 3, 3));
    else
      jp1.setLayout(new GridLayout(0, 1, 3, 3));
    pnlSouth.add(BorderLayout.CENTER, jp1);
    for (int i = 0; i < fieldNumber; i++) {
      JPanel jp2 = new JPanel();
      jp2.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
      jp2.add(new JLabel(labels[i]));
      txtValue[i] = new JTextField(Constants.FLOAT_FIELD);
      txtValue[i].setText("0");
      jp2.add(txtValue[i]);
      // System.out.println("Added: " + i + " " + valueTF[i].getText() + " " +
      // valueTF[i].toString());
      // System.out.println("Adding focus: " + i);
      txtValue[i].addFocusListener(new FocusListener() {
        public void focusLost(FocusEvent fe) {
          retrieveparlist(selected);
        }

        public void focusGained(FocusEvent fe) {
        }
      });
      jp1.add(jp2);
    }
    pnlSouth.revalidate();
    pnlSouth.repaint();
  }

  public void setList(XRDcat aparent, int index, int number, String[] labels) {
    if (objParent != null) {
      // System.out.println("Retrieving");
      retrieveparlist();
    }
    if (objParent == aparent && theindex == index)
      return;
    // System.out.println("Changing list: " + aparent.getLabel() + " " + index +
    // " " + number + " " + labels[0]);
    objParent = aparent;
    theindex = index;
    fieldNumber = number;
    setFields(labels);
    if (objParent != null) {
      selected = -1;
      int numb = objParent.subordinateloopField[theindex].setList(lstAtomicElements);
      if (txtTotTF != null)
        txtTotTF.setText(String.valueOf(numb));
      if (numb > 0) {
        setparameterlist(0);
        selected = 0;
      }
    }
    // initListener();
  }

  public void setparameterlist(int numb) {
    int totnumb = objParent.numberofelementSubL(theindex);
    if (totnumb > numb) {
      lstAtomicElements.setSelectedIndex(numb);
      setparameterlist();
    }
  }

  public XRDcat selectedObject = null;

  public void setparameterlist() {
    if (objParent != null) {
      XRDcat obj = (XRDcat) objParent.subordinateloopField[theindex].selectedElement();
      if (obj != null && obj != selectedObject)
        selectedObject = obj;
      for (int i = 0; i < fieldNumber; i++) {
        Parameter apar = obj.parameterField[i];
        if (apar != null) {
          ((myJFrame) getFrameParent()).removeComponentfromlist(txtValue[i]);
          ((myJFrame) getFrameParent()).addComponenttolist(txtValue[i], apar);
          txtValue[i].setText(apar.getValue());
        } /*
           * else { ((myJFrame)
           * getFrameParent()).removeComponentfromlist(valueTF[i]); }
           */
      }
    }
  }

  public void retrieveparlist(int numb) {
    if (numb >= 0 && objParent != null) {
      XRDcat obj = (XRDcat) objParent.subordinateloopField[theindex].elementAt(numb);
      if (obj != null)
        for (int i = 0; i < fieldNumber; i++)
          obj.parameterField[i].setValue(txtValue[i].getText());
    }
  }

  public void retrieveparlist() {
    retrieveparlist(selected);
  }

  public void setparameterField(String label) {
    if (objParent != null)
      objParent.subordinateloopField[theindex].setLabelAt(label, lstAtomicElements.getSelectedIndex());
  }

  public String gettheLabel() {
    return "Parameter label:";
  }

  void btnAdd_Clicked() {
    // add a new parameter
    if (objParent != null && lstAtomicElements != null) {
      retrieveparlist(selected);
      selected = -1;
      objParent.addsubordinateloopField(theindex);
      int numb = objParent.numberofelementSubL(theindex);
      if (txtTotTF != null)
        txtTotTF.setText(String.valueOf(numb));
      setparameterlist(numb - 1);
    }
  }

  /**
   * Action on remove button cliccking.
   */
  protected void btnRemove_Clicked() {
    // remove selected parameter
    if (objParent != null && lstAtomicElements != null)
      if (lstAtomicElements.getSelectedIndex() >= 0) {
        selected = -1;
        if (objParent.removeselSubLField(theindex)) {
          int numb = objParent.numberofelementSubL(theindex);
          if (txtTotTF != null)
            txtTotTF.setText(String.valueOf(numb));
        }
        setparameterlist(0);
      }
  }

  /**
   * Action on atomic element list selection.
   */
  protected void doAtomicElements_ListSelect() {
    if (lstAtomicElements != null) {
      retrieveparlist(selected);
      selected = lstAtomicElements.getSelectedIndex();
      setparameterlist(selected);
    }
  }

  /**
   * Destroys objects.
   */
  public void dispose() {
    lstAtomicElements = null;
    objParent = null;
  }

}
