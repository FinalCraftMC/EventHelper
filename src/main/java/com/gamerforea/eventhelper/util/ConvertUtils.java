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

	public static Entity toMinecraftEntity(org.bukkit.entity.Entity bEntity) throws Exception
	{
		return (Entity)getMinecraftEntity.get(bEntity);
	}

	public static WorldServer toMinecraftWorld(org.bukkit.World bWorld) throws Exception
	{
		return (WorldServer) getMinecraftWorld.get(bWorld);
	}

	public static ItemStack toMinecraftItemStack(org.bukkit.inventory.ItemStack bItemStack) throws Exception
	{
		return (ItemStack) getMinecraftItemStack.get(bItemStack);
	}

	public static org.bukkit.entity.Entity toBukkitEntity(Entity entity) throws Exception
	{
		return (org.bukkit.entity.Entity) getBukkitEntity.invoke(entity);
	}

	public static Player toBukkitEntity(EntityPlayer player) throws Exception
	{
		return (Player) getBukkitEntity.invoke(player);
	}

	public static org.bukkit.World toBukkitWorld(World world)
	{
		return Bukkit.getWorld(world.getWorldInfo().getWorldName());
	}

	public static org.bukkit.inventory.ItemStack toBukkitItemStackMirror(ItemStack stack) throws Exception
	{
		return (org.bukkit.inventory.ItemStack) asCraftMirror.invoke(null, stack);
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

			getMinecraftEntity = Class.forName("org.bukkit.craftbukkit.entity.CraftEntity").getField("entity");
			getMinecraftEntity.setAccessible(true);

			getMinecraftWorld = Class.forName("org.bukkit.craftbukkit.CraftWorld").getField("world");
			getMinecraftWorld.setAccessible(true);

			getMinecraftItemStack = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack").getField("handle");
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
