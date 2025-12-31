package com.universalrobots.toggleAcrossPlane.impl.program;

import java.util.Collection;


import com.universalrobots.toggleAcrossPlane.impl.installation.togglePlaneInstallationNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.feature.FeatureModel;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.AssignmentNode;
import com.ur.urcap.api.domain.program.nodes.builtin.CommentNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.assignmentnode.ExpressionAssignmentNodeConfig;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.value.expression.Expression;
import com.ur.urcap.api.domain.value.expression.ExpressionBuilder;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.api.domain.variable.VariableFactory;

public class togglePlaneProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPIProvider apiProvider;
	private final ProgramAPI programAPI;
	private final togglePlaneProgramNodeView view;
	private final DataModel model;
	private final KeyboardInputFactory keyboardInputFactory;
	private final InputValidationFactory validatorFactory;
	private final togglePlaneInstallationNodeContribution installation;
	
	private static final String SELECTED_FEATURE = "selectedFeature";
	private static final String VARIABLE_NAME = "variableName";
	private static final String PLANE_NAME = "planeName";
	private static final String DEFAULT_VAR = "Plane_Toggle";
	private final String comment1 = "This node must be placed in BeforeStart.";
	
	private Variable var;
	
	public togglePlaneProgramNodeContribution(ProgramAPIProvider apiProvider, togglePlaneProgramNodeView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.programAPI = apiProvider.getProgramAPI();
		this.view = view;
		this.model = model;
		this.keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.validatorFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
		this.installation = apiProvider.getProgramAPI().getInstallationNode(togglePlaneInstallationNodeContribution.class);
		lockChildSequence();
		VariableFactory vf = programAPI.getVariableModel().getVariableFactory();
		try {
			var = vf.createGlobalVariable(DEFAULT_VAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.set(VARIABLE_NAME, var);
		model.set(PLANE_NAME, "XY");
		generateSubtree();
	}
	
	@Override
	public void openView() {
		view.updateView(this);
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Toggle Variable Across Plane";
	}

	@Override
	public boolean isDefined() {
		return (model.isSet(VARIABLE_NAME) && model.isSet(PLANE_NAME) && model.isSet(SELECTED_FEATURE));
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}
	
	public void generateSubtree() {
		clearSubtree();
		createSubtree();
	}
	
	//Handle subtree creation in Polyscope.
	private void clearSubtree() {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();
		TreeNode subTree = programModel.getRootTreeNode(this);
		try {
			for (TreeNode child : subTree.getChildren()) {
				subTree.removeChild(child);
			}
		} catch (TreeStructureException e) {
			e.printStackTrace();
		}
	}

	private void createSubtree() {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();
		ProgramNodeFactory nf = programModel.getProgramNodeFactory();
		TreeNode root = programModel.getRootTreeNode(this);
		try {
			CommentNode comment = nf.createCommentNode();
			comment.setComment(comment1);
			root.addChild(comment);
			
			AssignmentNode assn = nf.createAssignmentNode();
			ExpressionAssignmentNodeConfig assncf;
			if (isDefined()) {
				assncf = assn.getConfigFactory().createExpressionConfig(getVar(), createFunctionExpression());
				
				assn.setConfig(assncf);
				root.addChild(assn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void lockChildSequence() {
		ProgramModel programModel = programAPI.getProgramModel();
		TreeNode root = programModel.getRootTreeNode(this);
		root.setChildSequenceLocked(true);
	}
	
	public Collection<Feature> getFeatures() {
		FeatureModel fModel = programAPI.getFeatureModel();
		return fModel.getGeomFeatures();
	}
	
	public Feature getBaseFeature() {
		FeatureModel fModel = programAPI.getFeatureModel();
		return fModel.getBaseFeature();
	}
	
	public Feature getSelectedFeature() {
		return model.get(SELECTED_FEATURE, (Feature)null);
	}
	
	public String getSelectedFeatureName() {
		if (getSelectedFeature() == null) {
			return "your selected Feature"; }
		else {
			return getSelectedFeature().getName(); }
	}
	
	public void setFeature(final Feature feature) {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(SELECTED_FEATURE, feature);
				view.updateView(togglePlaneProgramNodeContribution.this);
				generateSubtree();
			}
		});
	}

	public void removeFeature() {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.remove(SELECTED_FEATURE);
				view.updateView(togglePlaneProgramNodeContribution.this);
				generateSubtree();
			}
		});
	}
	
	public boolean isFeatureSelected() {
		if (getSelectedFeature() == null) {
			return false; }
		else {
			return true;}
	}
	
	public void setVar(String name) {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.remove(VARIABLE_NAME);
				clearSubtree();
				createVariable(name);
				model.set(VARIABLE_NAME, var);
				view.updateView(togglePlaneProgramNodeContribution.this);
				createSubtree();
			}
		});
	}
	
	public Variable createVariable(String name) {
		VariableFactory vf = programAPI.getVariableModel().getVariableFactory();
		try {
			if (name != getVarName()) {
				var = vf.createGlobalVariable(name);
			}
			return var;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Expression createFunctionExpression() {
		ExpressionBuilder builder = apiProvider.getProgramAPI().getValueFactoryProvider().createExpressionBuilder();
		try {
			return builder.append("is_tcp_postive_of_plane(" + getSelectedFeatureName() + ", \"" + getPlane() + "\")").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Expression createFalseExpression() {
		ExpressionBuilder builder = apiProvider.getProgramAPI().getValueFactoryProvider().createExpressionBuilder();
		try {
			return builder.append("False").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getVarName() {
		return getVar().getDisplayName();
	}
	
	public Variable getVar() {
		return model.get(VARIABLE_NAME, var);
	}
	
	public void setPlane(String plane) {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(PLANE_NAME, plane);
				generateSubtree();
			}
		});
	}
	
	public String getPlane() {
		return model.get(PLANE_NAME, "XY");
	}
	
	public KeyboardTextInput getKeyboardForTextField() {
		KeyboardTextInput keyboardInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue(getVarName());
		keyboardInput.setErrorValidator(validatorFactory.createStringLengthValidator(1, 15));
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				if (!value.equals(getVarName())) {
					setVar(value);
				}
			}
		};
	}

	public void setInstallationModel() {
		String[] entry = {getVarName(), getSelectedFeatureName(), getPlane()};
		installation.setEntry(entry);
	}
	
	public String getDebug() {
		String s = " ";
		String varName;
		try {
			varName = model.get(VARIABLE_NAME, (Variable)null).getDisplayName();
		}
		catch (Exception e) {
			varName = "Variable undefined";
		}
		String feat;
		try {
			feat = model.get(SELECTED_FEATURE, (Feature)null).getName();
		}
		catch (Exception e) {
			feat = "Feature undefined";
		}
		
		String pln = model.get(PLANE_NAME, "Plane undefined");
		return varName+s+feat+s+pln;
	}
}
