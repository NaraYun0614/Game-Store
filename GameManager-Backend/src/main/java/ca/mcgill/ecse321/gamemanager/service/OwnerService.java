package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Employee;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.Owner;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;
import ca.mcgill.ecse321.gamemanager.repository.OwnerRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private GameRepository gameRepo;

    public Owner getOwner() {
        List<Owner> gotOwner = (List<Owner>) ownerRepo.findAll();
        if (gotOwner.size() != 1) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "There should be only one owner in the database.");
        }
        return gotOwner.getFirst();

    }

    public Boolean isOwner(String email) {
        Owner owner = ownerRepo.findOwnerByEmail(email);
        if(owner == null) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Owner with email " + email + " not found.");
        }
        return true;
    }

    @Transactional
    public Owner loginOwner(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Email or password cannot be empty.");
        }
        Owner owner = ownerRepo.findOwnerByEmail(email);
        if (owner == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Owner with this email does not exist.");
        }
        String encryptedPassword = SHA256Encryption.getSHA(password);
        if(!owner.getPassword().equals(encryptedPassword)) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Owner with this password does not match.");
        }
        return owner;
    }

    // Create a new owner
    @Transactional
    public Owner createOwner(String name, String email, String password) {
        List<Owner> owners = (List<Owner>) ownerRepo.findAll();
        if(!owners.isEmpty()){
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Owner already exists.");
        }
        if (ownerRepo.findOwnerByEmail(email) != null) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST,"An owner with this email already exists.");
        }
        if (password == null || password.length() < 9 || password.length() > 13) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Password length must be between 9 and 13 characters.");
        }
        Owner newOwner = new Owner(password,name, email);
        return ownerRepo.save(newOwner);
    }

    // Update an existing owner by email
    @Transactional
    public Owner updateOwner(String email, String newName, String newPassword) {
        Owner owner = ownerRepo.findOwnerByEmail(email);
        if (owner == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Invalid Owner email.");
        }

        if (newName == null || newName.isBlank()) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Invalid Owner name.");
        }
        owner.setName(newName);
        if(newPassword == null || newPassword.length() < 9 || newPassword.length() > 13) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Password length must be between 9 and 13 characters.");
        }
        owner.setPassword(newPassword);

        return ownerRepo.save(owner);
    }


    @Transactional
    public Game updateGameDiscount(float discount, int gameId){
        if(discount > 1 || discount < 0) {
            throw new GameManagerException(HttpStatus.BAD_REQUEST, "Invalid discount value. Discount must be between 0 and 1.");
        }
        Game game = gameRepo.findByGameId(gameId);
        if (game == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Game with id " + gameId + " does not exist.");
        }
        double currentPrice = game.getPrice();
        currentPrice = discount * currentPrice;
        game.setPrice(currentPrice);
        return gameRepo.save(game);

    }
}