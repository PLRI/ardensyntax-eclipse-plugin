/*
 * generated by Xtext 2.10.0
 */
package arden.plugin.editor.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import arden.plugin.editor.ui.folding.ArdenSyntaxFoldingRegionProvider;
import arden.plugin.editor.ui.syntaxcoloring.ArdenSyntaxHighlightingConfiguration;
import arden.plugin.editor.ui.syntaxcoloring.ArdenSyntaxSemanticHighlightingCalculator;

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
public class ArdenSyntaxUiModule extends AbstractArdenSyntaxUiModule {

	public ArdenSyntaxUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
    public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
        return ArdenSyntaxSemanticHighlightingCalculator.class;
    }

    public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
        return ArdenSyntaxHighlightingConfiguration.class;
    }

    public Class<? extends IFoldingRegionProvider> bindIFoldingRegionProvider() {
        return ArdenSyntaxFoldingRegionProvider.class;
    }
}