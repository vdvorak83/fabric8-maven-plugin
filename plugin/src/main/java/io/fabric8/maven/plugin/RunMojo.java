/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.fabric8.maven.plugin;

import io.fabric8.kubernetes.api.Controller;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.Set;

import static io.fabric8.kubernetes.api.KubernetesHelper.getName;

/**
 * This goal forks the install goal then applies the generated kubernetes resources to the current cluster,
 * waits for the new pod to start and tails it to the console.
 * Pressing <code>Ctrl+C</code> will then terminate the application.
 * <p>
 * You can think of this goal as like a combination of `fabric8:deploy`, `fabric8:logs` then `fabric8:undeploy` once you hit
 * <code>Ctrl+C</code>
 * <p>
 * Note that the goals fabric8:resource and fabric8:build must be bound to the proper execution phases.
 */
@Mojo(name = "run", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.VALIDATE)
@Execute(phase = LifecyclePhase.INSTALL)
public class RunMojo extends AbstractTailLogMojo {

    @Parameter(property = "fabric8.deleteOnExit", defaultValue = "true")
    private boolean deleteOnExit;

    @Override
    protected void applyEntities(Controller controller, final KubernetesClient kubernetes, final String namespace, String fileName, final Set<HasMetadata> entities) throws Exception {
        super.applyEntities(controller, kubernetes, namespace, fileName, entities);

        tailAppPodsLogs(kubernetes, namespace, entities, true, this.deleteOnExit, true);
    }


}
