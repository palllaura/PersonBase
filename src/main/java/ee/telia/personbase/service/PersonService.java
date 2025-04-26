package ee.telia.personbase.service;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
     * Get all people in database.
     * @return people in list.
     */
    public List<Person> getAllPeople() {
        return (List<Person>) personRepository.findAll();
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
     * Delete person from database.
     * @param id ID of person to delete.
     * @return true if person was deleted, false if not found or error.
     */
    public boolean deletePersonById(Long id) {
        try {
            personRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (Exception e) {
            LOGGER.warning("Unexpected error while deleting person: " + e.getMessage());
            return false;
        }
    }

    /**
     * Edit existing person if validated.
     * @param person Person to edit.
     * @return validation result.
     */
    public ValidationResult editPerson(Person person) {
        ValidationResult result = validatePerson(person);

        if (!personRepository.existsById(person.getId())) {
            result.addError("No person with given id found");
            return result;
        }

        if (result.isValid()) {
            personRepository.save(person);
            String message = String.format("Successfully edited person: %s %s", person.getFirstName(), person.getLastName());
            LOGGER.info(message);
        } else {
            String message = String.format("Failed to edit person: %s %s", person.getFirstName(), person.getLastName());
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

        if (person.getBirthDate() == null || person.getBirthDate().isAfter(LocalDate.now())) {
            String message = "Birth date is missing or invalid";
            LOGGER.warning(message);
            result.addError(message);
        }

        if (!isValidEmail(person.getEmail())) {
            String message = "Email address is invalid";
            LOGGER.warning(message);
            result.addError(message);
        }

        if (!isValidPhoneNumber(person.getPhoneNumber())) {
            String message = "Phone number is invalid";
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
        return phoneNumber != null && phoneNumber.matches("^\\d{5,12}$");
    }

}
