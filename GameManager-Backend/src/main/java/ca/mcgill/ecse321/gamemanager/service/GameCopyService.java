package ca.mcgill.ecse321.gamemanager.service;

import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.repository.GameCopyRepository;
import ca.mcgill.ecse321.gamemanager.repository.GameRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class GameCopyService {
    @Autowired
    private GameCopyRepository gameCopyRepo;
    @Autowired
    private GameRepository gameRepo;

    public GameCopy findGameCopyByGameCopyId(int gameCopyId){
        Optional<GameCopy> optionalGameCopy = Optional.ofNullable(gameCopyRepo.findGameCopyByGameCopyId(gameCopyId));
        return optionalGameCopy.orElseThrow(() -> new GameManagerException(HttpStatus.NOT_FOUND, "Invalid GameCopy ID."));
    }

    public List<GameCopy> findGameCopiesByGame(int gameId){
        Game game = gameRepo.findByGameId(gameId);
        if (game==null) throw new GameManagerException(HttpStatus.NOT_FOUND, "Invalid Game ID.");
        return Collections.unmodifiableList(gameCopyRepo.findGameCopiesByGame(game));
    }

    public long countGameCopyOfGame(int gameId) {
        Game game = gameRepo.findByGameId(gameId);
        if (game==null) throw new GameManagerException(HttpStatus.BAD_REQUEST, "Invalid Game ID.");
        return gameCopyRepo.countByGame(game);
    }

    @Transactional
    public GameCopy createGameCopy(int gameId) {
        Game targetGame = gameRepo.findByGameId(gameId);
        if (targetGame == null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Invalid Game ID.");
        }
        int stock = targetGame.getStock();

        if (stock <= 0) {
            String gameTitle = targetGame.getTitle();
            throw new GameManagerException(HttpStatus.BAD_REQUEST, gameTitle + " with Game ID " + gameId + " is currently out of stock.");
        }

        targetGame.setStock(stock - 1);
        gameRepo.save(targetGame);
        GameCopy newGameCopy = new GameCopy(targetGame);
        return gameCopyRepo.save(newGameCopy);
    }

    @Transactional
    public void returnGameCopy(int gameCopyId){
        GameCopy gameCopy = findGameCopyByGameCopyId(gameCopyId);
        int gameId = gameCopy.getGame().getGameId();
        Game game = gameRepo.findByGameId(gameId);
        if (game==null) {
            throw new GameManagerException(HttpStatus.NOT_FOUND, "Game not found");
        }
        int stock = game.getStock();
        game.setStock(stock+1);
        gameCopyRepo.delete(gameCopy);
    }

}
