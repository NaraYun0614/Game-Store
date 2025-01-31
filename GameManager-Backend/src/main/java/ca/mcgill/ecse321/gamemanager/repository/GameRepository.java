package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    
    // Method to find a game by its ID
    Game findByGameId(int id);

    // Custom query to search games by title or description (case-insensitive)
    List<Game> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    // Custom query to search games by category name
    List<Game> findByCategoryName(String categoryName);

    List<Game> findByRequestStatus(Game.RequestStatus requestStatus);

}

