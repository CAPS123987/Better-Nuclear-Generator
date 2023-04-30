package me.CAPS123987.Utils;

import java.util.Map;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class Methodes {
	public static void areaList(int cornerX1 ,int cornerY1, int cornerZ1,int cornerX2, int cornerY2, int cornerZ2, SlimefunItemStack item, Map<Vector, SlimefunItemStack> list) {
		for(int X = cornerX1; X != cornerX2+1; X++ ) {
			for(int Y = cornerY1; Y != cornerY2+1; Y++ ) {
				for(int Z = cornerZ1; Z != cornerZ2+1; Z++ ) {
					list.put(new Vector(X,Y,Z), item);
					
				}
			}
		}
	}
	public static int fac(BlockFace f) {
		if(f.equals(BlockFace.SOUTH)) {
			return 0;
		}else if(f.equals(BlockFace.NORTH)){
			return 2;
		}else if(f.equals(BlockFace.WEST)){
			return 1;
		}else{
			return 3;
		}
		
	}
	public static Vector rotVector(Vector v, int i) {
		Vector newV = v.clone();
		double X = newV.getX();
		double Z = newV.getZ();
		switch(i) {
			case 0:
				newV.setX(X);
				newV.setZ(Z*-1.0);
				return newV;
				
			case 2:
				newV.setX(X*-1);
				newV.setZ(Z);
				return newV;
				
			case 1:
				newV.setX(Z);
				newV.setZ(X);
				return newV;
				
			case 3:
				newV.setX(Z*-1);
				newV.setZ(X*-1);
				return newV;
				
			default:
				return newV;
		}
		
	}
	public static int toInt(String s) {
		return Integer.parseInt(s);
	}
}
