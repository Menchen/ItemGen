package io.github.menchen.itemgen;

import net.minecraft.server.v1_7_R4.TileEntityMobSpawner;
import net.minecraft.server.v1_7_R4.TileEntity;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.server.v1_7_R4.NBTTagCompound;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class NMS17_R4 {

	public String[] setspawner(Block block, Player player, int delay, int ranger, int sranger) {
		String[] x = new String[2];
		World world = ((CraftWorld) block.getWorld()).getHandle();
		TileEntity tileEntity = world.getTileEntity(block.getX(), block.getY(), block.getZ());
		net.minecraft.server.v1_7_R4.ItemStack itemStack = CraftItemStack.asNMSCopy(player.getItemInHand());
		if (tileEntity instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner) tileEntity;
			NBTTagCompound spawnerTag = new NBTTagCompound();
			mobSpawner.b(spawnerTag);
			if (delay == -1) {
				spawnerTag.remove("SpawnPotentials");
				spawnerTag.setString("EntityId", "Item");
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setShort("Age", (short) 0);
				itemTag.setShort("Health", (short) 5);
				NBTTagCompound itemStackTag = new NBTTagCompound();
				itemStack.save(itemStackTag);
				itemStackTag.setByte("Count", (byte) 1);
				itemTag.set("Item", itemStackTag);
				spawnerTag.set("SpawnData", itemTag);
				spawnerTag.setShort("SpawnCount", (short) itemStack.count);
				spawnerTag.setShort("SpawnRange", (short) (2));

			} else {
				spawnerTag.setShort("Delay", (short) 0);
				spawnerTag.setShort("MinSpawnDelay", (short) (delay * 20));
				spawnerTag.setShort("MaxSpawnDelay", (short) (delay * 20));
				spawnerTag.setShort("MaxNearbyEntities", (short) player.getItemInHand().getAmount());
				spawnerTag.setShort("RequiredPlayerRange", (short) (ranger));
				spawnerTag.setShort("SpawnRange", (short) (sranger));
			}
			mobSpawner.a(spawnerTag);
		} else {
			x[1] = "true";
		}
		x[0] = itemStack.getName();
		x[1] = "false";
		return x;
	}
}
