# Java STIX Validator (BETA)
A Java library for validating STIX documents, with a simple JavaFX frontend.

## Dependencies
Because the Java STIX Validator uses JavaFX, you'll need Java 7 in order to run it. There are no other dependencies.

## Getting Started
1. Download a pre-built JAR from the [releases page]( https://github.com/STIXProject/java-stix-validator/releases).
1. Double-click on the downloaded file to run it (On Windows and Mac, with Java 7 installed).
1. Choose which version of STIX you want to validate against.
1. Select a file to be validated.

## What checks are performed?
Currently, the validator only checks for conformance with the selected version of the STIX schemas. 

Though it's on the roadmap, the current version does not yet check for compliance with non-schema validation requirements, suggested practices, or style guidance.

## Development
If you want to develop against the Java STIX Validator, simply import the code into your IDE as a JavaFX application. The
`src` folder should be added as a source folder and the `resources` folder as a resources folder.

The main class for the UI is `org.mitre.stix.validator.Main`.

## Terms
BY USING THE STIX DOCUMENT VALIDATOR, YOU SIGNIFY YOUR ACCEPTANCE OF THE
TERMS AND CONDITIONS OF USE.  IF YOU DO NOT AGREE TO THESE TERMS, DO NOT USE
THE STIX DOCUMENT VALIDATOR.

For more information, please refer to the LICENSE.txt file
