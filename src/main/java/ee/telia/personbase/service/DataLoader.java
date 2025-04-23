package ee.telia.personbase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.telia.personbase.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PersonService personService;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        File file = new File("src/main/resources/data/people.json");
        List<Person> people = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
        for (Person person : people) {
            personService.savePerson(person);
        }
    }
}
