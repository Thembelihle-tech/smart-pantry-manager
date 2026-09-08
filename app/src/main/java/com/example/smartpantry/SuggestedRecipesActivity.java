package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the strict-matching rule (Section 2.3) against the current pantry
 * and lists ONLY the recipes the user can make right now, in full.
 */
public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerSuggested);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView emptyView = findViewById(R.id.textEmptySuggestions);

        List<Ingredient> pantry = dbHelper.getAllIngredients();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (recipeIsFullyCovered(recipe, pantry)) {
                matches.add(recipe);
            }
        }

        if (matches.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(matches, recipe -> {
                Intent intent = new Intent(this, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_recipes);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryListActivity.class));
                return true;
            } else if (id == R.id.nav_recipes) {
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * A recipe qualifies as "suggested" ONLY if every single required
     * ingredient is covered by the pantry (Section 2.3). One missing or
     * insufficient ingredient disqualifies the whole recipe.
     */
    private boolean recipeIsFullyCovered(Recipe recipe, List<Ingredient> pantry) {
        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean covered = false;
            for (Ingredient pantryItem : pantry) {
                if (MatchingUtils.pantryCovers(pantryItem, required)) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                return false; // strict rule: bail out on the first missing ingredient
            }
        }
        return true;
    }
}
