package skytheory.hap.tile;

import net.minecraft.util.EnumFacing;

/**
 * setTorqueはTileの初期化や同期などの際にのみ用いること<br>
 * 機械の加工などで参照する際はgetTorqueが利用できる<br>
 * トルク量の受け渡しの計算の際にはset/getNextTorqueで行えばOK
 * @author SkyTheory
 *
 */
public interface ITorqueTile {

	/** 対象の方位が入力面かどうかを返す */
	public boolean isInputSide(EnumFacing facing);

	/** 対象の方位が出力面かどうかを返す */
	public boolean isOutputSide(EnumFacing facing);

	/** トルクの量を初期化する */
	public void setTorque(float amount);

	/** 次の更新のためにストックするトルクの量を設定する */
	public void setNextTorque(float amount);

	/** トルクの量を取得する */
	public float getTorque();

	/** 次の更新のためにストックされているトルクの量を取得する */
	public float getNextTorque();

	/** 減衰率を取得する */
	public float getFriction();

	/** トルクの最大値を取得する */
	public float getMaxTorque();

}
