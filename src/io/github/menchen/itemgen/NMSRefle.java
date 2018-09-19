package io.github.menchen.itemgen;

//import net.minecraft.server.v1_9_R1.5BlockPosition;
//import net.minecraft.server.v1_9_R1.NBTTagCompound;
//import net.minecraft.server.v1_9_R1.TileEntity;
//import net.minecraft.server.v1_9_R1.TileEntityMobSpawner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
//import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
//import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

public class NMSRefle {
    public static Class<?> CraftWorld;
    public static Class<?> NMSWorld;
    public static Class<?> TileEntityMobSpawner;
    public static Class<?> NBTTagCompound;
    public static Class<?> NMS_ItemStack;
    public static Constructor<?> BPCons;
    public static Constructor<?> NBTTagCompCons;
    public static Field NMS_ItemStack_countField;
    public static Method CW_getHandle;
    public static Method CW_getTileEntity;
    public static Method CI_asNMSCopy;
    public static Method CI_asBukkitCopy;
    public static Method MobSpawner_load;
    public static Method NBTTag_setString;
    public static Method NBTTag_setShort;
    public static Method NBTTag_setByte;
    public static Method NBTTag_set;
    public static Method NBTTag_remove;
    public static Method NMS_ItemStack_save;
    public static Method NMS_ItemStack_setTag;
    public static Method NMS_ItemStack_getName;
    public static Method MobSpawner_save;
    private static boolean haveInited = false;
    public static boolean useMobspawner_load = false;
    public static boolean useMobspawner_save = false;
    public static boolean useLegacyEntityId = false;

    public static boolean Init() {
        if (haveInited) {
            return true;
        }
        try {
            CraftWorld = getCraftBukkitClass("CraftWorld");
            NMSWorld = getNMSClass("World");
            NMS_ItemStack = getNMSClass("ItemStack");
            TileEntityMobSpawner = getNMSClass("TileEntityMobSpawner");
            NBTTagCompound = getNMSClass("NBTTagCompound");
            BPCons = getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class);
            NBTTagCompCons = getNMSClass("NBTTagCompound").getConstructor();
            NMS_ItemStack_countField = NMS_ItemStack.getDeclaredField("count");

            CW_getHandle = CraftWorld.getDeclaredMethod("getHandle");
            CW_getTileEntity = NMSWorld.getDeclaredMethod("getTileEntity", getNMSClass("BlockPosition"));
            CI_asNMSCopy = getCraftBukkitClass("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            CI_asBukkitCopy = getCraftBukkitClass("inventory.CraftItemStack").getDeclaredMethod("asBukkitCopy", NMS_ItemStack);
            NBTTag_setString = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
            NBTTag_setShort = NBTTagCompound.getDeclaredMethod("setShort", String.class, short.class);
            NBTTag_setByte = NBTTagCompound.getDeclaredMethod("setByte", String.class, byte.class);
            NBTTag_set = NBTTagCompound.getDeclaredMethod("set", String.class, getNMSClass("NBTBase"));
            NBTTag_remove = NBTTagCompound.getDeclaredMethod("remove", String.class);
            NMS_ItemStack_save = NMS_ItemStack.getDeclaredMethod("save", NBTTagCompound);
            NMS_ItemStack_setTag = NMS_ItemStack.getDeclaredMethod("setTag", NBTTagCompound);
            NMS_ItemStack_getName = NMS_ItemStack.getDeclaredMethod("getName");
            haveInited = true;
        } catch (Exception e) {
            getLogger().severe(e.toString());
            return false;
        }
        try {
            MobSpawner_save = TileEntityMobSpawner.getDeclaredMethod("save", NBTTagCompound);
            useMobspawner_save = true;
        } catch (NoSuchMethodException e) {
            getLogger().info("Not after 1.9 server, trying pre 1.9 method");
            try {
                MobSpawner_save = TileEntityMobSpawner.getDeclaredMethod("b", NBTTagCompound);
                useMobspawner_save = false;
            } catch (NoSuchMethodException _e) {
                getLogger().info("Pre 1.9 method not working, please contact plugin author at Spigot.");
                return false;
            }
        }
        try {
            MobSpawner_load = TileEntityMobSpawner.getDeclaredMethod("load", NBTTagCompound);
            useMobspawner_load = true;
        } catch (NoSuchMethodException e) {
            getLogger().info("After 1.10 method not found.Trying Pre 1.10 method");
            try {
                MobSpawner_load = TileEntityMobSpawner.getDeclaredMethod("a", NBTTagCompound);
                useMobspawner_load = false;
            } catch (NoSuchMethodException _e) {
                getLogger().severe("Pre 1.10 method not working, please contact plugin author at Spigot.");
            }
        }
        return true;
    }

    private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        //private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }

    private static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
        //private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }

