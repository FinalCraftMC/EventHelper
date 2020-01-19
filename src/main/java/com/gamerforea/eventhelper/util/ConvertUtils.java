package com.gamerforea.eventhelper.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ConvertUtils
{
	private static final Method getBukkitEntity;
	private static final Method asCraftMirror;

	private static final Field getMinecraftEntity;
	private static final Field getMinecraftWorld;
	private static final Field getMinecraftItemStack;

	public static Entity toMinecraftEntity(org.bukkit.entity.Entity bEntity)
	{
		try {
			return (Entity)getMinecraftEntity.get(bEntity);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static WorldServer toMinecraftWorld(org.bukkit.World bWorld)
	{
		try {
			return (WorldServer) getMinecraftWorld.get(bWorld);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static ItemStack toMinecraftItemStack(org.bukkit.inventory.ItemStack bItemStack)
	{
		try {
			return (ItemStack) getMinecraftItemStack.get(bItemStack);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static org.bukkit.entity.Entity toBukkitEntity(Entity entity)
	{
		try {
			return (org.bukkit.entity.Entity) getBukkitEntity.invoke(entity);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static Player toBukkitEntity(EntityPlayer player)
	{
		try {
			return (Player) getBukkitEntity.invoke(player);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static org.bukkit.World toBukkitWorld(World world)
	{
		return Bukkit.getWorld(world.getWorldInfo().getWorldName());
	}

	public static org.bukkit.inventory.ItemStack toBukkitItemStackMirror(ItemStack stack)
	{
		try {
			return (org.bukkit.inventory.ItemStack) asCraftMirror.invoke(null, stack);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static BlockFace toBukkitFace(ForgeDirection direction)
	{
		switch (direction)
		{
			case DOWN:
				return BlockFace.DOWN;
			case UP:
				return BlockFace.UP;
			case NORTH:
				return BlockFace.NORTH;
			case SOUTH:
				return BlockFace.SOUTH;
			case WEST:
				return BlockFace.WEST;
			case EAST:
				return BlockFace.EAST;
			case UNKNOWN:
				return BlockFace.SELF;
			default:
				return BlockFace.SELF;
		}
	}

	static
	{
		try
		{
			getBukkitEntity = Entity.class.getDeclaredMethod("getBukkitEntity");
			getBukkitEntity.setAccessible(true);

			getMinecraftEntity = CraftUtils.getCraftClass("entity.CraftEntity").getDeclaredField("entity");
			getMinecraftEntity.setAccessible(true);

			getMinecraftWorld = CraftUtils.getCraftClass("CraftWorld").getDeclaredField("world");
			getMinecraftWorld.setAccessible(true);

			getMinecraftItemStack = CraftUtils.getCraftClass("inventory.CraftItemStack").getDeclaredField("handle");
			getMinecraftItemStack.setAccessible(true);

			asCraftMirror = CraftUtils.getCraftClass("inventory.CraftItemStack").getDeclaredMethod("asCraftMirror", ItemStack.class);
			asCraftMirror.setAccessible(true);
		}
		catch (Throwable throwable)
		{
			throw new RuntimeException("Failed hooking CraftBukkit methods!", throwable);
		}
	}
}
