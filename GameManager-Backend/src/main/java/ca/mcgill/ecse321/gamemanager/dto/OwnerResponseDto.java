package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.Owner;

public class OwnerResponseDto {
    private String name;
    private String email;

    public OwnerResponseDto() {}

    public OwnerResponseDto(Owner owner) {
        this.name = owner.getName();
        this.email = owner.getEmail();
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
