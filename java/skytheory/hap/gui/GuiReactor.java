package skytheory.hap.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import defeatedcrow.hac.api.climate.DCHeatTier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import skytheory.hap.HeatAndProcessing;
import skytheory.hap.container.ContainerReactor;
import skytheory.hap.tile.TileReactorAdvanced;
import skytheory.lib.gui.GuiBase;
import skytheory.lib.gui.HoverButton;
import skytheory.lib.gui.RenderUtil;
import skytheory.lib.gui.ToggleButton;

public class GuiReactor extends GuiBase<ContainerReactor> {

	public static final ResourceLocation TEXTURE_REACTOR_GUI = new ResourceLocation(HeatAndProcessing.MOD_ID, "textures/gui/reactor_advanced.png");
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 201;

	public static final int TANK_X = 125;
	public static final int TANK_Y1 = 21;
	public static final int TANK_Y2 = 48;
	public static final int TANK_Y3 = 75;
	public static final int TANK_Y4 = 102;
	public static final int TANK_GAUGE_WIDTH = 40;
	public static final int TANK_GAUGE_HEIGHT = 7;

	public static final int LOCK_X = 124;
	public static final int LOCK_Y1 = 11;
	public static final int LOCK_Y2 = 38;
	public static final int LOCK_Y3 = 65;
	public static final int LOCK_Y4 = 92;

	public static final int LOCK_OVERLAY_X1 = 176;
	public static final int LOCK_OVERLAY_X2 = 182;
	public static final int LOCK_OVERLAY_X3 = 188;
	public static final int LOCK_OVERLAY_X4 = 194;
	public static final int LOCK_OVERLAY_Y = 0;

	public static final int LOCK_WIDTH = 6;
	public static final int LOCK_HEIGHT = 7;

	public static final int INCR_X = 79;
	public static final int INCR_Y = 106;
	public static final int INCR_OVERLAY_X = 183;
	public static final int INCR_OVERLAY_Y = 14;

	public static final int DECR_X = 18;
	public static final int DECR_Y = 106;
	public static final int DECR_OVERLAY_X = 176;
	public static final int DECR_OVERLAY_Y = 14;

	public static final int HEAT_CTRL_WIDTH = 7;
	public static final int HEAT_CTRL_HEIGHT = 7;

	public static final int HEAT_X = 45;
	public static final int HEAT_Y = 103;
	public static final int HEAT_MOVE = 3;
	public static final int HEAT_BAR_X = 176;
	public static final int HEAT_BAR_Y = 35;
	public static final int HEAT_BAR_WIDTH = 5;
	public static final int HEAT_BAR_HEIGHT = 10;

	public static final int HEAT_GAUGE_X1 = 18;
	public static final int HEAT_GAUGE_X2 = 86;
	public static final int HEAT_GAUGE_Y1 = 106;
	public static final int HEAT_GAUGE_Y2 = 113;

	public static final int PROGRESS_X = 47;
	public static final int PROGRESS_Y = 57;

	public static final int PROGRESS_OVERLAY_X = 176;
	public static final int PROGRESS_OVERLAY_Y = 45;
	public static final int PROGRESS_WIDTH = 11;
	public static final int PROGRESS_HEIGHT = 20;


	public static int id;

	public final TileReactorAdvanced tile;

	public ToggleButton lock1;
	public ToggleButton lock2;
	public ToggleButton lock3;
	public ToggleButton lock4;
	public List<ToggleButton> lockButtons;
	public HoverButton heatIncr;
	public HoverButton heatDecr;

	public GuiReactor(EntityPlayer player, TileReactorAdvanced tile) {
		super(new ContainerReactor(player, tile), GUI_WIDTH, GUI_HEIGHT, TEXTURE_REACTOR_GUI);
		this.tile = tile;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();

		this.lock1 = new ToggleButton(this, id++, LOCK_X, LOCK_Y1, LOCK_OVERLAY_X1, LOCK_OVERLAY_Y, LOCK_WIDTH, LOCK_HEIGHT, TEXTURE_REACTOR_GUI);
		this.lock2 = new ToggleButton(this, id++, LOCK_X, LOCK_Y2, LOCK_OVERLAY_X2, LOCK_OVERLAY_Y, LOCK_WIDTH, LOCK_HEIGHT, TEXTURE_REACTOR_GUI);
		this.lock3 = new ToggleButton(this, id++, LOCK_X, LOCK_Y3, LOCK_OVERLAY_X3, LOCK_OVERLAY_Y, LOCK_WIDTH, LOCK_HEIGHT, TEXTURE_REACTOR_GUI);
		this.lock4 = new ToggleButton(this, id++, LOCK_X, LOCK_Y4, LOCK_OVERLAY_X4, LOCK_OVERLAY_Y, LOCK_WIDTH, LOCK_HEIGHT, TEXTURE_REACTOR_GUI);
		this.lockButtons = Arrays.asList(lock1, lock2, lock3, lock4);

		for (int i = 0; i < 4; i++) {
			lockButtons.get(i).setState(tile.getLockState(i));
		}

		this.heatIncr = new HoverButton(this, id++, INCR_X, INCR_Y, INCR_OVERLAY_X, INCR_OVERLAY_Y, HEAT_CTRL_WIDTH, HEAT_CTRL_HEIGHT, TEXTURE_REACTOR_GUI);
		this.heatDecr = new HoverButton(this, id++, DECR_X, DECR_Y, DECR_OVERLAY_X, DECR_OVERLAY_Y, HEAT_CTRL_WIDTH, HEAT_CTRL_HEIGHT, TEXTURE_REACTOR_GUI);

		this.heatIncr.setEnabled(tile.getHeatTier() != DCHeatTier.INFERNO);
		this.heatDecr.setEnabled(tile.getHeatTier() != DCHeatTier.ABSOLUTE);

		this.addButton(lock1);
		this.addButton(lock2);
		this.addButton(lock3);
		this.addButton(lock4);
		this.addButton(heatIncr);
		this.addButton(heatDecr);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		this.drawProgressBar();
		this.drawHeatTier();
		this.drawTankGauge(tile.tankInput1, TANK_X, TANK_Y1);
		this.drawTankGauge(tile.tankInput2, TANK_X, TANK_Y2);
		this.drawTankGauge(tile.tankOutput1, TANK_X, TANK_Y3);
		this.drawTankGauge(tile.tankOutput2, TANK_X, TANK_Y4);
	}

