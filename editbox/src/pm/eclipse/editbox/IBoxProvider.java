package pm.eclipse.editbox;

import java.util.Collection;

import org.eclipse.ui.IWorkbenchPart;


public interface IBoxProvider {
	
	String getId();
	String getName();

	boolean supports(IWorkbenchPart editorPart);
	IBoxDecorator decorate(IWorkbenchPart editorPart);
	void releaseDecorator(IBoxDecorator decorator);

	IBoxSettings getEditorsBoxSettings();
	IBoxSettingsStore getSettingsStore();

	IBoxSettings createSettings();
	IBoxDecorator createDecorator();
	Collection<String> getBuilders();
	IBoxBuilder createBoxBuilder(String name);

}
