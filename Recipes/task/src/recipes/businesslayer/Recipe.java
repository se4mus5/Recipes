package recipes.businesslayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "RECIPES")
public class Recipe {
    @Id
    @SequenceGenerator(name = "recipeGenerator", sequenceName = "RECIPE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipeGenerator")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // do not use in representation via read path
    private long id;
    @NotBlank(message = "Name field is required")
    private String name;
    @NotBlank(message = "Description field is required")
    private String description;
    @NotBlank(message = "Category field is required")
    private String category;
    @UpdateTimestamp
    private LocalDateTime date;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //private String user;
    @ManyToOne
    @CreatedBy
    @JoinColumn(name = "USER_ID")
    private User user;
    @ElementCollection
    @NotEmpty(message = "At least one ingredient is required")
    private List<String> ingredients;
    @ElementCollection
    @NotEmpty(message = "At least one one direction step is required")
    private List<String> directions;
}
