package com.coffee.BLL;

import com.coffee.DAL.RecipeDAL;
import com.coffee.DTO.Material;
import com.coffee.DTO.Module;
import com.coffee.DTO.Recipe;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeBLL extends Manager<Recipe> {
    private RecipeDAL recipeDAL;

    public RecipeBLL() {
        recipeDAL = new RecipeDAL();
    }

    public RecipeDAL getRecipeDAL() {
        return recipeDAL;
    }

    public void setRecipeDAL(RecipeDAL recipeDAL) {
        this.recipeDAL = recipeDAL;
    }

    public Object[][] getData() {
        return getData(recipeDAL.searchRecipes());
    }

    public Pair<Boolean, String> addRecipe(Recipe recipe) {
        Pair<Boolean, String> result;

        result = exists(recipe);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (recipeDAL.addRecipe(recipe) == 0)
            return new Pair<>(false, "Thêm công thức không thành công.");
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> updateRecipe(Recipe recipe) {

        if (recipeDAL.updateRecipe(recipe) == 0)
            return new Pair<>(false, "Cập nhật công thức không thành công.");
        return new Pair<>(true, "");
    }

    public Pair<Boolean, String> deleteRecipe(Recipe recipe) {
        if (recipeDAL.deleteRecipe("product_id = " + recipe.getProduct_id(),
                "material_id = " + recipe.getMaterial_id(), "size = '" + recipe.getSize() + "'") == 0)
            return new Pair<>(false, "Xoá công thức không thành công.");

        return new Pair<>(true, "Xoá công thức thành công.");
    }

    public List<Recipe> searchRecipes(String... conditions) {
        return recipeDAL.searchRecipes(conditions);
    }

    public List<Recipe> findRecipes(String key, String value) {
        List<Recipe> list = new ArrayList<>();
        List<Recipe> recipeList = recipeDAL.searchRecipes();
        for (Recipe recipe : recipeList) {
            if (getValueByKey(recipe, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(recipe);
            }
        }
        return list;
    }

    public List<Recipe> findRecipesBy(Map<String, Object> conditions) {
        List<Recipe> recipes = recipeDAL.searchRecipes();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            recipes = findObjectsBy(entry.getKey(), entry.getValue(), recipes);
        return recipes;
    }

    public Pair<Boolean, String> exists(Recipe recipe) {
        List<Recipe> recipes = findRecipesBy(Map.of(
                "product_id", recipe.getProduct_id(),
                "material_id", recipe.getProduct_id(),
                "size", recipe.getSize()
        ));

        if (!recipes.isEmpty()) {
            return new Pair<>(true, "Công thức đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    @Override
    public Object getValueByKey(Recipe recipe, String key) {
        return switch (key) {
            case "product_id" -> recipe.getProduct_id();
            case "material_id" -> recipe.getMaterial_id();
            case "quantity" -> recipe.getQuantity();
            case "size" -> recipe.getSize();
            case "unit" -> recipe.getUnit();
            default -> null;
        };
    }

//    public List<List<Object>> searchRecipesByProduct(int product_id, String size) {
//        List<List<Object>> result = new ArrayList<>();
//        for (Recipe recipe : searchRecipes("product_id = " + product_id, "size = '" + size + "'")) {
//            List<Object> objects = new ArrayList<>();
//            Material material = new MaterialBLL().searchMaterials("id = " + recipe.getMaterial_id()).get(0);
//            objects.add(material.getId());
//            objects.add(material.getName());
//            objects.add(recipe.getQuantity());
//            objects.add(recipe.getUnit());
//
//            result.add(objects);
//        }
//        return result;
//    }

    public static void main(String[] args) {
        RecipeBLL receiptBLL = new RecipeBLL();
//        Recipe recipe = new Recipe(1, 1, 0);
//        receiptBLL.addRecipe(recipe);
//        recipe.setQuantity(50);
//        receiptBLL.updateRecipe(recipe);
//        receiptBLL.deleteRecipe(recipe);
    }
}
