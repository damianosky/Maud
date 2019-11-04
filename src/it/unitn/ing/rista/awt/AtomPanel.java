/*
 * @(#)AtomPanel.java created Aug 29, 2004 Pergine Valsugana
 *
 * Copyright (c) 1996-2004 Luca Lutterotti All Rights Reserved.
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

import it.unitn.ing.rista.chemistry.XRayDataSqLite;
import it.unitn.ing.rista.diffr.AtomScatterer;
import it.unitn.ing.rista.diffr.AtomSite;
import it.unitn.ing.rista.util.*;
import it.unitn.ing.rista.models.xyzTableModel;
import it.unitn.ing.rista.interfaces.AtomsStructureI;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * <p>
 * The AtomPanel defines the composition of a phase, with atom types, position
 * and parameters.
 * <p/>
 *
 * @version $Revision: 1.5 $, $Date: 2019/11/04 15:45:52 $
 * @author Luca Lutterotti, revised by Damiano Martorelli. 
 * @since JDK1.1
 */

public class AtomPanel extends JPanel {

  protected int iSiteSelectedIndex = -1;
  protected JList lstSiteLabel = null;
  protected JAtomTypeListPane pnlAtomTypeList;
  protected JTextField txtAtomquantity_incell = null;
  protected JTextField txtAtomQuantity = null;
  protected JTextField txtXCoord = null;
  protected JTextField txtYCoord = null;
  protected JTextField txtZCoord = null;
  protected JTextField txtBFactor = null;
  private JCheckBox ckBxQuantityFromOcc;
  private JCheckBox ckBxUseUinsteadOfB;
  private JCheckBox ckBxUseThisAtomCB;
  protected AtomsStructureI m_Struct;
  private myJFrame frmParent;