	protected void drawProgressBar() {
		int x = PROGRESS_X + guiLeft;
		int y = PROGRESS_Y + guiTop;
		float ratio = tile.progress / TileReactorAdvanced.TORQUE_PROCESS;
		int height = MathHelper.ceil(PROGRESS_HEIGHT * ratio);
		this.drawTexturedModalRect(x, y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, PROGRESS_WIDTH, height);
	}

	protected void drawHeatTier() {
		int x = HEAT_X + guiLeft + tile.getHeatTier().getTier() * HEAT_MOVE;
		int y = HEAT_Y + guiTop;
		this.drawTexturedModalRect(x, y, HEAT_BAR_X, HEAT_BAR_Y, HEAT_BAR_WIDTH, HEAT_BAR_HEIGHT);
	}

	@Override
	public void renderHoveredToolTip(int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
		int guiX = mouseX - getGuiLeft();
		int guiY = mouseY - getGuiTop();
		if (guiX >= TANK_X && guiX < TANK_X + TANK_GAUGE_WIDTH) {
			if (guiY >= TANK_Y1 && guiY < TANK_Y1 + TANK_GAUGE_HEIGHT) {
				this.drawHoveringText(this.getFluidToolTip(0), mouseX, mouseY);
			}
			if (guiY >= TANK_Y2 && guiY < TANK_Y2 + TANK_GAUGE_HEIGHT) {
				this.drawHoveringText(this.getFluidToolTip(1), mouseX, mouseY);
			}
			if (guiY >= TANK_Y3 && guiY < TANK_Y3 + TANK_GAUGE_HEIGHT) {
				this.drawHoveringText(this.getFluidToolTip(2), mouseX, mouseY);
			}
			if (guiY >= TANK_Y4 && guiY < TANK_Y4 + TANK_GAUGE_HEIGHT) {
				this.drawHoveringText(this.getFluidToolTip(3), mouseX, mouseY);
			}
		}
		if (guiX >= HEAT_GAUGE_X1 && guiX < HEAT_GAUGE_X2 && guiY >= HEAT_GAUGE_Y1 && guiY < HEAT_GAUGE_Y2) {
			this.drawHoveringText(tile.getHeatTier().localize(), mouseX, mouseY);
		}
	}

	private List<String> getFluidToolTip(int index) {
		FluidTank tank = tile.getTank(index);
		List<String> tips = new ArrayList<>();
		if (tank.getFluid() == null) {
			tips.add("Empty");
		} else {
			tips.add(tank.getFluid().getLocalizedName());
			tips.add(String.format("%d mb", tank.getFluidAmount()));
		}
		if (tile.getLockState(index)) {
			FluidStack stack = tile.getFilter(index);
			if (stack != null) {
				tips.add(String.format("Locked: %s", stack.getLocalizedName()));
			} else {
				tips.add("Locked");
			}
		}
		return tips;
	}

	protected void drawTankGauge(FluidTank tank, int x, int y) {
		int gauge_width = RenderUtil.calcRenderLength(tank.getFluidAmount(), tank.getCapacity(), TANK_GAUGE_WIDTH);
		this.drawFluidContents(tank.getFluid(), tank.getCapacity(), x, y, gauge_width, TANK_GAUGE_HEIGHT);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (lockButtons.contains(button)) {
			ToggleButton toggleButton = (ToggleButton) button;
			if (lockButtons.contains(toggleButton)) {
				toggleButton.toggle();
				int i = lockButtons.indexOf(toggleButton);
				tile.updateLock(i, toggleButton.getState());
			}
		}
		if (button == heatIncr) {
			tile.incrHeatTier();
			if (tile.getHeatTier() == DCHeatTier.INFERNO) heatIncr.disable();
			heatDecr.enable();
		}
		if (button == heatDecr) {
			tile.decrHeatTier();
			if (tile.getHeatTier() == DCHeatTier.ABSOLUTE) heatDecr.disable();
			heatIncr.enable();
		}
	}
}
