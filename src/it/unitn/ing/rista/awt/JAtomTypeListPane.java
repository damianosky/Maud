/*
 * @(#)JAtomTypeListPane.java created 08/06/16 Casalino
 *
 * Copyright (c) 1996-2016 Luca Lutterotti All Rights Reserved.
 *
 * This software is the research result of the author and it is 
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
import it.unitn.ing.rista.diffr.*;
import it.unitn.ing.rista.util.Constants;
import it.unitn.ing.rista.util.MoreMath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The JAtomTypeListPane is a class ....
 *
 * @author Luca Lutterotti, revised by Damiano Martorelli.
 * @version $Revision: 1.2 $, $Date: 2017/08/04 10:55 $
 * @since JDK1.1
 */

public class JAtomTypeListPane extends JSubordListPane {

  
  /**
   * Serial ID
   */
  private static final long serialVersionUID  = -5622779308651768257L;
  /**
   * Defines the button for selecting atom from periodic table.
   */
  protected JButton         btnAtomTypeChoice = new JIconButton("PeriodicTable.gif");

  /**
   * Class constructor.
   * 
   * @param parent
   *          is the reference to the parent frame.
   * @param showTotal
   *          is the flag which commands if all is shown.
   */
  public JAtomTypeListPane(Frame parent, boolean showTotal) {
	super(parent, showTotal);
  }

	public void addCustomControlsToFieldsPanel() {
		JPanel pnlAtomButtons = new JPanel(new FlowLayout());

		this.pnlCenter.add(pnlAtomButtons, BorderLayout.NORTH);

		pnlAtomButtons.add(new JLabel("Atom type:"));
		pnlAtomButtons.add(btnAtomTypeChoice);
		btnAtomTypeChoice.setToolTipText("Press to select the atom type");
		btnAtomTypeChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				chooseTheAtom();
			}
		});
		JButton infoButton = new JButton("Info");
		infoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showInfoPanel();
			}
		});

		pnlAtomButtons.add(infoButton);


	}
	/**
	 * Sets the parameters for atom.
	 */
	public void setParameterList() {
		if (itsparent != null) {
			AtomScatterer scatterer = (AtomScatterer) itsparent.subordinateloopField[theindex].selectedElement();
			if (scatterer != null && scatterer != selectedObject) {
				selectedObject = scatterer;
				for (int i = 0; i < fieldNumber; i++) {
					Parameter apar = scatterer.parameterField[i];
					if (apar != null) {
						((myJFrame) getFrameParent()).removeComponentfromlist(valueTF[i]);
//						System.out.println("Adding jatom: " + i + " " + valueTF[i].getText());
						((myJFrame) getFrameParent()).addComponenttolist(valueTF[i], apar);
						valueTF[i].setText(apar.getValue());
					}/* else {
//						System.out.println("Removing jatom: " + i + " " + valueTF[i].getText());
						((myJFrame) getFrameParent()).removeComponentfromlist(valueTF[i]);
					}*/
				}
				btnAtomTypeChoice.setText(scatterer.getAtomSymbol());
			}
		}
	}

	/**
	 * Activates the periodic table for atom selection.
	 */
	protected void chooseTheAtom() {
		AtomScatterer scatterer = null;
		if (itsparent != null) {
			scatterer = (AtomScatterer) itsparent.subordinateloopField[theindex].selectedElement();
			if (scatterer != null)
				ChooseAtomD.getAtomType(getFrameParent(), scatterer);
			btnAtomTypeChoice.setText(scatterer.getAtomSymbol());
		}
	}
	  
	/**
	 * Shows the info panel with plots of absorption (total and photo), scattering
	 * (coherent and incoherent).
	 */
	public void showInfoPanel() {
		AtomScatterer scatterer = (AtomScatterer) itsparent.subordinateloopField[theindex].selectedElement();
		if (scatterer == null)
			return;
		int atomicNumber = scatterer.getAtomicNumber();

		int plotCounts = 3000;
		double[] x = new double[plotCounts];

		double[] y = new double[plotCounts];
		double xstart = 1.01;
		double xstep = 0.01;
		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getTotalAbsorptionForAtomAndEnergy(atomicNumber, x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(getFrameParent(), x, y)).setVisible(true);

		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getCoherentScatteringForAtomAndEnergy(atomicNumber, x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(getFrameParent(), x, y)).setVisible(true);

		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getIncoherentScatteringForAtomAndEnergy(atomicNumber, x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(getFrameParent(), x, y)).setVisible(true);

		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getPhotoAbsorptionForAtomAndEnergy(atomicNumber, x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(getFrameParent(), x, y)).setVisible(true);

/*		for (int i = 0; i < plotCounts; i++) {
			x[i] = xstart + i * xstep;
			y[i] = XRayDataSqLite.getPhotoAbsorptionForAtomAndEnergyDiv(atomicNumber, x[i]);
			if (y[i] > 0)
				y[i] = MoreMath.log10(y[i]);
			else
				y[i] = 0;
		}
		(new PlotSimpleData(getFrameParent(), x, y)).setVisible(true);*/


	}


}