  private JPanel pnlAtomSites = null;
  private JButton btnAddSite;
  private JPanel pnlAtomButtons;
  private JButton btnPositions;
  private JButton btnDuplicate;
  private JPanel pnlAtomParamWest;
  private JPanel pnlAtomParameters;
  private JPanel pnlAtomTypes;
  private JPanel pnlAtomParamEast;
  private JPanel pnlAtomSiteTitle;
/**
 *  Class constructor.
 *
 * @param parentD the parent dialog form.
 * @param m_Struct the atom structure.
 */
  public AtomPanel(myJFrame parentD, AtomsStructureI m_Struct) {
    this.frmParent = parentD;
    this.m_Struct = m_Struct;
    initComponents();
    initListeners();
  }
  /**
   * Initializes panel
   */
  public void initComponents() {
    JPanel pnlAtomButtons = new JPanel(new GridLayout(0, 2));
    //
    JPanel jPanel13 = null;
    JPanel pnlMainContainer = new JPanel(new FlowLayout());
    add(pnlMainContainer);
    pnlMainContainer.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Atoms"));
    pnlMainContainer.add(pnlAtomSites = new JPanel(new BorderLayout()));
    //
    JPanel pnlAtomOptions = new JPanel(new BorderLayout());
    pnlAtomSites.add(pnlAtomOptions, BorderLayout.SOUTH);
    pnlAtomOptions.add(pnlAtomButtons, BorderLayout.CENTER);
    //
    JButton btnAddSite = new JButton("Add site");
    pnlAtomButtons.add(btnAddSite);
    btnAddSite.setToolTipText("Add a new site to the list");
    btnAddSite.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        addNewSiteAction();
      }
    });

    pnlAtomButtons.add(btnDuplicate = new JButton("Duplicate"));
    btnDuplicate.setToolTipText("Duplicate the selected sites in the list");
    btnDuplicate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        duplicateAtoms();
      }
    });

    final JRemoveButton btnRemove = new JRemoveButton();
    btnRemove.setToolTipText("Remove the selected sites from the list");
    pnlAtomButtons.add(btnRemove);
    btnRemove.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        if (!Constants.confirmation || Utility.areYouSureToRemove("Remove the selected sites?"))
          removeSiteAction();
      }
    });
	  pnlAtomButtons.add(btnPositions = new JIconButton("NewSheet.gif", "Positions"));
	  btnPositions.setToolTipText("Shows a list of the equivalent positions for this site");
	  btnPositions.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent event) {
			  show_xyz();
		  }
	  });

    JPanel pnlCheckBoxes = new JPanel(new FlowLayout());
    pnlAtomOptions.add(pnlCheckBoxes, BorderLayout.SOUTH);
    pnlCheckBoxes.add(pnlAtomButtons = new JPanel(new GridLayout(0, 1, 3, 3)));
    pnlAtomButtons.add(ckBxUseUinsteadOfB = new JCheckBox("Use U instead of B for thermal factors"));
	ckBxUseUinsteadOfB.setToolTipText("Select this to use Biso factor instead of dimensionless Uiso (also for anisotropic)");
	if (m_Struct != null)
	  ckBxUseUinsteadOfB.setSelected(m_Struct.isDebyeWallerModelDimensionLess());
	ckBxUseUinsteadOfB.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
		  m_Struct.setDebyeWallerModelDimensionLess(ckBxUseUinsteadOfB.isSelected());
	  }
	});
	pnlAtomButtons.add(ckBxQuantityFromOcc = new JCheckBox("Compute quantity from occupancy"));
	//
	ckBxQuantityFromOcc.setToolTipText("Uncheck this to compute occupancy from quantity");
    if (m_Struct != null)
	  ckBxQuantityFromOcc.setSelected(m_Struct.getQuantityFromOccupancy());
	ckBxQuantityFromOcc.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
		  m_Struct.setQuantityFromOccupancy(ckBxQuantityFromOcc.isSelected());
		}
	});
	pnlAtomButtons.add(ckBxUseThisAtomCB = new JCheckBox("Use it in the computation"));
	//
	ckBxUseThisAtomCB.setToolTipText("Uncheck this to set this atom as dummy");
    //
	lstSiteLabel = new JList();
	lstSiteLabel.setVisibleRowCount(6);
	lstSiteLabel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	//
	JScrollPane scrlPaneAtomSite = new JScrollPane();
	scrlPaneAtomSite.setViewportView(lstSiteLabel);
	pnlAtomSites.add(scrlPaneAtomSite, BorderLayout.CENTER);
	pnlAtomSites.add(pnlAtomSiteTitle = new JPanel(new BorderLayout(0, 0)), BorderLayout.NORTH);
	pnlAtomSiteTitle.add(new JLabel("Atom site list:"), BorderLayout.WEST);
	pnlMainContainer.add(pnlAtomTypes = new JPanel(new BorderLayout()));
    //
	pnlAtomTypeList = new JAtomTypeListPane(frmParent, false);
	pnlAtomTypeList.setBorder(new TitledBorder(
		  new BevelBorder(BevelBorder.LOWERED), "Atom types in the site:"));
    //
	pnlAtomTypes.add(pnlAtomTypeList, BorderLayout.NORTH);
	pnlAtomTypes.add(pnlAtomParameters = new JPanel(new BorderLayout()), BorderLayout.CENTER);
    //
    pnlAtomParameters.add(pnlAtomParamWest = new JPanel(new GridLayout(0, 1, 3, 3)), BorderLayout.WEST);
    pnlAtomParamWest.add(new JLabel(" Total quantity:"));
    pnlAtomParamWest.add(new JLabel("Total occupancy:"));
    pnlAtomParamWest.add(new JLabel("              x:"));
    pnlAtomParamWest.add(new JLabel("              y:"));
    pnlAtomParamWest.add(new JLabel("              z:"));
	String Bstring = "    Biso factor:";
	if (m_Struct != null && m_Struct.isDebyeWallerModelDimensionLess())
	{
	  Bstring = "    Uiso factor:";
	}
    pnlAtomParamWest.add(new JLabel(Bstring));

    pnlAtomParameters.add(pnlAtomParamEast = new JPanel(new GridLayout(0, 1, 3, 3)), BorderLayout.CENTER);
    //
    txtAtomquantity_incell.setToolTipText("Set the number of atoms in the cell");
    //
    pnlAtomParamEast.add(txtAtomquantity_incell = new JTextField("1", 6));
    pnlAtomParamEast.add(txtAtomQuantity = new JTextField("1", 6));
    pnlAtomParamEast.add(txtXCoord = new JTextField("0", Constants.FLOAT_FIELD));
    pnlAtomParamEast.add(txtYCoord = new JTextField("0", Constants.FLOAT_FIELD));
    pnlAtomParamEast.add(txtZCoord = new JTextField("0", Constants.FLOAT_FIELD));
    pnlAtomParamEast.add(txtBFactor = new JTextField("0", Constants.FLOAT_FIELD));

    initAtomList();

  }
  /**
   * Initializes listener.
   */
	public void initListeners() {
    lstSiteLabel.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        sitelabellist_ListSelect();
      }
    });
    if (m_Struct.getAtomList().size() > 0)
      iSiteSelectedIndex = 0;
  }
