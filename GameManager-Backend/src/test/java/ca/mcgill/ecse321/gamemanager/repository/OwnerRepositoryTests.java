package ca.mcgill.ecse321.gamemanager.repository;


import ca.mcgill.ecse321.gamemanager.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class OwnerRepositoryTests {

    @Autowired
    private OwnerRepository ownerRepo;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        ownerRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadOwner() {
        // create owner
        String name = "OwnerName";
        String password = "OwnerPassword";
        String email = "OwnerEmail";

        Owner testOwner = new Owner(password, name, email);

        // save owner in database
        ownerRepo.save(testOwner);

        // retrieve from DB
        Owner ownerFromDB = ownerRepo.findOwnerByEmail(email);

        // assertions
        assertNotNull(ownerFromDB);
        assertEquals(name, ownerFromDB.getName());
        assertEquals(password, ownerFromDB.getPassword());
        assertEquals(email, ownerFromDB.getEmail());
    }
}