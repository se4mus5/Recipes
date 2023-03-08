package recipes.businesslayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Pattern(regexp = ".+@.+\\..+")
    // @Email // doesn't conform the tests :(
    @NotBlank(message = "Email field is required")
    private String email;
    @NotBlank(message = "A password of minimum 8 characters is required")
    @Size(min = 8)
    private String password;
}