/**
 * Retrieves data parameters for selected atom.
 */
  void retrieveAtom() {
    if (iSiteSelectedIndex >= 0) {
      AtomSite anatom = getSelectedSite();
      if (anatom != null) {
	      pnlAtomTypeList.retrieveparlist();
        anatom.setQuantity(txtAtomquantity_incell.getText());
        anatom.getOccupancy().setValue(txtAtomQuantity.getText());
        anatom.getBfactor().setValue(txtBFactor.getText());
        anatom.getLocalCoordX().setValue(txtXCoord.getText());
        anatom.getLocalCoordY().setValue(txtYCoord.getText());
        anatom.getLocalCoordZ().setValue(txtZCoord.getText());
        anatom.setDummy(!ckBxUseThisAtomCB.isSelected());
      }
      anatom.refreshPositions(false);
      anatom.refreshOccupancyAndQuantity();
    }
  }
  /**
   * Retrieves parameters.
   */
  void retrieveParameters() {
    retrieveAtom();
  }
  /**
   * Sets the atom component according to AtomSite object passed.
   * 
   * @param anatom
   *          a AtomSite object reference.
   */
  public void setAtomComponent(AtomSite anatom) {
    if (anatom != null) {
	    String labels[] = {"Partial occupancy: "};
	    pnlAtomTypeList.setList(anatom, 0, labels.length, labels);
	    frmParent.addComponenttolist(txtAtomQuantity, anatom.getOccupancy());
      frmParent.addComponenttolist(txtXCoord, anatom.getLocalCoordX());
      frmParent.addComponenttolist(txtYCoord, anatom.getLocalCoordY());
      frmParent.addComponenttolist(txtZCoord, anatom.getLocalCoordZ());
      frmParent.addComponenttolist(txtBFactor, anatom.getBfactor());
    } else {
      frmParent.removeComponentfromlist(txtAtomQuantity);
      frmParent.removeComponentfromlist(txtXCoord);
      frmParent.removeComponentfromlist(txtYCoord);
      frmParent.removeComponentfromlist(txtZCoord);
      frmParent.removeComponentfromlist(txtBFactor);
	    txtAtomQuantity.setText("0");
      txtXCoord.setText("0");
      txtYCoord.setText("0");
      txtZCoord.setText("0");
      txtBFactor.setText("0");
    }
  }
  /**
   * Gets the selected atom.
   * 
   * @return a AtomSite object reference.
   */
  public AtomSite getSelectedAtom() {
    return (AtomSite) m_Struct.getAtomList().selectedElement();
  }
  /**
   * Gets the atom for the selected site.
   * 
   * @return a AtomSite object reference.
   */
  public AtomSite getSelectedSite() {
//		return getSelectedAtom();
    if (iSiteSelectedIndex >= 0 && iSiteSelectedIndex < m_Struct.getAtomList().size())
      return (AtomSite) m_Struct.getAtomList().elementAt(iSiteSelectedIndex);
    else
      return null;
  }
  /**
   * Initializes atoms.
   */
  public void initAtomList() {
    int sitenumb = m_Struct.getAtomList().setList(lstSiteLabel);
//		atomnumber = loadatomtable();
//		totalatomnumber.setText(String.valueOf(sitenumb));
    if (sitenumb > 0)
      setatomsite(0);
  }
  /**
   * Sets the atom site.
   */
  public void setatomsite() {
    AtomSite anatom = getSelectedAtom();
    if (anatom != null) {
      anatom.refreshPositions(false);
      anatom.refreshOccupancyAndQuantity();
      txtAtomquantity_incell.setText(anatom.getQuantity());
      txtAtomQuantity.setText(anatom.getOccupancy().getValue());
      txtXCoord.setText(anatom.getLocalCoordX().getValue());
      txtYCoord.setText(anatom.getLocalCoordY().getValue());
      txtZCoord.setText(anatom.getLocalCoordZ().getValue());
      txtBFactor.setText(anatom.getBfactor().getValue());
      ckBxUseThisAtomCB.setSelected(!anatom.isDummyAtom());
    }
    setAtomComponent(anatom);
  }
  /**
   * Sets the specified atom site.
   * 
   * @param numb
   *          the site position.
   */
  public void setatomsite(int numb) {
    lstSiteLabel.setSelectedIndex(numb);
    setatomsite();
  }

