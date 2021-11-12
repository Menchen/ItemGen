package io.github.menchen.itemgen.Wrapper;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSItemStack {
    private Object base;

    private static boolean inited=false;
    private static Class<?> cls;
    private static Class<?> cicls;
    private static Class<?> nbtcls;
    private static Method asNMSCopy;
    private static Method asBukkitCopy;
    private static Method save;
    private static Method setTag;
    private static Method getName;

    private static void init(){
        cls = ReflectionManager.getOrCreateClass("NMSItemStack",ReflectionManager.ClassType.NMS);
        cicls = ReflectionManager.getOrCreateClass("inventory.CraftItemStack",ReflectionManager.ClassType.BUKKIT);
        nbtcls = ReflectionManager.getOrCreateClass("NBTTagCompound",ReflectionManager.ClassType.NMS);
        save = ReflectionManager.getOrCreateMethod(cls,"save",nbtcls);
        setTag = ReflectionManager.getOrCreateMethod(cls,"setTag",nbtcls);
        getName = ReflectionManager.getOrCreateMethod(cls,"getName");
        asBukkitCopy = ReflectionManager.getOrCreateMethod(cicls,"asBukkitCopy",cls);
        asNMSCopy = ReflectionManager.getOrCreateMethod(cicls,"asNMSCopy",ItemStack.class);

        inited = true;
    }
    public String getName() throws InvocationTargetException, IllegalAccessException {
        return (String) getName.invoke(base);
    }
    public void setTag(NBTCompound c) throws InvocationTargetException, IllegalAccessException {
        setTag.invoke(base,c.getBase());
    }
    public void save(NBTCompound c) throws InvocationTargetException, IllegalAccessException {
        save.invoke(base,c.getBase());
    }
    public Object asNMSCopy(){
        return base;
    }
    public ItemStack asBukkitCopy() throws InvocationTargetException, IllegalAccessException {
        return (ItemStack) asBukkitCopy.invoke(null,base);
    }

    public NMSItemStack(ItemStack bukkitItem) throws InvocationTargetException, IllegalAccessException {
        if (!inited) init();
        base = asNMSCopy.invoke(null,bukkitItem);
    }

    public Class<?> getBaseClass(){
        return cls;
    }
    public NMSItemStack(Object nmsItem){
        if (!inited) init();
        base = nmsItem;
    }
}
