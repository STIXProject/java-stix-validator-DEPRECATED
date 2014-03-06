package org.mitre.stix.validator;

import org.xml.sax.SAXParseException;

// A lot of boilerplate for what's a simple structure to hold the category, line, column, and message
// for an error.
public class SchemaError {
    private Enum<Categories> category;
    private Integer line;
    private Integer column;
    private String message;

    public SchemaError(Integer line, Integer column, String message, Enum<Categories> category) {
        this.line = line;
        this.column = column;
        this.message = message;
        this.category = category;
    }

    // Create an instance of this class from a SAXParseException and the category
    public static SchemaError fromException(SAXParseException exception, Enum<Categories> category) {
        return new SchemaError(exception.getLineNumber(), exception.getColumnNumber(), exception.getMessage(), category);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Enum<Categories> getCategory() {
        return category;
    }

    public void setCategory(Enum<Categories> category) {
        this.category = category;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public enum Categories {
        FATAL_ERROR("Fatal Error"),
        ERROR("Error"),
        WARNING("Warning"),
        SUGGESTION("Suggestion"),
        NOTICE("Notice");

        private final String text;
        private Categories(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
