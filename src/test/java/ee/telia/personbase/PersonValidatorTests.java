package ee.telia.personbase;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.InternetSpeed;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import ee.telia.personbase.service.PersonValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonValidatorTests {
    private PersonRepository repository;
    private PersonValidator validator;
    private Person person;
    private ValidationResult result;

    @BeforeEach
    void setup() {
        repository = mock(PersonRepository.class);
        validator = new PersonValidator(repository);
        person = createTestPerson();
    }

    /**
     * Helper method to create a correct person.
     * @return person.
     */
    Person createTestPerson() {
        person = new Person();
        person.setFirstName("Toomas");
        person.setLastName("Mets");
        person.setEmail("toomas@mets.ee");
        person.setPhoneNumber("56666666");
        person.setBirthDate(LocalDate.of(1980, 2, 13));
        person.setInternetSpeedMbps(InternetSpeed.MBPS400);
        return person;
    }

    @Test
    void validationSuccessful() {
        result = validator.validate(person);
        Assertions.assertTrue(result.isValid());
    }

    @Test
    void validationFailsIfPersonWithSameNameAndBirthdateExists() {
        when(repository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDateAndIdNot(
                "Toomas", "Mets", LocalDate.of(1980, 2, 13), person.getId()
        )).thenReturn(true);

        result = validator.validate(person);
        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains(
                "Person with same first name, last name and birthdate already exists."
        ));

    }

    @Test
    void validationFailsNameIsOnlyWhitespace() {
        person.setFirstName("     ");
        result = validator.validate(person);
        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("First name is required"));
    }

    @Test
    void validationFailsBirthDateIsInTheFuture() {
        person.setBirthDate(LocalDate.now().plusDays(1));
        result = validator.validate(person);
        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("Birth date is missing or invalid"));
    }

    @Test
    void validationFailsEmailIsInvalid() {
        person.setEmail("toomas2gmail.com");
        result = validator.validate(person);

        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("Email address is invalid"));
    }

    @Test
    void validationFailsInvalidPhoneNumber() {
        person.setPhoneNumber("112");
        result = validator.validate(person);

        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("Phone number is invalid"));
    }

    @Test
    void validationFailsInvalidInternetSpeed() {
        person.setInternetSpeedMbps(InternetSpeed.NONE);
        result = validator.validate(person);

        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("Internet speed is invalid"));

    }
}
