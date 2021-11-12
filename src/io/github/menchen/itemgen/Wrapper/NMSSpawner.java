package io.github.menchen.itemgen.Wrapper;

import org.bukkit.block.Block;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class NMSSpawner extends NMSBlock{

    private static boolean inited = false;
    private static Class<?> mobSpawnerTileEntitycls;
    private static Method MobSpawner_save;
    private static Method MobSpawner_load;
    private static boolean useMobspawner_save;
    private static boolean useMobspawner_load;

    private static void init(){
        mobSpawnerTileEntitycls = ReflectionManager.getOrCreateClass("TileEntityMobSpawner",ReflectionManager.ClassType.NMS);
        MobSpawner_save = ReflectionManager.getOrCreateMethod(mobSpawnerTileEntitycls,"save",NBTCompound.getBaseClass());
        if (MobSpawner_save != null) {
            useMobspawner_save = true;
        }else {
            org.bukkit.Bukkit.getLogger().info("Not after 1.9 server, trying pre 1.9 method");
            MobSpawner_save = ReflectionManager.getOrCreateMethod(mobSpawnerTileEntitycls, "b", NBTCompound.getBaseClass());

            if (MobSpawner_save != null) {
                useMobspawner_save = false;
            }else {
                org.bukkit.Bukkit.getLogger().info("Pre 1.9 method not working, please contact plugin author at Spigot.");
            }
        }
        MobSpawner_load = ReflectionManager.getOrCreateMethod(mobSpawnerTileEntitycls,"load",NBTCompound.getBaseClass());
        if (MobSpawner_load != null) {
            useMobspawner_load = true;
        }else {
            org.bukkit.Bukkit.getLogger().info("After 1.10 method not found.Trying Pre 1.10 method");
            MobSpawner_load = ReflectionManager.getOrCreateMethod(mobSpawnerTileEntitycls, "a", NBTCompound.getBaseClass());

            if (MobSpawner_load != null) {
                useMobspawner_load = false;
            }else {
                org.bukkit.Bukkit.getLogger().severe("Pre 1.10 method not working, please contact plugin author at Spigot.");
            }
        }
        inited = true;
    }
    public static boolean isObjectInstanceOf (Object obj){
        return mobSpawnerTileEntitycls.isInstance(obj);
    }

    public boolean isInstanceOf(){
        return mobSpawnerTileEntitycls.isInstance(base);
    }
    public void save(NBTCompound nbt) throws InvocationTargetException, IllegalAccessException {
        MobSpawner_save.invoke(base,nbt.getBase());
    }
    public void load(NBTCompound nbt) throws InvocationTargetException, IllegalAccessException {
        MobSpawner_load.invoke(base,nbt.getBase());
    }

    public static void removeSpawnPotentials(NBTCompound base) throws InvocationTargetException, IllegalAccessException {
        base.remove("SpawnPotentials");
    }
    public static void setEntityId(NBTCompound base, String legacyName) throws InvocationTargetException, IllegalAccessException {
        base.put("EntityId", legacyName);
    }
    public static void setSpawnData(NBTCompound base, NBTCompound spawnDataNbt) throws InvocationTargetException, IllegalAccessException {
        base.put("SpawnData", spawnDataNbt);
    }
    public static void setSpawnCount(NBTCompound base, short spawnCount) throws InvocationTargetException, IllegalAccessException {
        base.put("SpawnCount", spawnCount);
    }
    public static void setSpawnMinDelay(NBTCompound base, short minDelay) throws InvocationTargetException, IllegalAccessException {
        base.put("MinSpawnDelay", minDelay );
    }
    public static void setSpawnMaxDelay(NBTCompound base, short maxDelay) throws InvocationTargetException, IllegalAccessException {
        base.put("MaxSpawnDelay", maxDelay );
    }
    public static void setMaxNerbyEntity(NBTCompound base, short maxCount) throws InvocationTargetException, IllegalAccessException {
        base.put("MaxNerbyEntity", maxCount);
    }
    public static void setRequirePlayerRange(NBTCompound base, short range) throws InvocationTargetException, IllegalAccessException {
        base.put("RequirePlayerRange", range);
    }
    public static void setSpawnRange(NBTCompound base, short spawnRange) throws InvocationTargetException, IllegalAccessException {
        base.put("SpawnRange", spawnRange);
    }
    public NMSSpawner(Block block){
        super(block);
        if (!inited) init();
    }
    public NMSSpawner(NMSBlock nmsBlock){
        super(nmsBlock.getBase());
        if (!inited) init();

    }

    public NMSSpawner(Object tileEntity) throws Exception {
        super(tileEntity);
        if (!inited) init();
        if (!isInstanceOf()) throw new Exception("Not instance of Spawner Tile Entity");
    }
}
