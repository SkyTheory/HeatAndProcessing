package skytheory.hap.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.recipe.IReactorRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ReactorRecipeBuilder {

	// Input Item
	private List<ArrayList<ItemStack>> inputItems = new ArrayList<>();

	// Output Item
	private ItemStack outputItemPrimary = ItemStack.EMPTY;
	private ItemStack outputItemSecondary = ItemStack.EMPTY;
	private float secondaryChance;

	// Input Fluid
	private FluidStack inputFluidPrimary;
	private FluidStack inputFluidSecondary;

	// Output Fluid
	private FluidStack outputFluidPrimary;
	private FluidStack outputFluidSecondary;

	// Catalyst
	private List<ItemStack> catalysts = Collections.emptyList();

	// Climate
	private List<DCHeatTier> heat = Collections.singletonList(DCHeatTier.NORMAL);

	public ReactorRecipeBuilder inputItem(ItemStack... input) {
		List<ArrayList<ItemStack>> list = new ArrayList<>();
		Arrays.stream(input)
		.map(Collections::singletonList)
		// 覚書：HeatAndClimateのリアクターはArrayListのみ受け付けるっぽい
		.map(ArrayList::new)
		.forEach(list::add);
		this.inputItems = list;
		return this;
	}

	@SafeVarargs
	public final ReactorRecipeBuilder inputItem(List<ItemStack>... input) {
		// 覚書：HeatAndClimateのリアクターはArrayListのみ受け付けるっぽい
		this.inputItems = Arrays.stream(input).map(ArrayList<ItemStack>::new).collect(Collectors.toList());;
		return this;
	}

	public ReactorRecipeBuilder inputFluid(Fluid fluid, int amount) {
		return this.inputFluid(new FluidStack(fluid, amount), null);
	}

	public ReactorRecipeBuilder inputFluid(Fluid fluid1, int amount1, Fluid fluid2, int amount2) {
		return this.inputFluid(new FluidStack(fluid1, amount1), new FluidStack(fluid2, amount2));
	}

	public ReactorRecipeBuilder inputFluid(FluidStack stack) {
		return this.inputFluid(stack, null);
	}

	public ReactorRecipeBuilder inputFluid(FluidStack primary, FluidStack secondary) {
		this.inputFluidPrimary = primary;
		this.inputFluidSecondary = secondary;
		return this;
	}

	public ReactorRecipeBuilder catalyst(ItemStack... catalyst) {
		this.catalysts = Arrays.asList(catalyst);
		return this;
	}

	public ReactorRecipeBuilder catalyst(ArrayList<ItemStack> catalysts) {
		this.catalysts = catalysts;
		return this;
	}

	public ReactorRecipeBuilder outputItem(Item item) {
		return outputItem(new ItemStack(item), ItemStack.EMPTY, 0.0f);
	}

	public ReactorRecipeBuilder outputItem(Item item1, Item item2) {
		return outputItem(new ItemStack(item1), new ItemStack(item2), 0.0f);
	}

	public ReactorRecipeBuilder outputItem(ItemStack stack) {
		return outputItem(stack, ItemStack.EMPTY, 0.0f);
	}

	public ReactorRecipeBuilder outputItem(ItemStack primary, ItemStack secondary) {
		return outputItem(primary, secondary, 1.0f);
	}

	public ReactorRecipeBuilder outputItem(ItemStack primary, ItemStack secondary, float chance) {
		this.outputItemPrimary = primary;
		this.outputItemSecondary = secondary;
		this.secondaryChance = chance;
		return this;
	}

	public ReactorRecipeBuilder outputFluid(FluidStack stack) {
		return this.outputFluid(stack, null);
	}

	public ReactorRecipeBuilder outputFluid(FluidStack primary, FluidStack secondary) {
		this.outputFluidPrimary = primary;
		this.outputFluidSecondary = secondary;
		return this;
	}

	public ReactorRecipeBuilder heatTier(DCHeatTier heat) {
		Set<DCHeatTier> set = new HashSet<>();
		set.add(heat);
		set.add(heat.addTier(1));
		set.add(heat.addTier(-1));
		return heatTier(new ArrayList<>(set));
	}

	public ReactorRecipeBuilder heatTier(List<DCHeatTier> heat) {
		this.heat = heat;
		return this;
	}

	public IReactorRecipe build() {
		return new CustomReactorRecipe(
				inputItems,
				outputItemPrimary,
				outputItemSecondary,
				secondaryChance,
				inputFluidPrimary,
				inputFluidSecondary,
				outputFluidPrimary,
				outputFluidSecondary,
				catalysts,
				heat
				);

	}
}
