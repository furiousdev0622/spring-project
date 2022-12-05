package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.common.PosterModel;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.Poster;
import sirkostya009.posterapp.service.PosterService;
import sirkostya009.posterapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService posterService;
    private final UserService userService;

    /**
     * Posts a poster
     * @param posterText text of poster to be published
     * @param token an object that holds current user's username
     * @return a published user
     */
    @PostMapping
    public PosterModel post(@RequestBody String posterText,
                            JwtAuthenticationToken token) {
        var user = userService.findByUsername(token.getName());
        return PosterModel.of(posterService.save(posterText, user), user, false);
    }

    /**
     * Returns a page of recently posted posters
     * @param page page number to be served
     * @param token an object that holds current user's username
     * @return page of posters
     */
    @GetMapping
    public Slice<PosterModel> all(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                  JwtAuthenticationToken token) {
        return sliceOfPostersToPosterModels(
                posterService.recommendation(page, userService.findByUsername(token.getName())),
                userService.findByUsername(token.getName()),
                true
        );
    }

    /**
     * Similar to all method, returns a page of posters but popular ones, sorted and filtered with a special algorithm
     * @param page page number to be served
     * @param token an object that holds current user's username
     * @return page of popular posters
     */
    @GetMapping("/popular")
    public Slice<PosterModel> popular(@RequestParam(name = "page",  defaultValue = "0") Integer page,
                                      JwtAuthenticationToken token) {
        return sliceOfPostersToPosterModels(
                posterService.popular(page),
                userService.findByUsername(token.getName()),
                true
        );
    }

    /**
     * Returns a poster with a specified id
     * @param id provided id
     * @param token an object that holds current user's username
     * @return poster with a specified id
     * @exception RuntimeException if no poster was found
     */
    @GetMapping("/{id}")
    public PosterModel id(@PathVariable Long id,
                          JwtAuthenticationToken token) {
        return PosterModel.of(posterService.getPoster(id), userService.findByUsername(token.getName()), true);
    }

    /**
     * Likes post by the current user
     * @param id id of a poster to like
     * @param token an object that holds current user's username
     * @return true or false whether the user has liked or disliked the post
     * @exception RuntimeException if no poster was found
     */
    @GetMapping("/like/{id}")
    public boolean like(@PathVariable Long id,
                        JwtAuthenticationToken token) {
        return posterService.likePoster(id, userService.findByUsername(token.getName()));
    }

    /**
     * Edits poster's text
     * @param newText new text of a poster
     * @param id id of a poster to edit
     * @param token an object that holds current user's username
     * @return updated poster
     * @exception RuntimeException if no poster was found
     */
    @PostMapping("/edit/{id}")
    public PosterModel edit(@RequestBody String newText,
                            @PathVariable Long id,
                            JwtAuthenticationToken token) {
        var requester = userService.findByUsername(token.getName());
        return PosterModel.of(
                posterService.editPoster(newText, id, requester),
                requester,
                false
        );
    }

    /**
     * Returns a page of most recent posters by user
     * @param username user to query posters of
     * @param page number of the page
     * @param token an object that holds current user's username
     * @return page of posters
     * @exception RuntimeException if no poster or username were found
     */
    @GetMapping("/by/{username}")
    public Slice<PosterModel> userPosters(@PathVariable String username,
                                          @RequestParam(name = "value", defaultValue = "0") Integer page,
                                          JwtAuthenticationToken token) {
        return sliceOfPostersToPosterModels(
                posterService.postersByUser(userService.findByUsername(username), page),
                userService.findByUsername(token.getName()),
                false
        );
    }

    private Slice<PosterModel> sliceOfPostersToPosterModels(Slice<Poster> slice, AppUser requester, boolean includeUserInfo) {
        return new SliceImpl<>(
                listOfPostersToPosterModels(slice.getContent(), requester, includeUserInfo),
                slice.getPageable(),
                slice.hasNext()
        );
    }

    private List<PosterModel> listOfPostersToPosterModels(List<Poster> posters, AppUser requester, boolean includeUserInfo) {
        return posters.stream().map(poster -> PosterModel.of(poster, requester, includeUserInfo)).toList();
    }

}
