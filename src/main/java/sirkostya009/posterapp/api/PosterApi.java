package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.Poster;
import sirkostya009.posterapp.service.PosterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService service;

    @PostMapping
    public Poster post(@RequestBody Poster poster) {
        return service.save(poster);
    }

    @GetMapping
    public List<Poster> all() {
        return service.findAll();
    }

}
