/*
 * generated by Xtext
 */
package arden.xtext.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import arden.xtext.ui.folding.ArdenSyntaxFoldingRegionProvider;
import arden.xtext.ui.syntaxcoloring.ArdenSyntaxHighlightingConfiguration;
import arden.xtext.ui.syntaxcoloring.ArdenSyntaxSemanticHighlightingCalculator;

/**
 * Use this class to register components to be used within the IDE.
 */
public class ArdenSyntaxUiModule extends arden.xtext.ui.AbstractArdenSyntaxUiModule {
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
