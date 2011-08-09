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
package de.richter.eclipse.fontresizer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class DecreaseEditorFontSizeHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public DecreaseEditorFontSizeHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEclipsePreferences node = InstanceScope.INSTANCE
				.getNode("org.eclipse.ui.workbench");
		String string = node.get("org.eclipse.jdt.ui.editors.textfont", "");

		String[] segments = string.split("\\|");
		float fontSize = Float.parseFloat(segments[2]);
		fontSize -= 1.0;

		String newFontSize = String.format("%s|%s|%.1f|%s|%s|%s|%s",
				segments[0], segments[1], fontSize, segments[3], segments[4],
				segments[5], segments[6]);

		node.put("org.eclipse.jdt.ui.editors.textfont", newFontSize);
		// "1|Monaco|48.0|0|COCOA|1|Monaco");

		return null;
	}
}
