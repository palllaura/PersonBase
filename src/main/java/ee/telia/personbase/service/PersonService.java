package ee.telia.personbase.service;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class PersonService {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(PersonService.class.getName());
    private final PersonRepository personRepository;

    /**
     * Construct new PersonService.
     * @param personRepository PersonRepository associated with the service.
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Save new person to database if validated.
     * @param person Person to save.
     * @return validation result.
     */
    public ValidationResult savePerson(Person person) {
        ValidationResult result = validatePerson(person);
        if (result.isValid()) {
            personRepository.save(person);
            String message = String.format("Saved new person: %s %s", person.getFirstName(), person.getLastName());
            LOGGER.info(message);
        } else {
            String message = String.format("Failed to save person: %s %s", person.getFirstName(), person.getLastName());
            LOGGER.warning(message);
        }
        return result;
    }

    /**
     * Validates all the input fields for a new Person.
     * @param person Person to validate.
     * @return validation result.
     */
    private ValidationResult validatePerson(Person person) {
        ValidationResult result = new ValidationResult();

        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            String message = "First name is required";
            LOGGER.warning(message);
            result.addError(message);
        }

        if (person.getLastName() == null || person.getLastName().isBlank()) {
            String message = "Last name is required";
            LOGGER.warning(message);
            result.addError(message);
        }

        if (person.getBirthDate() != null && person.getBirthDate().isAfter(LocalDate.now())) {
            String message = String.format("Birth date cannot be in the future: %s", person.getBirthDate());
            LOGGER.warning(message);
            result.addError(message);
        }

        if (!isValidEmail(person.getEmail())) {
            String message = String.format("Invalid email address: %s", person.getEmail());
            LOGGER.warning(message);
            result.addError(message);
        }

        if (!isValidPhoneNumber(person.getPhoneNumber())) {
            String message = String.format("Invalid phone number: %s", person.getPhoneNumber());
            LOGGER.warning(message);
            result.addError(message);
        }
        return result;
    }

    /**
     * Validates if email field is correct.
     * @param email email to validate.
     * @return true if email is correct, else false.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Validates if phone number is correct.
     * @param phoneNumber phone number to validate.
     * @return true if correct, else false.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\"^\\\\+?\\\\d{1,4}[- ]?\\\\d{6,10}$\"\n");
    }

}
