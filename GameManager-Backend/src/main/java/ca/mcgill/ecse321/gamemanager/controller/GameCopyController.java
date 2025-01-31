package ca.mcgill.ecse321.gamemanager.controller;

import ca.mcgill.ecse321.gamemanager.dto.GameCopyRequestDto;
import ca.mcgill.ecse321.gamemanager.dto.GameCopyResponseDto;
import ca.mcgill.ecse321.gamemanager.exception.GameManagerException;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import ca.mcgill.ecse321.gamemanager.service.GameCopyService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class GameCopyController {
    @Autowired
    private GameCopyService gameCopyService;

    /**
     * Return the gameCopy with the given ID.
     *
     * @param gameCopyId The primary key of the gameCopy to find.
     * @return The gameCopy with the given ID.
     */
    @GetMapping("/game-copy/{gameCopyId}")
    public GameCopyResponseDto findGameCopyByGameCopyId(@PathVariable int gameCopyId){
        GameCopy gameCopy = gameCopyService.findGameCopyByGameCopyId(gameCopyId);
        return new GameCopyResponseDto(gameCopy);
    }

    @GetMapping("/game/{gameid}/game-copies")
    public List<GameCopyResponseDto> findGameCopiesByGameId(@PathVariable int gameid){
        List<GameCopy> gameCopies = gameCopyService.findGameCopiesByGame(gameid);
        List<GameCopyResponseDto> gameCopyResponseDtos = new ArrayList<>();
        for (GameCopy eachGC : gameCopies) {
            gameCopyResponseDtos.add(new GameCopyResponseDto(eachGC));
        }
        return gameCopyResponseDtos;
    }

    @GetMapping("/game/{gameid}/game-copies/count")
    public long countGameCopyOfGame(@PathVariable int gameid) {
        return gameCopyService.countGameCopyOfGame(gameid);
    }

    @PostMapping("/game-copy")
    @ResponseStatus(HttpStatus.CREATED)
    public GameCopyResponseDto createGameCopy(@RequestBody GameCopyRequestDto gameCopyRequestDto) {
        GameCopy savedGameCopy = gameCopyService.createGameCopy(gameCopyRequestDto.getGameId());
        return new GameCopyResponseDto(savedGameCopy);
    }

    @DeleteMapping("/game-copy/{gameCopyId}")
    public ResponseEntity<Void> deleteGameCopy(@PathVariable int gameCopyId){
        gameCopyService.returnGameCopy(gameCopyId);
        return ResponseEntity.noContent().build();
    }
}
