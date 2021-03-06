/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Red Hat, Inc.
 ******************************************************************************/
package com.openshift.internal.restclient.model.v1beta3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;

import org.jboss.dmr.ModelNode;
import org.junit.BeforeClass;
import org.junit.Test;

import com.openshift.internal.restclient.model.BuildConfig;
import com.openshift.internal.restclient.model.build.ImageChangeTrigger;
import com.openshift.internal.restclient.model.build.WebhookTrigger;
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IBuildConfig;
import com.openshift.restclient.model.build.BuildSourceType;
import com.openshift.restclient.model.build.BuildStrategyType;
import com.openshift.restclient.model.build.BuildTriggerType;
import com.openshift.restclient.model.build.IBuildStrategy;
import com.openshift.restclient.model.build.IBuildTrigger;
import com.openshift.restclient.model.build.IGitBuildSource;
import com.openshift.restclient.model.build.ISTIBuildStrategy;
import com.openshift.restclient.utils.Samples;

/**
 * @author Jeff Cantrill
 */
public class BuildConfigTest {
	
	private static IBuildConfig config;
	private static IClient client;
	
	@BeforeClass
	public static void setup() throws Exception{
		client = mock(IClient.class);
		when(client.getBaseURL()).thenReturn(new URL("https://localhost:8443"));
		when(client.getOpenShiftAPIVersion()).thenReturn("v1beta3");
		ModelNode node = ModelNode.fromJSONString(Samples.V1BETA3_BUILD_CONFIG.getContentAsString());
		config = new BuildConfig(node, client, ResourcePropertiesRegistry.getInstance().get("v1beta3", ResourceKind.BuildConfig));
	}
	
	@Test
	public void getBuildTriggers(){
		IBuildTrigger [] exp = new IBuildTrigger[]{
				new WebhookTrigger(BuildTriggerType.github, "secret101","foo", "https://localhost:8443", "v1beta3","foo"),
				new WebhookTrigger(BuildTriggerType.generic, "secret101","foo", "https://localhost:8443", "v1beta3","foo"),
				new ImageChangeTrigger("", "", "")
		};
		assertArrayEquals(exp, config.getBuildTriggers().toArray());
	}
	
	@Test
	public void getOutputRespositoryName(){
		assertEquals("origin-ruby-sample",config.getOutputRepositoryName());
	}
	
	@Test
	public void getSourceURI(){
		assertEquals("git://github.com/openshift/ruby-hello-world.git", config.getSourceURI());
	}
	
	@Test
	public void getGitBuildSource(){
		IGitBuildSource source = config.<IGitBuildSource>getBuildSource();
		assertEquals(BuildSourceType.Git, source.getType());
		assertEquals("git://github.com/openshift/ruby-hello-world.git", source.getURI());
		assertEquals("Exp. to get the source ref","", source.getRef());
	}
	
	@Test
	public void getSTIBuildStrategy() {
		IBuildStrategy strategy = config.getBuildStrategy();
		assertEquals(BuildStrategyType.STI, strategy.getType());
		ISTIBuildStrategy sti = (ISTIBuildStrategy)strategy;
		assertEquals(new DockerImageURI("ruby-20-centos7:latest"), sti.getImage());
		assertEquals("alocation", sti.getScriptsLocation());
		assertEquals(1, sti.getEnvironmentVariables().size());
		assertTrue("Exp. to find the environment variable",sti.getEnvironmentVariables().containsKey("foo"));
		assertEquals("bar",sti.getEnvironmentVariables().get("foo"));
	}

}
