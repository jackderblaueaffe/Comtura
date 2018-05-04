package api.errors;

import java.util.List;

public class ValidationErrorMessages {
    private List<String> validationErrorMessages;


    public List<String> getErrorMessages() {
        return validationErrorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.validationErrorMessages = errorMessages;
    }

    public ValidationErrorMessages(List<String> errorMessages) {
        this.validationErrorMessages = errorMessages;
    }
}