/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Red Hat, Inc.
 ******************************************************************************/
package com.openshift.restclient.model;

import java.util.List;

import com.openshift.restclient.model.build.IBuildSource;
import com.openshift.restclient.model.build.IBuildStrategy;
import com.openshift.restclient.model.build.IBuildTrigger;

/**
 * @author Jeff Cantrill
 */
public interface IBuildConfig extends IResource {

	/**
	 * Returns the source URL for a build
	 * @return
	 */
	String getSourceURI();
	
	/**
	 * Returns the policies which will trigger a build
	 * @return
	 */
	List<IBuildTrigger> getBuildTriggers();
	
	/**
	 * Returns the source info of the build
	 * @return
	 */
	<T extends IBuildSource> T getBuildSource();
	
	/**
	 * Returns the strategy to for building the source
	 * @return
	 */
	<T extends IBuildStrategy> T getBuildStrategy(); 	
	
	/**
	 * Retrieves the name of the repository where the
	 * resulting build image will be pushed
	 * @return
	 */
	String getOutputRepositoryName();
}
