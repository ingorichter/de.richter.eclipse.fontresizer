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
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class IncreaseEditorFontSizeHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public IncreaseEditorFontSizeHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Font font = JFaceResources.getFontRegistry().get("org.eclipse.jdt.ui.editors.textfont");
		
		final FontData[] newFontData = font.getFontData();
		newFontData[0].setHeight(newFontData[0].getHeight() + 1);

		JFaceResources.getFontRegistry().put("org.eclipse.jdt.ui.editors.textfont", newFontData);

		return null;
	}
}
