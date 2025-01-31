package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Owner;
import ca.mcgill.ecse321.gamemanager.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class OwnerServiceTests {

    @Mock
    private OwnerRepository mockOwnerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    @AfterEach
    public void clearDb(){
        mockOwnerRepository.deleteAll();
    }

    private final String VALID_EMAIL = "example@example.com";
    private final String VALID_PASSWORD = "1234567890";
    private final String VALID_NEW_PASSWORD = "12345678901";
    private final String INVALID_EMPTY_PASSWORD = "";
    private final String INVALID_LONG_PASSWORD = "123456789012345";
    private final String VALID_NAME = "John";
    private final String VALID_NEW_NAME = "Doe";
    private final String INVALID_NAME = "";


    @Test
    public void testAndCreateValidOwner() {
        Owner owner = new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(owner);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(null);

        Owner createdOwner = ownerService.createOwner(VALID_NAME,VALID_EMAIL,VALID_PASSWORD);

        assertNotNull(createdOwner);
        assertEquals(VALID_PASSWORD, createdOwner.getPassword());
        assertEquals(VALID_EMAIL, createdOwner.getEmail());
        assertEquals(VALID_PASSWORD, createdOwner.getPassword());

        verify(mockOwnerRepository, times(1)).save(createdOwner);

    }

    @Test
    public void testAndCreateOwnerWithExistingEmail(){
        Owner owner = new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(owner);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(owner);


        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.createOwner(VALID_NAME,VALID_EMAIL,VALID_PASSWORD));

        assertEquals("An owner with this email already exists.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testAndCreateOwnerWithEmptyPassword(){

        Owner owner = new Owner(INVALID_EMPTY_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(owner);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(null);

        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.createOwner(VALID_NAME,VALID_EMAIL,INVALID_EMPTY_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testAndCreateOwnerWithInvalidPassword(){

        Owner owner = new Owner(INVALID_LONG_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(owner);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(null);

        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.createOwner(VALID_NAME,VALID_EMAIL,INVALID_LONG_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testGetOwner() {
        Owner owner0= new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        List<Owner> owners = new ArrayList<>();
        owners.add(owner0);

        when(mockOwnerRepository.findAll()).thenReturn(owners);
        Owner foundOwner = ownerService.getOwner();
        assertNotNull(foundOwner);
        assertEquals(owner0, foundOwner);

        verify(mockOwnerRepository, times(1)).findAll();
    }

    @Test
    public void testGetOwnerWithNone() {
        List<Owner> owners = new ArrayList<>();
        when(mockOwnerRepository.findAll()).thenReturn(owners);

        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.getOwner());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testUpdateOwnerSuccessfully() {
        Owner owner = new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(owner);
        Owner newOwner = new Owner(VALID_NEW_PASSWORD, VALID_NEW_NAME,VALID_EMAIL);
        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(newOwner);

        Owner updatedOwner = ownerService.updateOwner(VALID_EMAIL,VALID_NEW_NAME,VALID_NEW_PASSWORD);

        assertNotNull(updatedOwner);
        assertEquals(VALID_NEW_PASSWORD, updatedOwner.getPassword());
        assertEquals(VALID_NEW_NAME, updatedOwner.getName());
        verify(mockOwnerRepository, times(1)).findOwnerByEmail(VALID_EMAIL);
        verify(mockOwnerRepository, times(1)).save(newOwner);
    }

    @Test
    public void testUpdateOwnerWithInvalidPassword() {
        Owner owner = new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(owner);
        Owner newOwner = new Owner(INVALID_LONG_PASSWORD, VALID_NEW_NAME,VALID_EMAIL);
        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(newOwner);

        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.updateOwner(VALID_EMAIL,VALID_NAME,INVALID_LONG_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());


    }

    @Test
    public void testUpdateOwnerWithInvalidName() {
        Owner owner = new Owner(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockOwnerRepository.findOwnerByEmail(any(String.class))).thenReturn(owner);
        Owner newOwner = new Owner(INVALID_LONG_PASSWORD, VALID_NEW_NAME,VALID_EMAIL);
        when(mockOwnerRepository.save(any(Owner.class))).thenReturn(newOwner);

        GameManagerException e = assertThrows(GameManagerException.class, () -> ownerService.updateOwner(VALID_EMAIL,VALID_NAME,INVALID_LONG_PASSWORD));
        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());


    }


}
