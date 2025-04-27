package ee.telia.personbase;

import ee.telia.personbase.controller.PersonController;
import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.InternetSpeed;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonControllerTests {

    private PersonController controller;
    private PersonService service;
    private ValidationResult result;
    private ValidationResult expectedResult;
    private Person person;

    @BeforeEach
    void setup() {
        service = mock(PersonService.class);
        controller = new PersonController(service);
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
        Person person1 = createTestPerson();
        person1.setId(1L);

        Person person2 = createTestPerson();
        person2.setId(2L);

        when(service.getAllPeople()).thenReturn(List.of(person1, person2));
        List<Person> people = controller.getAllPeople();

        Assertions.assertEquals(1L, people.get(0).getId());
        Assertions.assertEquals(2L, people.get(1).getId());
    }

    @Test
    void addValidPersonReturnsOK() {
        person = createTestPerson();
        when(service.savePerson(person)).thenReturn(expectedResult);
        result = controller.addPerson(person);
        Assertions.assertTrue(result.isValid());
        Assertions.assertEquals(0, result.getMessages().size());
    }

    @Test
    void addInvalidPersonReturnsValidationErrors() {
        person = createTestPerson();
        expectedResult.addError("Validation error");
        when(service.savePerson(person)).thenReturn(expectedResult);

        result = controller.addPerson(person);

        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals("Validation error", result.getMessages().get(0));
    }

    @Test
    void editPersonWithValidDataReturnsOK() {
        person = createTestPerson();
        when(service.editPerson(person)).thenReturn(expectedResult);

        result = controller.editPerson(person);
        Assertions.assertTrue(result.isValid());
        Assertions.assertEquals(0, result.getMessages().size());
    }

    @Test
    void deleteExistingPersonReturnsOK() {
        when(service.deletePersonById(1L)).thenReturn(true);
        Assertions.assertTrue(controller.deletePerson(1L));
    }

    @Test
    void deleteNonExistingPersonReturnsFailure() {
        Assertions.assertFalse(controller.deletePerson(2L));
    }
}
