package com.android.shabeerali.ibake.utilities;

import android.util.Log;

import com.android.shabeerali.ibake.data.RecipeObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.os.Build.ID;

/**
 * Created by Shabeerali on 6/5/2017.
 */

/*
 *  Utility Class for  parsing the JSON adat from moviedb server
 */
public class RecipeDataJsonParser {

    private static final String TAG = RecipeDataJsonParser.class.getSimpleName();

    public static final String RECIPE_ID = "id";

    public static final String RECIPE_NAME = "name";

    public static final String INGREDIENTS = "ingredients";

    public static final String QUANTITY = "quantity";

    public static final String MEASURE = "measure";

    public static final String INGREDIENT = "ingredient";

    public static final String STEPS = "steps";

    public static final String SHORT_DESCRIPTION = "shortDescription";

    public static final String DESCRIPTION = "description";

    public static final String VIDEO_URL = "videoURL";

    public static final String THUMBNAIL_URL = "thumbnailURL";

    public static final String SERVINGS = "servings";

    public static final String IMAGE = "image";

    public static final String STEP_ID = "id";


    /**
     * Parse the JSON response for recipe list request
     *
     * @param recipeListJsonStr  JSON file data
     * @return RecipeObject array with recipe details
     */
    public static RecipeObject[] parseRecipeListInformation(String recipeListJsonStr)
            throws JSONException {

        /* RecipeObject array to hold recipe list data*/
        RecipeObject[] parsedRecipeData = null;

        JSONArray recipesArray = new JSONArray(recipeListJsonStr);

        parsedRecipeData = new RecipeObject[recipesArray.length()];

        Log.e(TAG, "API request returned : " + recipesArray.length());

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeInfo = recipesArray.getJSONObject(i);
            parsedRecipeData[i] = new RecipeObject();
            String name = recipeInfo.getString(RECIPE_NAME);
            int id = recipeInfo.getInt(RECIPE_ID);
            String image = recipeInfo.getString(IMAGE);
            int servings = recipeInfo.getInt(SERVINGS);

            parsedRecipeData[i].id = id;
            parsedRecipeData[i].name = name;
            parsedRecipeData[i].servings = servings;
            parsedRecipeData[i].image = image;


            JSONArray ingredientsArray = recipeInfo.getJSONArray(INGREDIENTS);
            int no_of_ingredients =  ingredientsArray.length();
            parsedRecipeData[i].number_of_ingredients = no_of_ingredients;
            parsedRecipeData[i].initializeIngredients();

            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredientInfo = ingredientsArray.getJSONObject(j);
                parsedRecipeData[i].ingredients[j].quantity =  ingredientInfo.getDouble(QUANTITY);
                parsedRecipeData[i].ingredients[j].measure =  ingredientInfo.getString(MEASURE);
                parsedRecipeData[i].ingredients[j].ingredient =  ingredientInfo.getString(INGREDIENT);
            }

            JSONArray stepsArray =  recipeInfo.getJSONArray(STEPS);
            int no_of_steps =  stepsArray.length();
            parsedRecipeData[i].number_of_steps = no_of_steps;
            parsedRecipeData[i].initializeSteps();

            for (int j = 0; j < stepsArray.length(); j++) {
                JSONObject stepsInfo = stepsArray.getJSONObject(j);
                parsedRecipeData[i].steps[j].id =  stepsInfo.getInt(STEP_ID);
                parsedRecipeData[i].steps[j].shortDescription =  stepsInfo.getString(SHORT_DESCRIPTION);
                parsedRecipeData[i].steps[j].description =  stepsInfo.getString(DESCRIPTION);
                parsedRecipeData[i].steps[j].videoURL =  stepsInfo.getString(VIDEO_URL);
                parsedRecipeData[i].steps[j].thumbnailURL =  stepsInfo.getString(THUMBNAIL_URL);
            }
        }
        return parsedRecipeData;
    }

}
