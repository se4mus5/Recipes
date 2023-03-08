package recipes.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.businesslayer.Recipe;
import recipes.businesslayer.RecipeService;
import recipes.businesslayer.User;
import recipes.persistence.UserRepository;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController // makes a class provide exact endpoints (a requested URL) to access the REST methods
@Validated
@RequestMapping("/api/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;
    @Resource
    private UserRepository userRepository;

    // POST receives a recipe as a JSON object and overrides the current recipe.
    @PostMapping("/new") // works in conjunction with RequestMapping, gets appended
    public ResponseEntity<Map<String, Object>> postNewRecipe(@AuthenticationPrincipal UserDetails currentUserDetails, @Valid @RequestBody Recipe newRecipe) { //@Valid: HttpResponse.BAD_REQUEST, if invalid
        User currentUser = userRepository.findByEmail(currentUserDetails.getUsername());
        newRecipe.setUser(currentUser);
        recipeService.saveRecipe(newRecipe);
        return new ResponseEntity<>(Map.of("id", newRecipe.getId()), HttpStatus.OK);
    }

    // GET returns the current recipe as a JSON object.
    @GetMapping("/{id}") // works in conjunction with RequestMapping, gets appended
    public Recipe getRecipeById(@PathVariable long id) {
        return recipeService.getRecipeById(id);
    }

    // DELETE recipe by id.
    @DeleteMapping("/{id}") // works in conjunction with RequestMapping, gets appended
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipeById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        if (recipeService.getRecipeById(id).getUser() != null && // null should be accepted as per test suite
                !recipeService.getRecipeById(id).getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only its author is authorized to DELETE a recipe.");
        }
        recipeService.deleteRecipeById(id);
    }

    // PUT recipe id.
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void putRecipe(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Recipe newRecipe, @PathVariable long id) { //@Valid = HttpResponse.BAD_REQUEST, if invalid
        System.out.println("##DIAG## recipe to be deleted: " + recipeService.getRecipeById(id));
        System.out.println("##DIAG## deleting user: " + userDetails.getUsername());
        if (recipeService.getRecipeById(id).getUser() != null && // null should be accepted as per test suite
                !recipeService.getRecipeById(id).getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only its author is authorized to UPDATE a recipe.");
        }
        recipeService.updateRecipeById(newRecipe, id);
    }

    // GET search by name, mutually exclusive with search by category
    @RequestMapping(value = "/search", params = {"name", "!category"}, method = RequestMethod.GET)
    public List<Recipe> searchByName(@RequestParam String name) {
        return recipeService.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    // GET search by category, mutually exclusive with search by name
    @RequestMapping(value = "/search", params = {"category", "!name"}, method = RequestMethod.GET)
    public List<Recipe> searchByCategory(@RequestParam String category) {
        return recipeService.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }
}