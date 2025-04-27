package ee.telia.personbase;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.InternetSpeed;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import ee.telia.personbase.service.PersonService;
import ee.telia.personbase.service.PersonValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class PersonServiceTests {

    private PersonService service;
    private PersonRepository repository;
    private PersonValidator validator;
    private ValidationResult result;
    private ValidationResult expectedResult;
    private Person person;

    @BeforeEach
    void setup() {
        repository = mock(PersonRepository.class);
        validator = mock(PersonValidator.class);
        service = new PersonService(repository, validator);
        result = new ValidationResult();
        expectedResult = new ValidationResult();
    }

    /**
     * Helper method to create a correct person.
     *
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
    void getAllPeopleReturnsAllPeople() {
        when(repository.findAll()).thenReturn(List.of(createTestPerson(), createTestPerson()));
        List<Person> people = service.getAllPeople();
        Assertions.assertEquals(2, people.size());
    }

    @Test
    void saveValidPersonReturnsOK() {
        person = createTestPerson();
        when(validator.validate(person)).thenReturn(expectedResult);
        when(repository.save(person)).thenReturn(person);
        result = service.savePerson(person);
        Assertions.assertTrue(result.isValid());
        Assertions.assertEquals(0, result.getMessages().size());
    }

    @Test
    void saveInvalidPersonReturnsValidationErrors() {
        person = createTestPerson();
        expectedResult.addError("Missing first name");
        when(validator.validate(person)).thenReturn(expectedResult);
        result = service.savePerson(person);
        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Missing first name", result.getMessages().get(0));
    }

    @Test
    void deletePersonByIdWithExistingPersonReturnsOK() {
        doNothing().when(repository).deleteById(1L);
        Assertions.assertTrue(service.deletePersonById(1L));
    }

    @Test
    void deletePersonByIdWithNonExistingPersonFails() {
        doThrow(new IllegalArgumentException()).when(repository).deleteById(1L);
        Assertions.assertFalse(service.deletePersonById(1L));
    }

    @Test
    void editNonExistingPersonFails() {
        person = createTestPerson();
        when(repository.existsById(1L)).thenReturn(false);
        result = service.editPerson(person);
        Assertions.assertFalse(result.isValid());
    }

    @Test
    void editExistingPersonWithValidDataReturnsOK() {
        person = createTestPerson();
        person.setId(1L);
        when(validator.validate(person)).thenReturn(expectedResult);
        when(repository.existsById(1L)).thenReturn(true);
        result = service.editPerson(person);
        Assertions.assertTrue(result.isValid());
    }

    @Test
    void editExistingPersonWithInvalidDataFails() {
        person = createTestPerson();
        person.setId(1L);
        expectedResult.addError("Missing first name");
        when(validator.validate(person)).thenReturn(expectedResult);
        when(repository.existsById(1L)).thenReturn(true);
        result = service.editPerson(person);
        Assertions.assertFalse(result.isValid());
    }
}
