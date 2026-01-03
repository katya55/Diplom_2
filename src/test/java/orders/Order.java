package orders;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<String> ingredients = new ArrayList<>();

    public List<String> getIngredients() {
        return ingredients;
    }

    public void add(String id) {
        ingredients.add(id);
    }

    public void addAll(List<String> ids) {
        ingredients.addAll(ids);  // добавляет сразу весь список
    }
}
