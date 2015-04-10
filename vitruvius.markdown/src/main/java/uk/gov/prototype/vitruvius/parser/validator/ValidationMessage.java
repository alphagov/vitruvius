package uk.gov.prototype.vitruvius.parser.validator;

import java.util.List;

public class ValidationMessage {

    private String message;
    private ValidationType type;


    public ValidationMessage() {

    }
    public ValidationMessage(String message, ValidationType type) {
        this.message = message;
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public ValidationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ValidationMessage{" +
                "message='" + message + '\'' +
                ", type=" + type +
                '}';
    }

    public enum ValidationType {
        ERROR,
        WARNING
    }

    public static ValidationMessage createErrorMessage(String message) {
        return new ValidationMessage(message, ValidationType.ERROR);
    }

    public static ValidationMessage createWarning(String message) {
        return new ValidationMessage(message, ValidationType.WARNING);
    }

    public static boolean hasErrors(List<ValidationMessage> messages) {
        for (ValidationMessage validationMessage : messages) {
            if (validationMessage.getType() == ValidationType.ERROR) {
                return true;
            }
        }
        return false;
    }
}
