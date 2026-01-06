package com.universalrobots.toggleAcrossPlane.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.universalrobots.toggleAcrossPlane.impl.installation.togglePlaneInstallationNodeService;
import com.universalrobots.toggleAcrossPlane.impl.program.togglePlaneProgramNodeService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Toggle Across Plane registering");
		bundleContext.registerService(SwingInstallationNodeService.class, new togglePlaneInstallationNodeService(), null);
		bundleContext.registerService(SwingProgramNodeService.class, new togglePlaneProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

