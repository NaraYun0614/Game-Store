package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Employee;
import ca.mcgill.ecse321.gamemanager.model.Review;
import ca.mcgill.ecse321.gamemanager.repository.EmployeeRepository;
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
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository mockEmployeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    @AfterEach
    public void clearDb(){
        mockEmployeeRepository.deleteAll();
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
    public void testAndCreateValidEmployee() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(null);

        Employee createdEmployee = employeeService.createEmployee(VALID_NAME,VALID_EMAIL,VALID_PASSWORD);

        assertNotNull(createdEmployee);
        assertEquals(VALID_PASSWORD, createdEmployee.getPassword());
        assertEquals(VALID_EMAIL, createdEmployee.getEmail());
        assertEquals(VALID_PASSWORD, createdEmployee.getPassword());

        verify(mockEmployeeRepository, times(1)).save(createdEmployee);

    }

    @Test
    public void testAndCreateEmployeeWithExistingEmail(){
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);

        //GameManagerException e = assertThrows(GameManagerException.class, () -> reviewService.createReview(VALID_RATING, INVALID_EMPTY_COMMENT, customerEmail, gameId));
        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.createEmployee(VALID_NAME,VALID_EMAIL,VALID_PASSWORD));

        assertEquals("A employee with this email already exists.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testAndCreateEmployeeWithEmptyPassword(){

        Employee employee = new Employee(INVALID_EMPTY_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(null);

        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.createEmployee(VALID_NAME,VALID_EMAIL,INVALID_EMPTY_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testAndCreateEmployeeWithInvalidPassword(){

        Employee employee = new Employee(INVALID_LONG_PASSWORD, VALID_NAME,VALID_EMAIL);

        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(null);

        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.createEmployee(VALID_NAME,VALID_EMAIL,INVALID_LONG_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());

    }

    @Test
    public void testGetEmployeeByEmail() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);

        Employee foundEmployee = employeeService.findEmployeeByEmail(VALID_EMAIL);

        assertNotNull(foundEmployee);
        assertEquals(VALID_EMAIL, foundEmployee.getEmail());
        assertEquals(VALID_PASSWORD, foundEmployee.getPassword());
        assertEquals(VALID_NAME, foundEmployee.getName());
        verify(mockEmployeeRepository, times(1)).findEmployeeByEmail(VALID_EMAIL);
    }

    @Test
    public void testGetAllEmployees() {
            Employee employee0 = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
            Employee employee1 = new Employee("1234567890", VALID_NAME,"example1@example.com");
            List<Employee> employees = new ArrayList<>();
            employees.add(employee0);
            employees.add(employee1);

            when(mockEmployeeRepository.findAll()).thenReturn(employees);
            List<Employee> foundEmployees = employeeService.getAllEmployees();
            assertNotNull(foundEmployees);
            assertEquals(employees.size(), foundEmployees.size());
            assertEquals(employee0, foundEmployees.get(0));
            assertEquals(employee1, foundEmployees.get(1));
            assertTrue(foundEmployees.contains(employee0));
            assertTrue(foundEmployees.contains(employee1));
            verify(mockEmployeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllEmployeesInAlphabeticalOrder() {
        Employee employee0 = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        Employee employee1 = new Employee("1234567890", VALID_NEW_NAME,"abcd@example.com");
        List<Employee> employees = new ArrayList<>();
        employees.add(employee0);
        employees.add(employee1);

        when(mockEmployeeRepository.findAll()).thenReturn(employees);
        List<Employee> foundEmployees = employeeService.getAllEmployeesInAlphabet();
        assertNotNull(foundEmployees);
        assertEquals(employees.size(), foundEmployees.size());
        assertEquals(employee1, foundEmployees.get(0));
        assertEquals(employee0, foundEmployees.get(1));
        assertTrue(foundEmployees.contains(employee0));
        assertTrue(foundEmployees.contains(employee1));
        verify(mockEmployeeRepository, times(1)).findAll();

    }

    @Test
    public void testUpdateEmployeeSuccessfully() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);
        Employee newEmployee = new Employee(VALID_NEW_PASSWORD, VALID_NEW_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        Employee updatedEmployee = employeeService.updateEmployee(VALID_EMAIL,VALID_NEW_NAME,VALID_NEW_PASSWORD);

        assertNotNull(updatedEmployee);
        assertEquals(VALID_NEW_PASSWORD, updatedEmployee.getPassword());
        assertEquals(VALID_NEW_NAME, updatedEmployee.getName());
        verify(mockEmployeeRepository, times(1)).findEmployeeByEmail(VALID_EMAIL);
        verify(mockEmployeeRepository, times(1)).save(newEmployee);
    }

    @Test
    public void testUpdateEmployeeWithInvalidPassword() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);
        Employee newEmployee = new Employee(INVALID_LONG_PASSWORD, VALID_NEW_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.updateEmployee(VALID_EMAIL,VALID_NAME,INVALID_LONG_PASSWORD));

        assertEquals("Password length must be between 9 and 13 characters.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());


    }

    @Test
    public void testUpdateEmployeeWithInvalidName() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);
        Employee newEmployee = new Employee(VALID_PASSWORD, INVALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.updateEmployee(VALID_EMAIL,INVALID_NAME,VALID_PASSWORD));

        assertEquals("Invalid Employee name.",e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,e.getStatus());


    }

    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee(VALID_PASSWORD, VALID_NAME,VALID_EMAIL);
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(employee);

        employeeService.deleteEmployee(VALID_EMAIL);
        verify(mockEmployeeRepository, times(1)).delete(employee);

    }

    @Test
    public void testDeleteEmployeeWithInvalidEmail() {
        String InvalidEmail = "example@example.com";
        when(mockEmployeeRepository.findEmployeeByEmail(any(String.class))).thenReturn(null);

        GameManagerException e = assertThrows(GameManagerException.class, () -> employeeService.deleteEmployee(InvalidEmail));

        assertEquals(HttpStatus.NOT_FOUND,e.getStatus());
        assertEquals("Employee with email example@example.com does not exist.",e.getMessage());
    }



}
