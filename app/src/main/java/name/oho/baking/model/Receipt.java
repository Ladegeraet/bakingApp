package name.oho.baking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Parcel
public class Receipt {

    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("ingredients")
    @Expose
    List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    Integer servings;
    @SerializedName("image")
    @Expose
    String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String findLastVideoURL(){
        List<Step> reversedSteps = new ArrayList<>(steps);
        Collections.reverse(reversedSteps);
        for (Step step:reversedSteps) {
            if (!step.getVideoURL().isEmpty()) {
                return step.getVideoURL();
            }
        }
        return "";
    }

    public String findLastThumbnailURL(){
        List<Step> reversedSteps = new ArrayList<>(steps);
        Collections.reverse(reversedSteps);
        for (Step step:reversedSteps) {
            if (!step.getThumbnailURL().isEmpty()) {
                return step.getThumbnailURL();
            }
        }
        return "";
    }

}
