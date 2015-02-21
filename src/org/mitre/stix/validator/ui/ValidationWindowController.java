/*
 * Copyright (c) 2015 – The MITRE Corporation
 * All rights reserved. See LICENSE.txt for complete terms.
 */
package org.mitre.stix.validator.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.mitre.stix.validator.SchemaError;
import org.mitre.stix.validator.StixValidator;
import org.xml.sax.SAXException;

import javax.annotation.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ValidationWindowController implements Initializable{

    public Button revalidateButton;
    public Label filenameLabel;
    public TableView validationResults;
    public TableColumn line;
    public TableColumn column;
    public TableColumn message;
    public TableColumn category;
    public Label validationSummary;
    public ComboBox<String> stixVersion;

    final FileChooser fileChooser = new FileChooser();
    Map<String, StixValidator> validators;
    String filename = null;
    ObservableList<BoundSchemaError> boundErrors;

    Color vSuccess = Color.GREEN;
    Color vFailure = Color.RED;
    Color vMessage = Color.BLACK;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure the file Chooser
        fileChooser.setTitle("Choose STIX File");

        // Filter for only XML files
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Set the chooser to go to the home directory
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if(userDirectory.canRead()) {
            fileChooser.setInitialDirectory(userDirectory);
        }

        // Create the validator
        try {
            validators = new HashMap<String, StixValidator>();
            validators.put("1.1", new StixValidator("1.1"));
        } catch (SAXException e) {
            filenameLabel.setText("Error building parser: " + e.getMessage());
            e.printStackTrace();
        }

        // Bind the table columns
        line.setCellValueFactory(new PropertyValueFactory<BoundSchemaError,Integer>("line"));
        column.setCellValueFactory(new PropertyValueFactory<BoundSchemaError,Integer>("column"));
        category.setCellValueFactory(new PropertyValueFactory<BoundSchemaError,String>("category"));
        message.setCellValueFactory(new PropertyValueFactory<BoundSchemaError,String>("message"));

        // Finally, bind the table
        boundErrors = FXCollections.observableList(new ArrayList<BoundSchemaError>());
        validationResults.setItems(boundErrors);
    }

    public void handleSelectFile(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        filename = file.getAbsolutePath();

        filenameLabel.setText(filename);
        revalidateButton.setDisable(false);

        validate(filename);
    }

    public void handleRevalidate(ActionEvent actionEvent) {
        validate(filename);
    }

    public void validate(String filename) {
        validationSummary.setText("Validating...");
        validationSummary.setTextFill(vMessage);
        try {
            File file = new File(filename);
            String version = stixVersion.getValue().split(" ")[1];

            if(validators.get(version) == null) {
                validators.put(version, new StixValidator(version));
            }
            List<SchemaError> errors = validators.get(version).validate(file);

            // Empty the observable list, then add the new errors
            boundErrors.remove(0, boundErrors.size());

            for(SchemaError error : errors) {
                boundErrors.add(new BoundSchemaError(error));
            }

            if(errors.size() == 0) {
                validationSummary.setText("STIX " + version + " validation successful: no errors found");
                validationSummary.setTextFill(vSuccess);
            } else {
                validationSummary.setText("STIX " + version + " validation failed: " + errors.size() + " errors found");
                validationSummary.setTextFill(vFailure);
            }

        } catch (SAXException e) {
            filenameLabel.setText("Error validating file: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            filenameLabel.setText("Error loading file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public class BoundSchemaError {
        public SimpleIntegerProperty line = new SimpleIntegerProperty();
        public SimpleIntegerProperty column = new SimpleIntegerProperty();
        public SimpleStringProperty category = new SimpleStringProperty();
        public SimpleStringProperty message = new SimpleStringProperty();

        public SimpleIntegerProperty lineProperty() {
            return line;
        }

        public SimpleIntegerProperty columnProperty() {
            return column;
        }

        public SimpleStringProperty categoryProperty() {
            return category;
        }

        public SimpleStringProperty messageProperty() {
            return message;
        }

        public BoundSchemaError(SchemaError error) {
            this.category = new SimpleStringProperty(error.getCategory().toString());
            this.message = new SimpleStringProperty(error.getMessage().toString());
            this.line = new SimpleIntegerProperty(error.getLine());
            this.column = new SimpleIntegerProperty(error.getColumn());
        }
    }
}
