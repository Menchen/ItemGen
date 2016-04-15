package io.github.menchen.itemgen;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.TileEntity;
import net.minecraft.server.v1_9_R1.TileEntityMobSpawner;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class NMS19_R1 {

	public String[] setspawner(Block block, Player player, int delay, int ranger, int sranger) {
		String[] x = new String[2];
		World world = ((CraftWorld) block.getWorld()).getHandle();
		TileEntity tileEntity = world.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		net.minecraft.server.v1_9_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(player.getItemInHand());
		if (tileEntity instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner) tileEntity;
			NBTTagCompound spawnerTag = new NBTTagCompound();
			mobSpawner.save(spawnerTag);
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
