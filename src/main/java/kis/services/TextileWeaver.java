package kis.services;

import kis.pages.knowledge.facts.FactsDisplay;

import java.io.StringWriter;

import net.java.textilej.parser.builder.HtmlDocumentBuilder;
import net.java.textilej.parser.MarkupParser;
import net.java.textilej.parser.markup.textile.TextileDialect;

/**
 * инкапсулирует операции с textile-j
 */
public class TextileWeaver {
    public String weave(String text) {
        StringWriter writer = new StringWriter();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
        builder.setEmitAsDocument(false);
        MarkupParser parser = new MarkupParser(new TextileDialect());
        parser.setBuilder(builder);
        parser.parse(text);

        return writer.getBuffer().toString();
    }
}