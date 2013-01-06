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
package de.richter.eclipse.fontresizer.startup;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.richter.eclipse.fontresizer.FontSizerPlugin;
import de.richter.eclipse.fontresizer.handlers.FontSizingManager;

public class FontResizerStartup implements IStartup {
	private IPartListener partListener;
	private MouseWheelListener mouseWheelListener;

	@Override
	public void earlyStartup() {
		partListener = createPartListener();
		mouseWheelListener = createMouseWheelListener();

		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				FontSizerPlugin.info("Workbench Windows: " + workbenchWindows.length);
				for (IWorkbenchWindow iWorkbenchWindow : workbenchWindows) {
					IWorkbenchPage[] pages = iWorkbenchWindow.getPages();
					FontSizerPlugin.info("" + pages);

					for (IWorkbenchPage iWorkbenchPage : pages) {
						IViewReference[] viewReferences = iWorkbenchPage.getViewReferences();
						IEditorReference[] editorReferences = iWorkbenchPage.getEditorReferences();

						for (IEditorReference iEditorReference : editorReferences) {
							IWorkbenchPart workbenchPart = iEditorReference.getPart(false);
							StyledText textWidgetFromEditor = getTextWidgetFromEditor(iEditorReference
									.getPart(false));
							if (textWidgetFromEditor != null) {
								textWidgetFromEditor.addMouseWheelListener(mouseWheelListener);
							}
						}
					}
				}
			}
		});

		IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().
		  getConfigurationElementsFor("org.eclipse.ui.editors");

		for (IConfigurationElement iConfigurationElement : configurationElementsFor) {
			String[] attributeNames = iConfigurationElement.getAttributeNames();

			for (String attributeName : attributeNames) {
				System.err.println(attributeName + " : "
						+ iConfigurationElement.getAttribute(attributeName));
			}
		}

		PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
			@Override
			public void windowOpened(IWorkbenchWindow window) {
				window.getPartService().addPartListener(partListener);
			}

			@Override
			public void windowDeactivated(IWorkbenchWindow window) {
				window.getPartService().removePartListener(partListener);
			}

			@Override
			public void windowClosed(IWorkbenchWindow window) {
				window.getPartService().removePartListener(partListener);
			}

			@Override
			public void windowActivated(IWorkbenchWindow window) {
				window.getPartService().addPartListener(partListener);
			}
		});
	}

	private IPartListener createPartListener() {
		return new IPartListener() {
			@Override
			public void partOpened(IWorkbenchPart part) {
				FontSizerPlugin.info(String.format("Part opened %s", part.toString()));
				attachMouseWheelListenerToTextWidget(part);
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				FontSizerPlugin.info(String.format("Part deactivated %s", part.toString()));
				removeMouseWheelListenerFromTextWidget(part);
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				FontSizerPlugin.info(String.format("Part closed %s", part.toString()));
				removeMouseWheelListenerFromTextWidget(part);
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				FontSizerPlugin.info(String.format("Part to top %s", part.toString()));
			}

			@Override
			public void partActivated(IWorkbenchPart part) {
				FontSizerPlugin.info(String.format("Part activated %s", part.toString()));

				attachMouseWheelListenerToTextWidget(part);
			}

			private void removeMouseWheelListenerFromTextWidget(IWorkbenchPart part) {
				StyledText textWidget = getTextWidgetFromEditor(part);
				if (textWidget != null) {
					textWidget.removeMouseWheelListener(mouseWheelListener);
				}
			}

			private void attachMouseWheelListenerToTextWidget(IWorkbenchPart part) {
				StyledText textWidget = getTextWidgetFromEditor(part);
				if (textWidget != null) {
					textWidget.addMouseWheelListener(mouseWheelListener);
				}
			}

//			private StyledText getTextWidgetFromEditor(IWorkbenchPart part) {
//				StyledText textWidget = null;
//				if (part instanceof JavaEditor) {
//					ISourceViewer sourceViewer = ((JavaEditor) part).getViewer();
//					textWidget = sourceViewer.getTextWidget();
//				}
//
//				FontSizerPlugin.info(part.getClass().getName());
//
//				return textWidget;
//			}
		};
	}

	private MouseWheelListener createMouseWheelListener() {
		return new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				if ((e.stateMask & SWT.ALT) == SWT.ALT) {
					FontSizerPlugin.info("Mouse Wheel scrolled and ALT Key pressed");

					if (e.count > 0) {
						FontSizingManager.increaseFontSize();
					} else {
						FontSizingManager.decreaseFontSize();
					}
				}
			}
		};
	}

	private StyledText getTextWidgetFromEditor(IWorkbenchPart part) {
		StyledText textWidget = null;

		// This is the way to go! There will be an Adapter to access the underlying TextWidget.
		// 1) What happens if the Editor is not a text editor?
		// 2) How can I determine the kind of editor to change the correct text properties (Text, Source, etc)?
		if (part != null) {
			textWidget = (StyledText) part.getAdapter(Control.class);
			FontSizerPlugin.info("Part class " + part.getClass().getName());
		}

		return textWidget;
//		if (part instanceof JavaEditor) {
////		if (part instanceof AbstractTextEditor)
//			ISourceViewer sourceViewer = ((JavaEditor) part).getViewer();
//			textWidget = sourceViewer.getTextWidget();
//		}
//
//		return textWidget;
	}

}
