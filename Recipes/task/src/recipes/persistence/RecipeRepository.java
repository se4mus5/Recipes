package recipes.persistence;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import recipes.businesslayer.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

}
