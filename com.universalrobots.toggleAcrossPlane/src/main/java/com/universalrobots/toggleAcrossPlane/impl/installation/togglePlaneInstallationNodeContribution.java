package com.universalrobots.toggleAcrossPlane.impl.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.InstallationAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class togglePlaneInstallationNodeContribution implements InstallationNodeContribution {

	@SuppressWarnings("unused")
	private final DataModel model;
	@SuppressWarnings("unused")
	private final togglePlaneInstallationNodeView view;
	private final InstallationAPI installationAPI;
	
	public static final String FUNCTION_NAME = "is_tcp_postive_of_plane";
	public static final String[] ARGUMENT_NAMES = {"feature", "plane"};
	public static final String LT = "&lt;";
	public static final String GT = "&gt;";
	public static final String CURR = "currPosTransLocal";
	public static final String THREAD_NAME = "toggle_plane_urcap_thread";
	public static final String THREAD_HANDLE = "toggle_plane_urcap_thread_handle";
	public static final String[] planes = {"XY", "XZ", "YZ"};
	
	public togglePlaneInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, togglePlaneInstallationNodeView view) {
		this.model = model;
		this.view = view;
		this.installationAPI = apiProvider.getInstallationAPI();
		try {
			installationAPI.getFunctionModel().addFunction(FUNCTION_NAME, ARGUMENT_NAMES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		//generate function
		writer.appendLine("def " + getFunctionDef() + ":");
		writer.appendLine("  " + CURR + " = pose_inv(pose_trans(pose_inv(get_actual_tcp_pose()), feature))");
		writer.appendLine("  if (plane == \"XY\"):");
		writer.appendLine("    return currPosTransLocal[2]>=0");
		writer.appendLine("  elif (plane == \"XZ\"):");
		writer.appendLine("    return currPosTransLocal[1]>=0");
		writer.appendLine("  elif (plane == \"YZ\"):");
		writer.appendLine("    return currPosTransLocal[0]>=0");
		writer.appendLine("  end");
		writer.end();
	}

	public String getFunctionReadout() {
		return FUNCTION_NAME + "(" + LT + ARGUMENT_NAMES[0] + GT + ", " + LT + ARGUMENT_NAMES[1] + GT + ")";
	}
	
	public String getFunctionDef() {
		return FUNCTION_NAME + "(" + ARGUMENT_NAMES[0] + ", " + ARGUMENT_NAMES[1] + ")";
	}
}
