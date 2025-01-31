package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.Employee;

public class EmployeeResponseDto {
    private String name;
    private String email;

    public EmployeeResponseDto() {}

    public EmployeeResponseDto(Employee employee) {
        if (employee!=null) {
            this.name = employee.getName();
            this.email = employee.getEmail();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}