package ee.telia.personbase.repository;

import ee.telia.personbase.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(
            String firstName,
            String lastName,
            LocalDate birthDate
    );

}
