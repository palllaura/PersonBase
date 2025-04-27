package ee.telia.personbase.service;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PersonValidator {

    private final PersonRepository personRepository;

    public PersonValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Validates all the input fields for a new Person.
     * @param person Person to validate.
     * @return validation result.
     */
    public ValidationResult validate(Person person) {
        ValidationResult result = new ValidationResult();

        normalizePerson(person);

        if (checkForPossibleDuplicate(person)) {
            result.addError("Person with same first name, last name and birthdate already exists.");
        }

        if (isBlank(person.getFirstName())) {
            result.addError("First name is required");
        }

        if (isBlank(person.getLastName())) {
            result.addError("Last name is required");
        }

        if (person.getBirthDate() == null || person.getBirthDate().isAfter(LocalDate.now())) {
            result.addError("Birth date is missing or invalid");
        }

        if (!isValidEmail(person.getEmail())) {
            result.addError("Email address is invalid");
        }

        if (!isValidPhoneNumber(person.getPhoneNumber())) {
            result.addError("Phone number is invalid");
        }

        return result;
    }

    private void normalizePerson(Person person) {
        person.setFirstName(normalizeName(person.getFirstName()));
        person.setLastName(normalizeName(person.getLastName()));
        person.setEmail(normalizeEmail(person.getEmail()));
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim().replaceAll("\\s+", " ");
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    /**
     * Check if there is a person with same name and birthdate in database.
     * @param person person whose name and birthdate to check.
     * @return true if possible duplicate was found, else false.
     */
    private boolean checkForPossibleDuplicate(Person person) {
        return personRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(
                person.getFirstName(), person.getLastName(), person.getBirthDate()
        );
    }

    /**
     * Checks if the given field is either null or blank.
     * @param value string to check.
     * @return true if value is null or blank, else false.
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
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