    @SuppressWarnings("Duplicates")
    public boolean setExpSpawner(Block block, int delay, int range, int srange, int value, int maxcount) {
        try {
            Object cfWorld = CW_getHandle.invoke(CraftWorld.cast(block.getWorld()));
            Object tileEntity = CW_getTileEntity.invoke(cfWorld, BPCons.newInstance(block.getX(), block.getY(), block.getZ()));

            if (TileEntityMobSpawner.isInstance(tileEntity)) {
                Object mobSpawner = TileEntityMobSpawner.cast(tileEntity);
                //TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner) tileEntity;
                Object spawnerTag = NBTTagCompCons.newInstance();
                //NBTTagCompound spawnerTag = new NBTTagCompound();
                MobSpawner_save.invoke(mobSpawner, spawnerTag);
                NBTTag_remove.invoke(spawnerTag, "SpawnPotentials");
                //MobSpawner_save.invoke(mobSpawner, spawnerTag);
                //mobSpawner.save(spawnerTag);
                NBTTag_setString.invoke(spawnerTag, "EntityId", "XPOrb");
                //spawnerTag.setString("EntityId", "Item");
                Object spawnData = NBTTagCompCons.newInstance();
                //NBTTagCompound itemTag = new NBTTagCompound();
                NBTTag_setShort.invoke(spawnData, "Age", (short) 0);
                //itemTag.setShort("Age", (short) 0);
                NBTTag_setShort.invoke(spawnData, "Health", (short) 5);
                // 1.9+ Fix, a change of minecraft NBT Data.
                NBTTag_setString.invoke(spawnData, "id", "minecraft:xp_orb");
                NBTTag_setShort.invoke(spawnData, "Value", (short) value);
                NBTTag_set.invoke(spawnerTag, "SpawnData", spawnData);
                //spawnerTag.set("SpawnData", itemTag);
                NBTTag_setShort.invoke(spawnerTag, "SpawnCount", (short) 1);
                NBTTag_setShort.invoke(spawnerTag, "MinSpawnDelay", (short) (delay * 20));
                //spawnerTag.setShort("MinSpawnDelay", (short) (delay * 20));
                NBTTag_setShort.invoke(spawnerTag, "MaxSpawnDelay", (short) (delay * 20));
                //spawnerTag.setShort("MaxSpawnDelay", (short) (delay * 20));
                NBTTag_setShort.invoke(spawnerTag, "MaxNearbyEntities", (short) maxcount);
                //spawnerTag.setShort("MaxNearbyEntities", (short) player.getItemInHand().getAmount());
                NBTTag_setShort.invoke(spawnerTag, "RequiredPlayerRange", (short) (range));
                //spawnerTag.setShort("RequiredPlayerRange", (short) (ranger));
                NBTTag_setShort.invoke(spawnerTag, "SpawnRange", (short) (srange));
                //spawnerTag.setShort("SpawnRange", (short) (sranger));

                MobSpawner_load.invoke(mobSpawner, spawnerTag);
                //mobSpawner.a(spawnerTag);

                ItemGen.metrics.addCustomChart(new Metrics.SimplePie("itemmaterial", () -> "EXPOrb"));
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean getSpawner(Block block,Player player){
        try {


        Object cfWorld = CW_getHandle.invoke(CraftWorld.cast(block.getWorld()));
        Object tileEntity = CW_getTileEntity.invoke(cfWorld, BPCons.newInstance(block.getX(), block.getY(), block.getZ()));
        if (TileEntityMobSpawner.isInstance(tileEntity)) {
            Object mobSpawner = TileEntityMobSpawner.cast(tileEntity);
            Object spawnerTag = NBTTagCompCons.newInstance();
            MobSpawner_save.invoke(mobSpawner, spawnerTag);
            Object itemTag = NBTTagCompCons.newInstance();
            NBTTag_set.invoke(itemTag,"BlockEntityTag",spawnerTag);
            Object item = CI_asNMSCopy.invoke(null,new ItemStack(ItemGen.SpawnerMat));
            NMS_ItemStack_setTag.invoke(item,itemTag);
            ItemStack spawneritem = (ItemStack) CI_asBukkitCopy.invoke(null,item);
            ItemMeta im =spawneritem.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            lore.add((new StringBuilder().append(ChatColor.DARK_PURPLE).append(ChatColor.ITALIC).append("(+NBT)")).toString());
            im.setLore(lore);
            im.addEnchant(Enchantment.LURE,1,false);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            spawneritem.setItemMeta(im);
            player.getInventory().addItem(spawneritem);



        }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // Full reflection
    @SuppressWarnings("deprecation")
    public String[] setspawner(Block block, Player player, int delay, int ranger, int sranger, ItemStack handItem) {
        String[] x = new String[2];
        if (!haveInited) {
            Init();
        }

        try {
            Object cfWorld = CW_getHandle.invoke(CraftWorld.cast(block.getWorld()));
            Object tileEntity = CW_getTileEntity.invoke(cfWorld, BPCons.newInstance(block.getX(), block.getY(), block.getZ()));
            //TileEntity tileEntity = craftWorld.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            Object itemStack = CI_asNMSCopy.invoke(null, handItem);
            x[0] = handItem.getType().toString();
            //net.minecraft.server.v1_9_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(player.getItemInHand());
            if (TileEntityMobSpawner.isInstance(tileEntity)) {
                Object mobSpawner = TileEntityMobSpawner.cast(tileEntity);
                //TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner) tileEntity;
                Object spawnerTag = NBTTagCompCons.newInstance();
                //NBTTagCompound spawnerTag = new NBTTagCompound();
                MobSpawner_save.invoke(mobSpawner, spawnerTag);
                //Not tested..
                if (delay == -1) {
                    NBTTag_remove.invoke(spawnerTag, "SpawnPotentials");
                    //MobSpawner_save.invoke(mobSpawner, spawnerTag);
                    //mobSpawner.save(spawnerTag);
                    NBTTag_setString.invoke(spawnerTag, "EntityId", "Item");
                    //spawnerTag.setString("EntityId", "Item");
                    Object itemTag = NBTTagCompCons.newInstance();
                    //NBTTagCompound itemTag = new NBTTagCompound();
                    NBTTag_setShort.invoke(itemTag, "Age", (short) 0);
                    //itemTag.setShort("Age", (short) 0);
                    NBTTag_setShort.invoke(itemTag, "Health", (short) 5);
                    // 1.9+ Fix, a change of minecraft NBT Data.
                    NBTTag_setString.invoke(itemTag, "id", "minecraft:item");
                    //itemTag.setShort("Health", (short) 5);
                    Object itemStackTag = NBTTagCompCons.newInstance();
                    //NBTTagCompound itemStackTag = new NBTTagCompound();
                    NMS_ItemStack_save.invoke(itemStack, itemStackTag);
                    //itemStack.save(itemStackTag);
                    NBTTag_setByte.invoke(itemStackTag, "Count", (byte) 1);
                    //itemStackTag.setByte("Count", (byte) 1);
                    NBTTag_set.invoke(itemTag, "Item", itemStackTag);
                    //itemTag.set("Item", itemStackTag);
                    NBTTag_set.invoke(spawnerTag, "SpawnData", itemTag);
                    //spawnerTag.set("SpawnData", itemTag);
                    NBTTag_setShort.invoke(spawnerTag, "SpawnCount", (short) (handItem.getAmount()));
                    //spawnerTag.setShort("SpawnCount", (short) itemStack.count);
                    //getLogger().warning("Debug itemStack count");
                    //getLogger().warning(NMS_ItemStack_countField.get(itemStack).toString());
                    NBTTag_setShort.invoke(spawnerTag, "SpawnRange", (short) (1));
                    //spawnerTag.setShort("SpawnRange", (short) (2));

                } else {
                    NBTTag_setShort.invoke(spawnerTag, "Delay", (short) -1);
                    //spawnerTag.setShort("Delay", (short) 0);
                    NBTTag_setShort.invoke(spawnerTag, "MinSpawnDelay", (short) (delay * 20));
                    //spawnerTag.setShort("MinSpawnDelay", (short) (delay * 20));
                    NBTTag_setShort.invoke(spawnerTag, "MaxSpawnDelay", (short) (delay * 20));
                    //spawnerTag.setShort("MaxSpawnDelay", (short) (delay * 20));
                    NBTTag_setShort.invoke(spawnerTag, "MaxNearbyEntities", (short) (handItem.getAmount()));
                    //spawnerTag.setShort("MaxNearbyEntities", (short) player.getItemInHand().getAmount());
                    NBTTag_setShort.invoke(spawnerTag, "RequiredPlayerRange", (short) (ranger));
                    //spawnerTag.setShort("RequiredPlayerRange", (short) (ranger));
                    NBTTag_setShort.invoke(spawnerTag, "SpawnRange", (short) (sranger));
                    //spawnerTag.setShort("SpawnRange", (short) (sranger));
                }
                MobSpawner_load.invoke(mobSpawner, spawnerTag);
                //mobSpawner.a(spawnerTag);

                ItemGen.metrics.addCustomChart(new Metrics.SimplePie("itemmaterial", () -> x[0]));

            } else {
                x[1] = "true";
            }
        } catch (Exception e) {
            getLogger().severe("Something wrong have happened with ItemGen! (NMSReflection)");
            getLogger().severe(e.toString());
            e.printStackTrace();
        }
        x[1] = "false";
        return x;
    }
}

