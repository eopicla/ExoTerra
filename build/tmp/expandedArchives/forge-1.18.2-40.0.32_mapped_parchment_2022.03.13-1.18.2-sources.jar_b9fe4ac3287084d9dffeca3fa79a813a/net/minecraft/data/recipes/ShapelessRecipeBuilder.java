package net.minecraft.data.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class ShapelessRecipeBuilder implements RecipeBuilder {
   private final Item result;
   private final int count;
   private final List<Ingredient> ingredients = Lists.newArrayList();
   private final Advancement.Builder advancement = Advancement.Builder.advancement();
   @Nullable
   private String group;

   public ShapelessRecipeBuilder(ItemLike pResult, int pCount) {
      this.result = pResult.asItem();
      this.count = pCount;
   }

   /**
    * Creates a new builder for a shapeless recipe.
    */
   public static ShapelessRecipeBuilder shapeless(ItemLike pResult) {
      return new ShapelessRecipeBuilder(pResult, 1);
   }

   /**
    * Creates a new builder for a shapeless recipe.
    */
   public static ShapelessRecipeBuilder shapeless(ItemLike pResult, int pCount) {
      return new ShapelessRecipeBuilder(pResult, pCount);
   }

   /**
    * Adds an ingredient that can be any item in the given tag.
    */
   public ShapelessRecipeBuilder requires(TagKey<Item> pTag) {
      return this.requires(Ingredient.of(pTag));
   }

   /**
    * Adds an ingredient of the given item.
    */
   public ShapelessRecipeBuilder requires(ItemLike pItem) {
      return this.requires(pItem, 1);
   }

   /**
    * Adds the given ingredient multiple times.
    */
   public ShapelessRecipeBuilder requires(ItemLike pItem, int pQuantity) {
      for(int i = 0; i < pQuantity; ++i) {
         this.requires(Ingredient.of(pItem));
      }

      return this;
   }

   /**
    * Adds an ingredient.
    */
   public ShapelessRecipeBuilder requires(Ingredient pIngredient) {
      return this.requires(pIngredient, 1);
   }

   /**
    * Adds an ingredient multiple times.
    */
   public ShapelessRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
      for(int i = 0; i < pQuantity; ++i) {
         this.ingredients.add(pIngredient);
      }

      return this;
   }

   public ShapelessRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
      this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
      return this;
   }

   public ShapelessRecipeBuilder group(@Nullable String pGroupName) {
      this.group = pGroupName;
      return this;
   }

   public Item getResult() {
      return this.result;
   }

   public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
      this.ensureValid(pRecipeId);
      this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
      pFinishedRecipeConsumer.accept(new ShapelessRecipeBuilder.Result(pRecipeId, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + pRecipeId.getPath())));
   }

   /**
    * Makes sure that this recipe is valid and obtainable.
    */
   private void ensureValid(ResourceLocation pId) {
      if (this.advancement.getCriteria().isEmpty()) {
         throw new IllegalStateException("No way of obtaining recipe " + pId);
      }
   }

   public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final Item result;
      private final int count;
      private final String group;
      private final List<Ingredient> ingredients;
      private final Advancement.Builder advancement;
      private final ResourceLocation advancementId;

      public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<Ingredient> pIngredients, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
         this.id = pId;
         this.result = pResult;
         this.count = pCount;
         this.group = pGroup;
         this.ingredients = pIngredients;
         this.advancement = pAdvancement;
         this.advancementId = pAdvancementId;
      }

      public void serializeRecipeData(JsonObject pJson) {
         if (!this.group.isEmpty()) {
            pJson.addProperty("group", this.group);
         }

         JsonArray jsonarray = new JsonArray();

         for(Ingredient ingredient : this.ingredients) {
            jsonarray.add(ingredient.toJson());
         }

         pJson.add("ingredients", jsonarray);
         JsonObject jsonobject = new JsonObject();
         jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
         if (this.count > 1) {
            jsonobject.addProperty("count", this.count);
         }

         pJson.add("result", jsonobject);
      }

      public RecipeSerializer<?> getType() {
         return RecipeSerializer.SHAPELESS_RECIPE;
      }

      /**
       * Gets the ID for the recipe.
       */
      public ResourceLocation getId() {
         return this.id;
      }

      /**
       * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
       */
      @Nullable
      public JsonObject serializeAdvancement() {
         return this.advancement.serializeToJson();
      }

      /**
       * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
       * is non-null.
       */
      @Nullable
      public ResourceLocation getAdvancementId() {
         return this.advancementId;
      }
   }
}