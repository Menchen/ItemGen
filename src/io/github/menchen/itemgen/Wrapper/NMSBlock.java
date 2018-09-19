package io.github.menchen.itemgen.Wrapper;

import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSBlock {
    private Object base;
    private Object cbWorld;

    private static boolean inited = false;
    private static Class<?> cbWorldcls;
    private static Class<?> nmsWorldcls;
    private static Class<?> mobSpawnerTileEntitycls;
    private static Class<?> blockPositioncls;
    private static Constructor blockPositionCons;
    private static Method cwHandle;
    private static Method getTileEntity;

    private static void init(){
        cbWorldcls = ReflectionManager.getOrCreateClass("CraftWorld",ReflectionManager.ClassType.BUKKIT);
        nmsWorldcls = ReflectionManager.getOrCreateClass("World",ReflectionManager.ClassType.NMS);
        mobSpawnerTileEntitycls = ReflectionManager.getOrCreateClass("TileEntityMobSpawner",ReflectionManager.ClassType.NMS);
        blockPositioncls = ReflectionManager.getOrCreateClass("BlockPosition",ReflectionManager.ClassType.NMS);
        blockPositionCons = ReflectionManager.getOrCreateConstructor(blockPositioncls);
        cwHandle = ReflectionManager.getOrCreateMethod(cbWorldcls,"getHandle");
        getTileEntity = ReflectionManager.getOrCreateMethod(nmsWorldcls,"getTileEntity",blockPositioncls);

        inited = true;
        }

        public NMSBlock(Object tileentity){
        base = tileentity;
        cbWorld = null;
        }

        public NMSBlock(Block block){
            try {
                cbWorld = cwHandle.invoke(cbWorldcls.cast(block.getWorld()));
                base = getTileEntity.invoke(cbWorld, blockPositionCons.newInstance(block.getX(), block.getY(), block.getZ()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }


}
