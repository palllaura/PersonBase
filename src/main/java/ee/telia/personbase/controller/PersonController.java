package ee.telia.personbase.controller;

import ee.telia.personbase.dto.ValidationResult;
import ee.telia.personbase.entity.Person;
import ee.telia.personbase.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {
    private final PersonService personService;

    /**
     * Construct new PersonController.
     * @param personService service associated with controller.
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Get all people from the database.
     * @return list of people.
     */
    @GetMapping("/people")
    public List<Person> getAllPeople() {
        return personService.getAllPeople();
    }

    /**
     * Add a new person to the database.
     * @param person person to add.
     * @return validation result.
     */
    @PostMapping("/add")
    public ValidationResult addPerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    /**
     * Edit existing person.
     * @param person person to edit.
     * @return validation result.
     */
    @PostMapping("/edit")
    public ValidationResult editPerson(@RequestBody Person person) {
        return personService.editPerson(person);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deletePerson(@PathVariable Long id) {
        return personService.deletePersonById(id);
    }

}
