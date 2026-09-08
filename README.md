# Smart Pantry Manager

An Android app (Java) that helps reduce food waste by tracking pantry
ingredients and suggesting **only** the recipes a user can make right now,
using a strict ingredient-matching rule (no partial matches, no shopping
trip required).

## Features
- Pantry management: add, edit, delete ingredients (name, quantity, unit, optional expiry date)
- Pantry list screen (RecyclerView + custom adapter)
- 18 seeded recipes, each with required ingredients and preparation steps
- Suggested Recipes screen: strict-matching logic, a recipe is only shown
  if 100% of its ingredients (in sufficient quantity) are in the pantry
- Recipe detail screen with full ingredient list and method
- Settings screen: expiring-soon alert toggle, unit preference
- Full CRUD backed by SQLite (SQLiteOpenHelper), data persists between sessions

## Database choice: SQLite
SQLite (via `SQLiteOpenHelper`) was chosen because the app's data
(pantry items + a fixed recipe catalogue) is simple, relational, and fully
local there's no need for cloud sync or a backend server for a
single-user offline pantry tracker. It also matches the on-device
persistent-storage approach covered in the module.

## Project structure
```
app/src/main/java/com/example/smartpantry/
  Ingredient.java, Recipe.java, RecipeIngredient.java   - data models
  DatabaseHelper.java                                   - SQLite CRUD + recipe seed data
  MatchingUtils.java                                    - strict-matching / normalization logic
  PantryListActivity.java                               - home screen (RecyclerView)
  AddEditIngredientActivity.java                        - add/edit form (Create + Update)
  SuggestedRecipesActivity.java                          - runs the strict-match algorithm
  RecipeDetailActivity.java                             - full recipe view
  SettingsActivity.java                                 - preferences
  PantryAdapter.java, RecipeAdapter.java                - RecyclerView adapters
```

## How to run
1. Open Android Studio → **Open** → select the `SmartPantryManager` folder.
2. Let Gradle sync (first sync may take a few minutes, needs internet).
3. Connect a device or start an emulator (API 24+).
4. Click **Run**: the app installs and launches on the Pantry List screen.
5. Tap **+** to add ingredients; tap the Recipes tab to see what you can cook.

## Known limitations / possible improvements
- Matching normalization is naive-rule-based rather than full NLP; very
  irregular plurals not in the small lookup table won't normalize automatically.
- No "Almost There" (missing 1 ingredient) list yet, noted as an optional
  stretch goal in the brief.