/*    public String gettheLabel() {
      return "Site label:";
    }*/
  /**
   * Ads new atom site.
   */
  void addNewSiteAction() {
    // add a new atom site
    retrieveAtom();
    iSiteSelectedIndex = -1;
    m_Struct.addAtom();
    setAtomComponent(null);

//			update3dStructure();

//		int sitenumb = thephase.getAtomNumber();
//		totalatomnumber.setText(String.valueOf(sitenumb));
//		setatomsite(sitenumb-1);
  }
  /**
   * Duplicates selected atom.
   */
  void duplicateAtoms() {
    AtomSite[] selAtom = getSelectedAtoms();
    if (selAtom != null) {
      iSiteSelectedIndex = -1;
      setAtomComponent(null);
      for (int i = 0; i < selAtom.length; i++) {
        AtomSite newAtom = (AtomSite) selAtom[i].getCopy(selAtom[i].getParent());
        newAtom.setLabel(selAtom[i].getLabel() + " copy");
        newAtom.setParent(selAtom[i].getParent());
        m_Struct.addAtom(newAtom);
      }
    }
  }
  /**
   * Gets selected atoms.
   * 
   * @return a AtomSite object array.
   */
  public AtomSite[] getSelectedAtoms() {
    int[] selAtom = lstSiteLabel.getSelectedIndices();
    AtomSite[] atomlist = null;
    if (selAtom != null) {
      atomlist = new AtomSite[selAtom.length];
      for (int i = 0; i < selAtom.length; i++) {
        atomlist[i] = m_Struct.getAtom(selAtom[i]);
      }
    }
    return atomlist;
  }
  /**
   * Removes sites.
   */
  public void removeSiteAction() {
    // remove selected atom
    int[] selAtom = lstSiteLabel.getSelectedIndices();
    if (selAtom != null) {
      iSiteSelectedIndex = -1;
      setAtomComponent(null);
      for (int i = selAtom.length - 1; i >= 0; i--)
        m_Struct.removeAtomAt(selAtom[i]);
    }
  }
  /**
   * Updates the label with the selected site name.
   */
  void sitelabellist_ListSelect() {
    retrieveAtom();
    if (lstSiteLabel != null) {
      iSiteSelectedIndex = lstSiteLabel.getSelectedIndex();
      setatomsite();
    }
  }

/*    void sitelabellist_DblClicked() {
      final LabelD labD = new LabelD(parentD, "Edit site label", true);
      labD.setTextField(getField());
      labD.jbok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          setField(labD.getTextField());
          labD.dispose();
        }
      });
    }

    public void setField(String label) {
      m_Struct.getAtomList().setLabelAt(label, sitelabellist.getSelectedIndex());
    }

    public String getField() {
      return sitelabellist.getSelectedValue().toXRDcatString();
    } */
  /**
   * Shows xyz coordinates.
   */
  public void show_xyz() {
    AtomSite anatom = getSelectedAtom();
    if (anatom != null) {
      final myJFrame jf = new myJFrame(frmParent, "xyz list of: " + anatom.toXRDcatString());
      jf.createDefaultMenuBar();
      TableModel xyzModel = new xyzTableModel(anatom);
      JTable xyztable = new JTable(xyzModel);
      JScrollPane scrollpane = new JScrollPane(xyztable);
//			scrollpane.setBorder(new LineBorder(Color.black));
      Container c1 = jf.getContentPane();
      c1.setLayout(new BorderLayout());
      c1.add(scrollpane, BorderLayout.CENTER);
      JPanel jp = new JPanel();
      jp.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 6));
      JCloseButton jb = new JCloseButton();
      jb.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          jf.setVisible(false);
          jf.dispose();
        }
      });
      jp.add(jb);
      c1.add(jp, BorderLayout.SOUTH);
      getRootPane().setDefaultButton(jb);
      jf.setSize(300, 250);
      jf.setVisible(true);
    } else
      System.out.println("No atom or site selected");
  }
  /**
   * Shows info panel.
   */
	public void showInfoPanel() {
		AtomSite atomSite = getSelectedAtom();
		if (atomSite == null)
			return;
		AtomScatterer atomScat = (AtomScatterer) atomSite.subordinateloopField[AtomSite.scattererLoopID].selectedElement();
		if (atomScat == null)
			return;
		int atomicNumber = atomScat.getAtomicNumber();

		int plotCounts = 3000;
		double[] x = new double[plotCounts];
		double[] y = new double[plotCounts];
		double xstart = 1.01;
		double xstep = 0.01;
		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getTotalAbsorptionForAtomAndEnergy(atomicNumber, x[i]);
//			x[i] = MoreMath.log10(x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(this.frmParent, x, y, false)).setVisible(true);

	}
  /**
   * Disposes panel.
   */
  public void dispose() {
    lstSiteLabel = null;
	  pnlAtomTypeList = null;
    iSiteSelectedIndex = -1;
  }

}

