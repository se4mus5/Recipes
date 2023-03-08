package recipes.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.businesslayer.User;
import recipes.persistence.UserRepository;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    @Resource
    UserRepository userRepository;
    @Resource
    PasswordEncoder encoder;
    @PostMapping("/register")
    public HttpStatus postRegister(@Valid @RequestBody User user) { //@Valid: HttpResponse.BAD_REQUEST, if invalid
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Username '%s' already exists in the database.", user.getEmail()));
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return HttpStatus.OK;
    }
}
