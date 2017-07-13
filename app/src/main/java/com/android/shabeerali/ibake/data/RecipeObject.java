package com.android.shabeerali.ibake.data;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shabeerali on 6/5/2017.
 */

public class RecipeObject implements Parcelable {
    public int id;
    public String name;
    public String image;
    public int servings;
    public int number_of_ingredients;
    public int number_of_steps;
    public Ingredients[] ingredients;
    public Steps[]  steps;

    public RecipeObject() {
        id = -1;
        name = "";
        image = "";
        servings = 0;
        number_of_ingredients = 0;
        number_of_steps   =  0;
        ingredients = null;
        steps  =  null;
    }

    public void initializeIngredients() {
        ingredients = new Ingredients[number_of_ingredients];
        for(int i = 0; i <number_of_ingredients; i++ ) {
            ingredients[i] = new Ingredients();
        }
    }

    public void initializeSteps() {
        steps = new Steps[number_of_steps];
        for(int i = 0; i <number_of_steps; i++ ) {
            steps[i] = new Steps();
        }
    }

    protected RecipeObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        servings = in.readInt();
        number_of_ingredients = in.readInt();
        number_of_steps = in.readInt();

        ingredients = in.createTypedArray(Ingredients.CREATOR);
        steps = in.createTypedArray(Steps.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(servings);
        dest.writeInt(number_of_ingredients);
        dest.writeInt(number_of_steps);

        dest.writeTypedArray(ingredients, flags);
        dest.writeTypedArray(steps, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipeObject> CREATOR = new Parcelable.Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }

        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject[size];
        }
    };


    public static class Ingredients implements Parcelable {
        public double quantity;
        public String measure;
        public String ingredient;

        Ingredients() {
            quantity = 0;
            measure = "";
            ingredient = "";
        }

        protected Ingredients(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };
    }

    public static class Steps implements Parcelable {
        public int id;
        public String shortDescription;
        public String description;
        public String videoURL;
        public String thumbnailURL;

        Steps() {
            id = -1;
            shortDescription = "";
            description = "";
            videoURL = "";
            thumbnailURL = "";
        }

        protected Steps(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
            @Override
            public Steps createFromParcel(Parcel in) {
                return new Steps(in);
            }

            @Override
            public Steps[] newArray(int size) {
                return new Steps[size];
            }
        };
    }

}

