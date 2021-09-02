package skytheory.hap.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.recipe.IReactorRecipe;
import defeatedcrow.hac.api.recipe.IRecipePanel;
import defeatedcrow.hac.core.fluid.FluidDictionaryDC;
import defeatedcrow.hac.core.util.DCUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class CustomReactorRecipe implements IReactorRecipe {

	protected final List<ArrayList<ItemStack>> inputItems;
	protected final ItemStack outputItemPrimary;
	protected final ItemStack outputItemSecondary;
	protected final float secondaryChance;
	protected final List<ItemStack> catalysts;
	protected final FluidStack inputFluidPrimary;
	protected final FluidStack inputFluidSecondary;
	protected final FluidStack outputFluidPrimary;
	protected final FluidStack outputFluidSecondary;
	protected final List<DCHeatTier> heat;
	private final int count;

	public CustomReactorRecipe(
			List<ArrayList<ItemStack>> inputItems,
			ItemStack outputItemPrimary,
			ItemStack outputItemSecondary,
			float secondaryChance,
			FluidStack inputFluidPrimary,
			FluidStack inputFluidSecondary,
			FluidStack outputFluidPrimary,
			FluidStack outputFluidSecondary,
			List<ItemStack> catalysts,
			List<DCHeatTier> heat
			) {
		this.inputItems = Validate.noNullElements(Validate.notNull(inputItems));
		inputItems.forEach(Validate::noNullElements);
		this.outputItemPrimary = Validate.notNull(outputItemPrimary);
		this.outputItemSecondary = Validate.notNull(outputItemSecondary);
		this.secondaryChance = secondaryChance;
		this.inputFluidPrimary = inputFluidPrimary;
		this.inputFluidSecondary = inputFluidSecondary;
		this.outputFluidPrimary = outputFluidPrimary;
		this.outputFluidSecondary = outputFluidSecondary;
		this.catalysts = Validate.notNull(catalysts);
		this.heat = Validate.notNull(heat);
		int count = 0;
		if (inputFluidPrimary != null) count++;
		if (inputFluidSecondary != null) count++;
		count += inputItems.size();
		this.count = count;
	}

	@Override
	public Object[] getInput() {
		return inputItems.toArray();
	}

	@Override
	public ItemStack getOutput() {
		return outputItemPrimary.copy();
	}

	@Override
	public ItemStack getSecondary() {
		return outputItemSecondary.copy();
	}

	@Override
	public List<ItemStack> getCatalyst() {
		return catalysts;
	}

	@Override
	public FluidStack getInputFluid() {
		return inputFluidPrimary;
	}

	@Override
	public FluidStack getSubInputFluid() {
		return inputFluidSecondary;
	}

	@Override
	public FluidStack getOutputFluid() {
		return outputFluidPrimary;
	}

	@Override
	public FluidStack getSubOutputFluid() {
		return outputFluidSecondary;
	}

	// 本家側で仕様変更されたなら、こっちも変えること
	@Deprecated
	@Override
	public List<ItemStack> getContainerItems(List<Object> items) {
		List<ItemStack> result = items.stream()
				.filter(List.class::isInstance)
				.map(list -> (List<?>) list)
				.flatMap(list -> list.stream())
				.filter(ItemStack.class::isInstance)
				.map(ItemStack.class::cast)
				.filter(stack -> stack.getItem().hasContainerItem(stack))
				.map(stack -> stack.getItem().getContainerItem(stack))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public float getSecondaryChance() {
		return secondaryChance;
	}

	@Override
	public List<Object> getProcessedInput() {
		return new ArrayList<Object>(inputItems);
	}

	@Override
	public int getRecipeSize() {
		return inputItems.size();
	}

	@Override
	public boolean matches(List<ItemStack> items, FluidStack fluid1, FluidStack fluid2) {
		// 液体を必要とするレシピの場合、タンクにそもそも液体がなければfalse
		if (inputFluidPrimary == null && fluid1 != null) return false;
		if (inputFluidSecondary == null && fluid2 != null) return false;

		// レシピの液体とタンク内部の液体が異なる、もしくは必要数に満たないならfalse
		if (inputFluidPrimary != null) {
			if (fluid1 == null) return false;
			if (!FluidDictionaryDC.matchFluid(inputFluidPrimary.getFluid(), fluid1.getFluid())) return false;
			if (inputFluidPrimary.amount > fluid1.amount) return false;
		}
		if (inputFluidSecondary != null) {
			if (fluid2 == null) return false;
			if (!FluidDictionaryDC.matchFluid(inputFluidSecondary.getFluid(), fluid2.getFluid())) return false;
			if (inputFluidSecondary.amount > fluid2.amount) return false;
		}

		// 要求材料のリストを作成
		List<List<ItemStack>> recipe = new ArrayList<>(inputItems);
		List<ItemStack> matched = new ArrayList<>();
		// インベントリのアイテムについて
		for (ItemStack stack : items) {
			Iterator<List<ItemStack>> recipeIterator = recipe.iterator();
			// インベントリの中身が空であるか、スロットパネルなら確認処理をスキップする
			if (stack.isEmpty() || stack.getItem() instanceof IRecipePanel) continue;
			// レシピに要求されているアイテムのうち、どれか一種類でも一致すれば
			boolean match = false;
			while (recipeIterator.hasNext()) {
				List<ItemStack> oreDictionary = recipeIterator.next();
				if (oreDictionary.stream().anyMatch(stack::isItemEqual)) {
					// 一致したアイテムを要求素材リストから削除
					recipeIterator.remove();
					match = true;
					matched.addAll(oreDictionary);
					break;
				}
			}
			// 要求素材に一致しなかった場合、レシピに関係するアイテムかを判定、関係なければfalse
			if (!match && !matched.stream().anyMatch(stack::isItemEqual)) {
				return false;
			}
		}
		// 材料が全種類揃っているならtrue
		return recipe.isEmpty();
	}

	@Override
	public boolean matchOutput(List<ItemStack> target, FluidStack fluid1, FluidStack fluid2, int slotsize) {
		if (outputFluidPrimary != null && fluid1 != null) {
			if (!outputFluidPrimary.isFluidEqual(fluid1)) return false;
		}
		if (outputFluidSecondary != null && fluid2 != null) {
			if (!outputFluidSecondary.isFluidEqual(fluid2)) return false;
		}
		// 第一引数はIInventoryとかで使ってるNonNullList<ItemStack>かな？
		if (target != null && !target.isEmpty()) {
			int i1 = -2;
			int i2 = -2;
			if (DCUtil.isEmpty(this.getOutput()))
				i1 = -1;
			if (DCUtil.isEmpty(this.getSecondary()))
				i2 = -1;
			for (int i = 0; i < target.size(); i++) {
				ItemStack get = target.get(i);
				if (i1 == -2 && DCUtil.canInsert(this.getOutput(), get)) {
					i1 = i;
					continue;
				}
				if (i2 == -2 && DCUtil.canInsert(this.getSecondary(), get)) {
					i2 = i;
				}
			}
			if (i1 == -1 && i2 == -1) {
				return true;
			} else {
				return i1 > -2 && i2 > -2 && i1 != i2;
			}
		} else {
			return DCUtil.isEmpty(this.getOutput()) && DCUtil.isEmpty(this.getSecondary());
		}
	}

	@Override
	public boolean matchCatalyst(ItemStack catalyst) {
		return catalysts.isEmpty() || catalysts.stream().anyMatch(stack -> stack.isItemEqual(catalyst));
	}

	@Override
	public boolean matchHeatTier(int id) {
		DCHeatTier tier = DCHeatTier.getTypeByID(id);
		return matchHeatTier(tier);
	}

	@Override
	public boolean matchHeatTier(DCHeatTier tier) {
		return requiredHeat().isEmpty() || requiredHeat().contains(tier);
	}

	// 覚書：加工できるときにtrueを返す！
	@Override
	public boolean additionalRequire(World world, BlockPos pos) {
		return true;
	}

	@Override
	public List<DCHeatTier> requiredHeat() {
		return heat;
	}

	@Deprecated
	@Override
	public String additionalString() {
		return "";
	}

	@Override
	public int recipeCoincidence() {
		return count;
	}

	@Override
	public boolean isSimpleRecipe() {
		return inputItems.size() <= 2 && inputFluidSecondary == null && outputFluidSecondary == null;
	}
}
