package com.universalrobots.toggleAcrossPlane.impl.installation;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class togglePlaneInstallationNodeView implements SwingInstallationNodeView<togglePlaneInstallationNodeContribution> {
	
	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;

	public static final int indentPX = 20;
	public static final String LT = "&lt;";
	public static final String GT = "&gt;";
	public static final String LINE1_1 = "<html>The script function <b>";
	public static final String LINE1_2 = "</b> is now available in the Expression Editor.";
	public static final String LINE2 = "This function returns True if the active TCP is located on the positive side of "
			+ "the provided plane, or False if the TCP is on the negative side of that plane.<html>";
	public static final String LINE3_1 = "<html><b>" + LT;
	public static final String LINE3_2 = GT + "</b> is the feature used to define the plane.<html>";
	public static final String LINE4_1 = "<html><b>" + LT;
	public static final String LINE4_2 = GT + "</b> is a string defining the plane. This must be either \"XY\", \"XZ\", or \"YZ\".<html>";
	
	
	public togglePlaneInstallationNodeView(ViewAPIProvider provider) {
		this.apiProvider = provider;
	}
	
	@Override
	public void buildUI(JPanel panel, togglePlaneInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Box content = Box.createVerticalBox();
		content.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentY(Component.TOP_ALIGNMENT);
		
		content.add(createLine1(contribution));
		content.add(createVerticalHalfSpacing());
		content.add(createTextSection(LINE2));
		content.add(createVerticalSpacing());
		content.add(createLine3(contribution));
		content.add(createVerticalHalfSpacing());
		content.add(createLine4(contribution));
		
		panel.add(content);
	}

	//Handle spacing	
	
		@SuppressWarnings("unused")
		private Component createHorizontalSpacing() {
			return Box.createRigidArea(new Dimension(10, 0));
		}
		
		@SuppressWarnings("unused")
		private Component createHorizontalSpacingIndent() {
			return Box.createRigidArea(new Dimension(indentPX+7, 0));
		}

		@SuppressWarnings("unused")
		private Component createVerticalSpacing() {
			return Box.createRigidArea(new Dimension(0, 15));
		}
		
		@SuppressWarnings("unused")
		private Component createVerticalHalfSpacing() {
			return Box.createRigidArea(new Dimension(0, 7));
		}
		
	//Handle text line creation
		
		private Box createTextSection(String textInput) {
			Box section = Box.createHorizontalBox();
			section.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel descriptionLabel = new JLabel(textInput);
			section.add(descriptionLabel);
			return section;
		}
		
		private Box createTextSection(JLabel textInput) {
			Box section = Box.createHorizontalBox();
			section.setAlignmentX(Component.LEFT_ALIGNMENT);
			section.add(textInput);
			return section;
		}
		
		private Box createLine1(togglePlaneInstallationNodeContribution contribution) {
			JLabel content = new JLabel();
			content.setText(LINE1_1 + contribution.getFunctionReadout() + LINE1_2);
			return createTextSection(content);
		}
		
		private Box createLine3(togglePlaneInstallationNodeContribution contribution) {
			JLabel content = new JLabel();
			content.setText(LINE3_1 + togglePlaneInstallationNodeContribution.ARGUMENT_NAMES[0] + LINE3_2);
			return createTextSection(content);
		}
		
		private Box createLine4(togglePlaneInstallationNodeContribution contribution) {
			JLabel content = new JLabel();
			content.setText(LINE4_1 + togglePlaneInstallationNodeContribution.ARGUMENT_NAMES[1] + LINE4_2);
			return createTextSection(content);
		}
		
}
