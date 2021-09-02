package skytheory.hap.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import skytheory.hap.block.BlockShaftBase;

public abstract class TileShaftBase extends TileTorqueRotational {

	/** レンダー用加速度係数 この値が小さいほど緩やかに加速する */
	// 覚書：HaCより緩やかに加速する。0.2を指定しても良かったけれど、好みの問題でこちらに。
	public static final float ACCEL_COEFFICIENT = 0.01f;
	/** レンダー用速度係数 この値の絶対値が大きいほど同じトルクでも高速回転する*/
	// 覚書：-32.0f辺りの数値を指定しておけばHaCよりちょっと遅いくらいの速度で回る
	public static final float SPEED_COEFFICIENT = -32.0f;
	// スピードの最低値、これを下回ると停止する
	public static final float SPEED_GATE = 0.01f;

	// 現在の角度
	protected float shaftAngleInput = 0.0f;
	protected float shaftAngleOutput = 0.0f;

	// 現在の速度
	protected float shaftSpeed = 0.0f;

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			this.updateAngle();
			this.updateSpeed();
		}
	}

	@Override
	public float getMaxTorque() {
		switch(this.getTorqueTier()) {
		case 2:
			return 128.0f;
		case 3:
			return 512.0f;
		default:
			return Float.MAX_VALUE;
		}
	}

	public int getTorqueTier() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockShaftBase) {
			return ((BlockShaftBase) block).torqueTier;
		}
		return 0;
	}

	// Render用にシャフトの角度と差分を取得する
	public void updateAngle() {
		// 現在の角度を求める
		shaftAngleInput += shaftSpeed;
		shaftAngleInput = shaftAngleInput % 360.0f;
		shaftAngleOutput += shaftSpeed * this.getFriction();
		shaftAngleOutput = shaftAngleOutput % 360.0f;
	}
	public void updateSpeed() {
		float prevSpeed = shaftSpeed;
		// この速度を目標に加減速する
		float targetSpeed = (this.getTorque() / this.getMaxTorque()) * SPEED_COEFFICIENT;
		// 前回の速度との差分を求めて、補正後の値を出す
		float accelDelta = (targetSpeed - shaftSpeed) * ACCEL_COEFFICIENT;
		// 現在の速度に追加する
		shaftSpeed = prevSpeed += accelDelta;
		// 速度が小さければ停止する
		if (Math.abs(shaftSpeed) < SPEED_GATE) shaftSpeed = 0.0f;
	}

	public float getShaftAngleInput() {
		return shaftAngleInput;
	}

	public float getShaftDeltaInput() {
		return shaftSpeed;
	}

	public float getShaftAngleOutput() {
		return shaftAngleOutput;
	}

	public float getShaftDeltaOutput() {
		return shaftSpeed * this.getFriction();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

}
