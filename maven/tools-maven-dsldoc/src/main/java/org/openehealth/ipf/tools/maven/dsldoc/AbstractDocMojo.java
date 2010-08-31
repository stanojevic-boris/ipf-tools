/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tools.maven.dsldoc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for Mojos.
 */
public abstract class AbstractDocMojo extends AbstractMojo {
    /**
     * The Maven Settings.
     * @parameter default-value="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * Location of the directories that contain sources for this build.
     * @parameter expression="${source.trees}"
     */
    protected String[] sourceTrees;

    /**
     * @parameter default-value="${project.build.sourceDirectory}"
     */
    protected String sourceDirectory;

    /**
     * @parameter default-value="${project.build.directory}/generated-sources/groovy-stubs/main"
     */
    protected String groovyGeneratedStubs;

    /**
     * Location of the output directory.
     * @parameter expression="${output.directory}" default-value="${project.build.outputDirectory}"
     * @required
     */
    protected File outputDirectory;

    /**
     * Location of the build directory.
     * @parameter expression="${build.directory}" default-value="${project.build.directory}"
     * @required
     */
    protected File buildDirectory;

    /**
     * Location of API links.
     * @parameter
     * @required
     */
    protected Map<String, String> links;

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Enables a proxy server for http access if it is configured within the Maven settings.
     */
    protected void enableProxy() {
        if (settings == null || settings.getActiveProxy() == null) {
            return;
        }

        Proxy activeProxy = settings.getActiveProxy();
        String protocol = activeProxy.getProtocol().isEmpty() ? "" : activeProxy.getProtocol();

        if (defined(activeProxy.getHost())) {
            System.setProperty(protocol + ".proxyHost", activeProxy.getHost());
        }

        if (activeProxy.getPort() > 0) {
            System.setProperty(protocol + ".proxyPort", Integer.toString(activeProxy.getPort()));
        }

        if (defined(activeProxy.getNonProxyHosts())) {
            System.setProperty(protocol + ".nonProxyHosts", activeProxy.getNonProxyHosts());
        }

        if (defined(activeProxy.getUsername())) {
            System.setProperty(protocol + ".proxyUser", activeProxy.getUsername());

            if (defined(activeProxy.getPassword())) {
                System.setProperty(protocol + ".proxyPassword", activeProxy.getPassword());
            }
        }
    }

    /**
     * Creates a type info object with the api links provided by the configuration.
     * @return the type info object.
     */
    protected Types initTypes() {
        Map<String, String> apiLinks = new HashMap<String, String>();
        for (Map.Entry<String, String> link : links.entrySet()) {
            apiLinks.put(link.getKey() + ".", link.getValue() + "/");
        }
        return new Types(apiLinks);
    }

    private boolean defined(String value) {
        return value != null && !value.isEmpty();
    }
}
