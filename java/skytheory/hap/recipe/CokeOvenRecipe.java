package skytheory.hap.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import skytheory.lib.util.ItemHandlerUtils;

public class CokeOvenRecipe implements IRecipeWrapper {

	public ItemStack ingredient;
	public ItemStack result;
	public ItemStack secondary;

	public CokeOvenRecipe(ItemStack ingredient, ItemStack result) {
		this(ingredient, result, ItemStack.EMPTY);
	}

	public CokeOvenRecipe(ItemStack ingredient, ItemStack result, ItemStack secondary) {
		this.ingredient = ingredient;
		this.result = result;
		this.secondary = secondary;
	}

	public boolean match(ItemStack stack) {
		return ingredient.isItemEqual(stack) && ingredient.getCount() <= stack.getCount();
	}

	public boolean canInput(IItemHandler inputHandler) {
		for (ItemHandlerUtils.SlotProperties slot : ItemHandlerUtils.iterator(inputHandler)) {
			if (match(slot.getStack())) {
				ItemStack consume = inputHandler.extractItem(slot.index, ingredient.getCount(), true);
				if (ingredient.getCount() == consume.getCount()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canOutput(IItemHandler outputHandler) {
		IItemHandler holder = ItemHandlerUtils.copyOf(outputHandler);
		if (!ItemHandlerHelper.insertItem(holder, result.copy(), false).isEmpty()) return false;
		if (!ItemHandlerHelper.insertItem(holder, secondary.copy(), false).isEmpty()) return false;
		return true;
	}

	public void processInput(IItemHandler inputHandler) {
		for (ItemHandlerUtils.SlotProperties slot : ItemHandlerUtils.iterator(inputHandler)) {
			ItemStack stack = slot.getStack();
			if (match(stack)) {
				ItemStack consume = inputHandler.extractItem(slot.index, ingredient.getCount(), true);
				if (ingredient.getCount() == consume.getCount()) {
					inputHandler.extractItem(slot.index, ingredient.getCount(), false);
					return;
				}
			}
		}
	}

	// 覚書；今のところchanceはレシピじゃなくてTileEntity依存にする予定
	public void processOutput(IItemHandler outputHandler, float chance, Random rand) {
		ItemHandlerHelper.insertItem(outputHandler, result.copy(), false);
		if (rand.nextFloat() < chance) {
			ItemHandlerHelper.insertItem(outputHandler, secondary.copy(), false);
		}
	}

	public void process(IItemHandler input, IItemHandler output, float chance, Random rand) {
		this.processInput(input);
		this.processOutput(output, chance, rand);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof CokeOvenRecipe)) return false;
		CokeOvenRecipe target = (CokeOvenRecipe) obj;
		if (!ItemStack.areItemStacksEqual(ingredient, target.ingredient)) return false;
		if (!ItemStack.areItemStacksEqual(result, target.result)) return false;
		if (!ItemStack.areItemStacksEqual(secondary, target.secondary)) return false;
		return true;
	}

	// JEI

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, Collections.singletonList(this.ingredient));
		ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(this.result, this.secondary));
	}

	@Override
	public void drawInfo(Minecraft mc, int wid, int hei, int mouseX, int mouseY) {
		if (!secondary.isEmpty()) {
			mc.fontRenderer.drawString("20-100% ", 108, 2, 0x0099FF, false);
		}
	}
}
