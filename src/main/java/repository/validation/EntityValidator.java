package repository.validation;


import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class EntityValidator {

    private Validator validator;

    public EntityValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public List<String> validate(Object object) {

        Set<ConstraintViolation<Object>> errors = validator.validate(object);

        List<String> fehler = new ArrayList<>();

        for(ConstraintViolation<Object> violation : errors) {
            fehler.add(violation.getMessage());
        }

        return fehler;
    }


    public List<String> createErrorListByConstraintViolationException(ConstraintViolationException c) {
        List<String> fehler = new ArrayList<>();
        for (Iterator<ConstraintViolation<?>> iter = c.getConstraintViolations().iterator(); iter.hasNext(); ) {
            fehler.add(iter.next().getMessage());
        }
        return fehler;
    }
}
