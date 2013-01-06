package de.richter.eclipse.fontresizer.handlers;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class FontSizingManager {

	public static void increaseFontSize() {
		final Font font = JFaceResources.getFontRegistry().get("org.eclipse.jdt.ui.editors.textfont");
		
		final FontData[] newFontData = font.getFontData();
		newFontData[0].setHeight(newFontData[0].getHeight() + 1);

		JFaceResources.getFontRegistry().put("org.eclipse.jdt.ui.editors.textfont", newFontData);
	}

	public static void decreaseFontSize() {
		final Font font = JFaceResources.getFontRegistry().get("org.eclipse.jdt.ui.editors.textfont");
		
		final FontData[] newFontData = font.getFontData();
		newFontData[0].setHeight(newFontData[0].getHeight() - 1);

		JFaceResources.getFontRegistry().put("org.eclipse.jdt.ui.editors.textfont", newFontData);
	}

	public static void resetFontSize() {
		Font defaultFont = JFaceResources.getTextFont();
		FontData[] data = defaultFont.getFontData();
		
		JFaceResources.getFontRegistry().put("org.eclipse.jdt.ui.editors.textfont", data);
	}
}
