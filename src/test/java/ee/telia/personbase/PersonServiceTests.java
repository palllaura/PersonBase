package ee.telia.personbase;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.repository.PersonRepository;
import ee.telia.personbase.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceTests {
    PersonRepository repository;
    PersonService service;

    @BeforeEach
    void createTestData() {
        repository = mock(PersonRepository.class);
        service = new PersonService(repository);
    }

    @Test
    void saveWithInvalidPhoneNumberFails() {
        Person person = mock(Person.class);

        when(person.getFirstName()).thenReturn("Toomas");
        when(person.getLastName()).thenReturn("Mets");
        when(person.getEmail()).thenReturn("toomas@mets.ee");
        when(person.getPhoneNumber()).thenReturn("112");
        when(person.getBirthDate()).thenReturn(LocalDate.of(1980, 2, 13));
        ValidationResult result = service.savePerson(person);

        Assertions.assertFalse(result.isValid());
        Assertions.assertTrue(result.getMessages().contains("Invalid phone number: 112"));
    }
}
