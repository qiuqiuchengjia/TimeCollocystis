package com.framework.greendroid.imagepicker.models;

/**
 * @ClassName: HotelDetailBean
 * @Description: TODO(酒店详情信息)
 * @author jdy
 * @date 2015年9月14日 上午10:47:24
 * 
 */
public class HotelDetailBean {
	private String title;//酒店名称
	private String tableCount;//酒桌数量
	private String hallShape;//大厅形状
	private String acreage;//酒店面积
	private String floorHeight;//酒店楼层高度
	private String pillarCount;//酒店柱子数量
	private String consumption;//每桌最低消费

	@Override
	public String toString() {
		return "HotelDetailBean [title=" + title + ", tableCount=" + tableCount
				+ ", hallShape=" + hallShape + ", acreage=" + acreage
				+ ", floorHeight=" + floorHeight + ", pillarCount="
				+ pillarCount + ", consumption=" + consumption + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTableCount() {
		return tableCount;
	}

	public void setTableCount(String tableCount) {
		this.tableCount = tableCount;
	}

	public String getHallShape() {
		return hallShape;
	}

	public void setHallShape(String hallShape) {
		this.hallShape = hallShape;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	public String getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(String floorHeight) {
		this.floorHeight = floorHeight;
	}

	public String getPillarCount() {
		return pillarCount;
	}

	public void setPillarCount(String pillarCount) {
		this.pillarCount = pillarCount;
	}

	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}
}
