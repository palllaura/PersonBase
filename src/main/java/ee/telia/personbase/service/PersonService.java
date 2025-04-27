package ee.telia.personbase.service;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(PersonService.class.getName());
    private final PersonRepository personRepository;
    private final PersonValidator personValidator;


    /**
     * Construct new PersonService.
     * @param personRepository PersonRepository associated with the service.
     * @param personValidator PersonValidator associated with the service.
     */
    public PersonService(PersonRepository personRepository, PersonValidator personValidator) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
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
        ValidationResult result = personValidator.validate(person);
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
        if (!personRepository.existsById(person.getId())) {
            ValidationResult result = new ValidationResult();
            result.addError("No person with given id found");
            return result;
        }

        ValidationResult result = personValidator.validate(person);

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
}
