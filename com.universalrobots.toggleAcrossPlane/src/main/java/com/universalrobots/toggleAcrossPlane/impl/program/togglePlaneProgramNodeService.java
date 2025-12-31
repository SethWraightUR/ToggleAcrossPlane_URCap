package com.universalrobots.toggleAcrossPlane.impl.program;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class togglePlaneProgramNodeService implements SwingProgramNodeService<togglePlaneProgramNodeContribution, togglePlaneProgramNodeView> {

	@Override
	public String getId() {
		return "toggleAcrossPlaneNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setDeprecated(false);
		configuration.setChildrenAllowed(true);
		configuration.setUserInsertable(true);		
	}

	@Override
	public String getTitle(Locale locale) {
		return "Toggle Across Plane";
	}

	@Override
	public togglePlaneProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new togglePlaneProgramNodeView(apiProvider);
	}

	@Override
	public togglePlaneProgramNodeContribution createNode(ProgramAPIProvider apiProvider,
			togglePlaneProgramNodeView view, DataModel model, CreationContext context) {
		return new togglePlaneProgramNodeContribution(apiProvider, view, model);
	}


}
