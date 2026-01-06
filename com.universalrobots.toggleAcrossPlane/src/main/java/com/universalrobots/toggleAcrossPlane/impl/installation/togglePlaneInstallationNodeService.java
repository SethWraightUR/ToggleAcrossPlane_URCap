package com.universalrobots.toggleAcrossPlane.impl.installation;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class togglePlaneInstallationNodeService implements SwingInstallationNodeService<togglePlaneInstallationNodeContribution, togglePlaneInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle(Locale locale) {
		// TODO Auto-generated method stub
		return "Toggle Across Plane";
	}

	@Override
	public togglePlaneInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new togglePlaneInstallationNodeView(apiProvider);
	}

	@Override
	public togglePlaneInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
			togglePlaneInstallationNodeView view, DataModel model, CreationContext context) {
		return new togglePlaneInstallationNodeContribution(apiProvider, model, view);
	}

}
