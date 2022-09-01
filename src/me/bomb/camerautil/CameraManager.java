package me.bomb.camerautil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class CameraManager {
	
	private static final CameraManager cameramanager;
	
	static {
		switch (Bukkit.getServer().getClass().getPackage().getName().substring(23)) {
		case "v1_19_R1":
			cameramanager = new CameraManager_v1_19_R1();
			break;
		case "v1_18_R2":
			cameramanager = new CameraManager_v1_18_R2();
			break;
		case "v1_17_R1":
			cameramanager = new CameraManager_v1_17_R1();
			break;
		case "v1_16_R3":
			cameramanager = new CameraManager_v1_16_R3();
			break;
		case "v1_15_R1":
			cameramanager = new CameraManager_v1_15_R1();
			break;
		case "v1_14_R1":
			cameramanager = new CameraManager_v1_14_R1();
			break;
		case "v1_13_R2":
			cameramanager = new CameraManager_v1_13_R2();
			break;
		case "v1_12_R1":
			cameramanager = new CameraManager_v1_12_R1();
			break;
		case "v1_11_R1":
			cameramanager = new CameraManager_v1_11_R1();
			break;
		case "v1_10_R1":
			cameramanager = new CameraManager_v1_10_R1();
			break;
		case "v1_9_R2":
			cameramanager = new CameraManager_v1_9_R2();
			break;
		case "v1_8_R3":
			cameramanager = new CameraManager_v1_8_R3();
			break;
		default:
			cameramanager = null;
		}
	}
	
	protected static Map<UUID, CameraData> cameradata = new HashMap<UUID, CameraData>();
	
	public static final void put(Player player,LocationPoint currentlocation,CameraType cameratype,boolean hideinventory,boolean hideinterface) {
		if(player==null||currentlocation==null||cameratype==null) return;
		if(cameradata.containsKey(player.getUniqueId())) {
			CameraData data = cameradata.get(player.getUniqueId());
			data.currentlocation = currentlocation;
			data.cameratype = cameratype;
			cameramanager.updateCameraType(player);
			return;
		}
		cameradata.put(player.getUniqueId(),new CameraData(currentlocation,null,cameratype,hideinventory,hideinterface));
		cameramanager.spawnCamera(player);
	}
	
	public static final LocationPoint getFirstLocationPoint(Player player) {
		return player!=null && cameradata.containsKey(player.getUniqueId()) ? cameradata.get(player.getUniqueId()).firstlocation : null;
	}
	
	public static final LocationPoint getPreviousLocationPoint(Player player) {
		return player!=null && cameradata.containsKey(player.getUniqueId()) ? cameradata.get(player.getUniqueId()).previouslocation : null;
	}
	
	public static final LocationPoint getCurrentLocationPoint(Player player) {
		return player!=null && cameradata.containsKey(player.getUniqueId()) ? cameradata.get(player.getUniqueId()).currentlocation : null;
	}
	
	public static final CameraType getCameraType(Player player) {
		return player!=null && cameradata.containsKey(player.getUniqueId()) ? cameradata.get(player.getUniqueId()).cameratype : null;
	}
	
	public static final void setLocationPoint(Player player,LocationPoint currentlocation) {
		if(player==null||currentlocation==null||!cameradata.containsKey(player.getUniqueId())) return;
		CameraData data = cameradata.get(player.getUniqueId());
		if(data.currentlocation.hasMove(currentlocation)) {
			data.previouslocation = data.currentlocation;
			data.currentlocation = currentlocation;
		}
		cameramanager.updateCameraLocation(player);
	}
	
	public static final void setCameraType(Player player,CameraType cameratype) {
		if(player==null||cameratype==null||!cameradata.containsKey(player.getUniqueId())) return;
		cameradata.get(player.getUniqueId()).cameratype = cameratype;
		cameramanager.updateCameraType(player);
	}
	
	public static final Set<UUID> keySet() {
		return cameradata.keySet();
	}
	
	public static final boolean contains(Player player) {
		return player!=null && cameradata.containsKey(player.getUniqueId());
	}
	
	public static final void remove(Player player) {
		if(player==null) return;
		boolean online = player.isOnline();
		if(online) cameramanager.despawnCamera(player);
		cameradata.remove(player.getUniqueId());
		if(online) cameramanager.restore(player);
	}
	
	public static final void registerHandler(Player player) {
		if(player==null) return;
		cameramanager.register(player);
	}
	
	public static final void unregisterHandler(Player player) {
		if(player==null) return;
		cameramanager.unregister(player);
	}
	
	protected abstract void register(Player player);
	protected abstract void unregister(Player player);
	protected abstract void spawnCamera(Player player);
	protected abstract void updateCameraType(Player player);
	protected abstract void updateCameraLocation(Player player);
	protected abstract void despawnCamera(Player player);
	protected abstract void restore(Player player);
	
	static final class CameraData {
		protected LocationPoint firstlocation;
		protected LocationPoint previouslocation;
		protected LocationPoint currentlocation;
		protected Object cameraentity;
		protected CameraType cameratype;
		protected boolean hideinventory;
		protected boolean hideinterface;
		private CameraData(LocationPoint currentlocation,Object cameraentity,CameraType cameratype,boolean hideinventory,boolean hideinterface) {
			this.firstlocation = currentlocation;
			this.previouslocation = currentlocation;
			this.currentlocation = currentlocation;
			this.cameraentity = cameraentity;
			this.cameratype = cameratype;
			this.hideinventory = hideinventory;
			this.hideinterface = hideinterface;
		}
	}
	
}