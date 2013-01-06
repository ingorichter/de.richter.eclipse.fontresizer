/*
 * Copyright 2011 Ingo Richter
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
 *
 */
package de.richter.eclipse.fontresizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class FontSizerPlugin extends Plugin {

	public static final String PLUGIN_ID = "de.richter.eclipse.fontresizer";
	private static BundleContext context;
	private static FontSizerPlugin fontResizerPlugin;

	public FontSizerPlugin() {
		fontResizerPlugin = this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);

		FontSizerPlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		FontSizerPlugin.context = null;

		super.stop(bundleContext);
	}

	static BundleContext getContext() {
		return context;
	}

	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getPluginId(), FontSizerPluginConstants.INTERNAL_ERROR,
				message, null));
	}

	public static void info(String message) {
		log(new Status(IStatus.INFO, getPluginId(), FontSizerPluginConstants.INFO, message, null));
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	private static Plugin getDefault() {
		return fontResizerPlugin;
	}

	private static String getPluginId() {
		return PLUGIN_ID;
	}
}
