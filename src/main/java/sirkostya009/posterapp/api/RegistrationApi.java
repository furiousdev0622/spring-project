package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.registration.RegistrationRequest;
import sirkostya009.posterapp.service.RegistrationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register")
public class RegistrationApi {

    private final RegistrationService service;

    /**
     * receives login and password strings and returns a token to confirm newly created acc
     * @param request a POJO class with login and password strings
     * @return token
     */
    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return service.register(request);
    }

    /**
     * receives a token string as a request param, validates if not validated, otherwise throws an error
     * @param token a random uuid
     */
    @GetMapping("/confirm")
    public void confirm(@RequestParam String token) {
        service.confirm(token);
    }

}
