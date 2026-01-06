package com.universalrobots.toggleAcrossPlane.impl.program;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class togglePlaneProgramNodeView implements SwingProgramNodeView<togglePlaneProgramNodeContribution> {

	@SuppressWarnings("unused")
	private final ViewAPIProvider apiProvider;
	
	public static final int indentPX = 60;
	public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(250, 30);
	private static final String BEFORE_START_WARNING = "<html><p style=\"color: red;\"><b>This node MUST be placed in BeforeStart</b></p></html>";
	private static final String BEFORE_START_WARNING2 = "<html>Thread errors <b>will</b> occur if this node is placed anywhere else</html>";
	private static final String EXPLANATION = "<html>While the program is running, the named variable will be set True when the robot's TCP is "
			+ "on the positive side of the selected plane, and set False when the TCP is on the negative side of that plane.</html>";
	private static final String LINE1 = "<html>Name the variable to be toggled as the robot passes through the plane:</html>";
	private static final String LINE2 = "<html>Select the feature to toggle relative to:</html>";
	private static final String LINE3_1 = "<html>Select the plane of ";
	private static final String LINE3_2 = " across which the variable will toggle:</html>";
	private static final String LINE4 = "<html>Note: multiple toggles can be defined by adding additional Toggle Across Plane nodes.</html>";
	public static final String NO_FEATURE = "<Feature>";
	public static final String DEFAULT_LABEL = "your selected Feature";
	public static final String XY = "XY";
	public static final String XZ = "XZ";
	public static final String YZ = "YZ";
	
	public static final Color WHITE = new Color(255,255,255);
	public static final Color YELLOW = new Color(255,255,112);
	
	private Color featuresColor = YELLOW;
	private JComboBox<Object> featuresComboBox = new JComboBox<>();
	private Box comboBox = Box.createHorizontalBox();
	private JTextField nameField = new JTextField();
	private KeyboardTextInput nameKeyboard;
	private JRadioButton XYButton = new JRadioButton(XY);
	private JRadioButton XZButton = new JRadioButton(XZ);
	private JRadioButton YZButton = new JRadioButton(YZ);
	private ButtonGroup radioButtons = new ButtonGroup();
	private ActionListener listener;
	private ImageIcon imgXY = new ImageIcon(getClass().getResource("/images/XY.png"));
	private ImageIcon imgXZ = new ImageIcon(getClass().getResource("/images/XZ.png"));
	private ImageIcon imgYZ = new ImageIcon(getClass().getResource("/images/YZ.png"));
	
	private JLabel line3 = new JLabel();
	private JLabel debug = new JLabel();
	private JLabel image = new JLabel(imgXY);
	
	public togglePlaneProgramNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Box content = Box.createVerticalBox();
		content.add(createCenteredTextSection(BEFORE_START_WARNING));
		content.add(createCenteredTextSection(BEFORE_START_WARNING2));
		
		content.add(createVerticalDoubleSpacing());
		content.add(createTextSection(LINE1));
		content.add(createVerticalHalfSpacing());
		content.add(createMidSection(provider));
		content.add(createVerticalDoubleSpacing());
		content.add(createTextSection(line3));
		content.add(createVerticalHalfSpacing());
		content.add(createRadioButtons(provider));
		content.add(createVerticalDoubleSpacing());
		content.add(createTextSection(EXPLANATION));
		content.add(createVerticalSpacing());
		content.add(createTextSection(LINE4));
//		content.add(createVerticalHalfSpacing());
//		content.add(debug);
		panel.add(content);
	}
	
	
	//Handle spacing	
	
	@SuppressWarnings("unused")
	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(10, 0));
	}
	
	@SuppressWarnings("unused")
	private Component createHorizontalSpacingIndent() {
		return Box.createRigidArea(new Dimension(indentPX, 0));
	}
	
	@SuppressWarnings("unused")
	private Component createHorizontalSpacingButtons() {
		return Box.createRigidArea(new Dimension(30, 0));
	}
	
	@SuppressWarnings("unused")
	private Component createHorizontalGlue() {
		return Box.createHorizontalGlue();
	}
	
	@SuppressWarnings("unused")
	private Component createVerticalDoubleSpacing() {
		return Box.createRigidArea(new Dimension(0, 40));
	}

	@SuppressWarnings("unused")
	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, 20));
	}
	
	@SuppressWarnings("unused")
	private Component createVerticalHalfSpacing() {
		return Box.createRigidArea(new Dimension(0, 10));
	}
	
	@SuppressWarnings("unused")
	private Component createVerticalSmallSpacing() {
		return Box.createRigidArea(new Dimension(0, 4));
	}
	
	@SuppressWarnings("unused")
	private Component createVerticalGlue() {
		return Box.createVerticalGlue();
	}
		
	//Handle text line creation
		
	private Box createTextSection(String textInput) {
		Box section = Box.createHorizontalBox();
		JLabel descriptionLabel = new JLabel(textInput);
		section.add(descriptionLabel);
		section.setAlignmentX(Box.LEFT_ALIGNMENT);
		return section;
	}
	
	private Box createTextSection(JLabel textInput) {
		Box section = Box.createHorizontalBox();
		section.add(textInput);
		section.setAlignmentX(Box.LEFT_ALIGNMENT);
		return section;
	}
	
	private Box createCenteredTextSection(String textInput) {
		Box section = Box.createHorizontalBox();
		JLabel descriptionLabel = new JLabel(textInput);
		descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
		descriptionLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		section.add(descriptionLabel);
		section.setAlignmentX(Box.LEFT_ALIGNMENT);
		return section;
	}
	
	//Handle features dropdown menu
	
	@SuppressWarnings("serial")
	private Box createFeaturesComboBox(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		comboBox.setAlignmentY(Component.TOP_ALIGNMENT);

		featuresComboBox.setFocusable(false);
		featuresComboBox.setPreferredSize(TEXT_FIELD_DIMENSION);
		featuresComboBox.setMaximumSize(TEXT_FIELD_DIMENSION);
		featuresComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					if (itemEvent.getItem() instanceof Feature) {
						provider.get().setFeature((Feature) itemEvent.getItem(), itemEvent.getItem().equals(provider.get().getBaseFeature()));
					} else if (itemEvent.getItem() instanceof String && !itemEvent.getItem().equals(NO_FEATURE)) {
						provider.get().setFeature((Feature) featuresComboBox.getItemAt(featuresComboBox.getSelectedIndex()-1), true);
					} else {
						provider.get().removeFeature();
					}
				}
			}
		});
			
		featuresComboBox.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, 
		            int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        
		        c.setBorder(new EmptyBorder(5, 5, 5, 0)); 
		        
		        // index == -1 indicates the "selected item" box (not an item in the list)
		        if (index == -1) {
		            c.setBackground(featuresColor);
		        }
		        return c;
		    }
		});
		comboBox.add(createHorizontalSpacingIndent());
		comboBox.add(featuresComboBox);
		return comboBox;
	}
			
	private void updateFeaturesComboBox(togglePlaneProgramNodeContribution contribution) {
		DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
		List<Feature> features = getSortedFeatures(contribution);

		model.addElement(NO_FEATURE);
			
		for (Feature feature : features) {
			model.addElement(feature);
			if (!feature.equals(contribution.getBaseFeature())) {
				model.addElement(feature.getName()+"_const");
			}
		}
		Feature selectedFeature = contribution.getSelectedFeature();
		if (selectedFeature != null) {
			if (selectedFeature.equals(contribution.getBaseFeature())) {
				model.setSelectedItem(selectedFeature);
			}
			else if (contribution.getIsConstant()) {
				model.setSelectedItem(selectedFeature.getName()+"_const");
			}
			else {
				model.setSelectedItem(selectedFeature);
			}
			featuresColor = WHITE;
		}
		else {
			model.setSelectedItem(NO_FEATURE);
			featuresColor = YELLOW;
		}
		featuresComboBox.setModel(model);
	}
	
	private List<Feature> getSortedFeatures(togglePlaneProgramNodeContribution contribution) {
		List<Feature> sortedFeatures = new ArrayList<Feature>(contribution.getFeatures());
		Collections.sort(sortedFeatures, new Comparator<Feature>() {
			@Override
			public int compare(Feature var1, Feature var2) {
				if (var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase()) == 0) {
					//Sort lowercase/uppercase consistently
					return var1.toString().compareTo(var2.toString());
				} else {
					return var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase());
				}
			}
		});
		sortedFeatures.add(0, contribution.getBaseFeature());
		return sortedFeatures;
	}
	
	//Handle variable name input field
	private Box createNameField(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setPreferredSize(TEXT_FIELD_DIMENSION);
		nameField.setMaximumSize(TEXT_FIELD_DIMENSION);
		nameField.setFocusable(false);
		nameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				nameKeyboard = provider.get().getKeyboardForTextField();
				nameKeyboard.show(nameField, provider.get().getCallbackForTextField());
			}
		});
		inputBox.add(createHorizontalSpacingIndent());
		inputBox.add(nameField);
		return inputBox;
	}
	
	private Box createRadioButtons(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputBox.setAlignmentY(Component.TOP_ALIGNMENT);
		
		listener = createListener(provider);
		XYButton.setActionCommand(XY);
		XYButton.addActionListener(listener);
		XZButton.setActionCommand(XZ);
		XZButton.addActionListener(listener);
		YZButton.setActionCommand(YZ);
		YZButton.addActionListener(listener);
		radioButtons.add(XYButton);
		radioButtons.add(XZButton);
		radioButtons.add(YZButton);
		
		inputBox.add(createHorizontalSpacingIndent());
		inputBox.add(XYButton);
		inputBox.add(createHorizontalSpacingButtons());
		inputBox.add(XZButton);
		inputBox.add(createHorizontalSpacingButtons());
		inputBox.add(YZButton);
		inputBox.add(createHorizontalGlue());
		
		return inputBox;
	}
	
	public ActionListener createListener(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        provider.get().setPlane(e.getActionCommand());
				updateView(provider.get());
			}
		};
	}
	
	private Box createMidSection(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Box.LEFT_ALIGNMENT);
		section.add(createMidLeftSection(provider));
		section.add(createImageSection());
		section.add(createHorizontalGlue());
		return section;
	}
	
	private Box createImageSection() {
		Box section = Box.createHorizontalBox();
		section.add(image);
		section.add(createHorizontalGlue());
		section.add(createHorizontalSpacingIndent());
		section.setAlignmentX(Box.LEFT_ALIGNMENT);
		return section;
	}
	
	private Box createMidLeftSection(ContributionProvider<togglePlaneProgramNodeContribution> provider) {
		Box content = Box.createVerticalBox();
		content.setAlignmentX(Box.LEFT_ALIGNMENT);
		content.add(createNameField(provider));
		content.add(createVerticalDoubleSpacing());
		content.add(createTextSection(LINE2));
		content.add(createVerticalHalfSpacing());
		content.add(createFeaturesComboBox(provider));
		return content;
	}
	
	public void updateView(togglePlaneProgramNodeContribution contribution) {
		updateFeaturesComboBox(contribution);
		String varName = contribution.getVarName();
		nameField.setText(varName);
		String plane = contribution.getPlane();
		if (plane.equals(XY)) {
			XYButton.setSelected(true);
			image.setIcon(imgXY);
		}
		else if (plane.equals(XZ)) {
			XZButton.setSelected(true);
			image.setIcon(imgXZ);
		}
		else if (plane.equals(YZ)) {
			YZButton.setSelected(true);
			image.setIcon(imgYZ);
		}
		line3.setText(LINE3_1 + contribution.getSelectedFeatureName() + LINE3_2);
		debug.setText(contribution.getDebug());
	}

}
