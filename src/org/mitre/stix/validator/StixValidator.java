package org.mitre.stix.validator;

import org.mitre.stix.validator.sp.SuggestedPracticeValidator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StixValidator {

    // This is Java, so you need a factory to create schemas
    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // The actual schema instance to validate against
    private Schema schema;

    // The list of best practice validators
    private List<SuggestedPracticeValidator> suggestedPracticeValidators = new ArrayList<SuggestedPracticeValidator>();

    // Create a new validator for a given STIX version
    public StixValidator(String version) throws SAXException {
        // Loads the correct set of schemas based on the STIX version
        schema = schemaFactory.newSchema(getClass().getResource("/schemas/" + version + "/uber_schema.xsd"));
    }

    public List<SchemaError> validate(File file) throws IOException, SAXException {
        return validate(new StreamSource(file));
    }

    public List<SchemaError> validate(String string) throws IOException, SAXException {
        return validate(new StreamSource(string));
    }

    private List<SchemaError> validate(Source source) throws SAXException, IOException {
        Validator validator = schema.newValidator();

        // Create and set the error handler. When the validator encounters errors, it calls
        // the methods on the error handler.
        StixErrorHandler errorHandler = new StixValidator.StixErrorHandler();
        validator.setErrorHandler(errorHandler);

        // See the catch block comment for why this needs to be wrapped in a try
        try {
            validator.validate(source);
        } catch(SAXParseException e) {
            // For some reason the validator will throw a parse exception for some errors
            // in addition to adding them to the error handler. Since the error is already
            // added to the error handler, just ignore it here.
        }

        // Return the list of errors. No errors = valid
        return errorHandler.getErrors();
    }

    // Implements the SAX error handler interface, just adds errors to a list
    public class StixErrorHandler implements ErrorHandler {

        List<SchemaError> errors = new ArrayList<SchemaError>();

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            errors.add(SchemaError.fromException(exception, SchemaError.Categories.WARNING));
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errors.add(SchemaError.fromException(exception, SchemaError.Categories.ERROR));
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            errors.add(SchemaError.fromException(exception, SchemaError.Categories.FATAL_ERROR));
        }

        public List<SchemaError> getErrors() {
            return errors;
        }
    }
}
