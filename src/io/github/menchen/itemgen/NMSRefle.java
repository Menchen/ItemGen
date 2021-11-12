package io.github.menchen.itemgen;

import io.github.menchen.itemgen.Wrapper.NBTCompound;
import io.github.menchen.itemgen.Wrapper.NMSItemStack;
import io.github.menchen.itemgen.Wrapper.NMSSpawner;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;


public class NMSRefle {


    public boolean setThrowsSpawner(Block block){
        try {
            NBTCompound spawnData = new NBTCompound();
            spawnData.put("Age",(short) 0);
            spawnData.put("Health",(short) 5);
            spawnData.put("id","minecraft:xp_orb");

            setSpawner(block,"XPOrb",spawnData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setSpawner(Block block,short delay, short range, short srange, short maxcount,short spawnCount) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        NMSSpawner tileEntity = new NMSSpawner(block);
        if (tileEntity.isInstanceOf()) {
            NBTCompound spawnerTag = new NBTCompound();
            tileEntity.save(spawnerTag);
            if (spawnCount >= 0) NMSSpawner.setSpawnCount(spawnerTag,spawnCount);
            if (delay >= -1) NMSSpawner.setSpawnMinDelay(spawnerTag, (short) (delay*20));
            if (delay >= -1) NMSSpawner.setSpawnMaxDelay(spawnerTag, (short) (delay*20));
            if (maxcount >= 0) NMSSpawner.setMaxNerbyEntity(spawnerTag,maxcount);
            if (range >= -1) NMSSpawner.setRequirePlayerRange(spawnerTag,range);
            if (srange >= -1) NMSSpawner.setSpawnRange(spawnerTag,srange);

            tileEntity.load(spawnerTag);
        }
    }
    public void setSpawner(Block block,String legacyName,NBTCompound spawnDataNbt)throws IllegalAccessException, InstantiationException, InvocationTargetException {
        NMSSpawner tileEntity = new NMSSpawner(block);

        if (tileEntity.isInstanceOf()) {
            NBTCompound spawnerTag = new NBTCompound();
            tileEntity.save(spawnerTag);
            NMSSpawner.removeSpawnPotentials(spawnerTag);
            NMSSpawner.setEntityId(spawnerTag,legacyName);
            NMSSpawner.setSpawnData(spawnerTag,spawnDataNbt);

            tileEntity.load(spawnerTag);

            ItemGen.metrics.addCustomChart(new Metrics.SimplePie("itemmaterial", () -> legacyName));
        }
    }
    public boolean setExpSpawner(Block block, int value) {
        try {
                NBTCompound spawnData = new NBTCompound();
                spawnData.put("Age",(short) 0);
                spawnData.put("Health",(short) 5);
                spawnData.put("Value",(short) value);
                spawnData.put("id","minecraft:xp_orb");

                setSpawner(block,"XPOrb",spawnData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ItemStack getSpawnerItem(Block block){
        try {
            NMSSpawner spawner = new NMSSpawner(block);
            if (spawner.isInstanceOf()){
                NBTCompound spawnerTag = new NBTCompound();
                spawner.save(spawnerTag);
                NBTCompound itemTag = new NBTCompound();
                itemTag.put("BlockEntityTag",spawnerTag);
                NMSItemStack nmsItemStack = new NMSItemStack(new ItemStack(ItemGen.SpawnerMat));
                nmsItemStack.setTag(itemTag);
                ItemStack spawneritem = nmsItemStack.asBukkitCopy();
                ItemMeta im = spawneritem.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add((new StringBuilder().append(ChatColor.DARK_PURPLE).append(ChatColor.ITALIC).append("(+NBT)")).toString());
                im.setLore(lore);
                im.addEnchant(Enchantment.LURE,1,false);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                spawneritem.setItemMeta(im);
                return spawneritem;
        }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean setItemSpawner(Block block, Player player, ItemStack handItem, short age, short health) {
        try {
            NMSItemStack nmsItemStack = new NMSItemStack(handItem);

            NBTCompound itemTag = new NBTCompound();
            itemTag.put("Age",age);
            itemTag.put("Health", health);
            itemTag.put("id","minecraft:item");

            NBTCompound itemStackTag = new NBTCompound();
            nmsItemStack.save(itemStackTag);
            itemStackTag.put("Count",(byte) handItem.getAmount());
            itemTag.put("Item",itemStackTag);

            setSpawner(block,"Item",itemTag);

            ItemGen.metrics.addCustomChart(new Metrics.SimplePie("itemmaterial", () -> handItem.getType().name() ));

        } catch (Exception e) {
            getLogger().severe("Something wrong have happened with ItemGen! (NMSReflection)");
            getLogger().severe(e.toString());
            e.printStackTrace();
        }
        return false;
    }
}

