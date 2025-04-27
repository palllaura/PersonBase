package ee.telia.personbase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.telia.personbase.entity.InternetSpeed;
import ee.telia.personbase.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonService personService;

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(DataLoader.class.getName());

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        File file = new File("src/main/resources/data/people.json");
        List<Person> people = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));

        for (Person person : people) {
            try {
                if (person.getInternetSpeedMbps() == InternetSpeed.NONE) {
                    String message = String.format("Skipping person with invalid internet speed: %s %s",
                            person.getFirstName(), person.getLastName());
                    LOGGER.warning(message);
                    continue;
                }
                personService.savePerson(person);
            } catch (Exception e) {
                LOGGER.warning("Error processing person: " + person);
            }
        }
    }
}
