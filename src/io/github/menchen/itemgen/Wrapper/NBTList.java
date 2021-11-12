package io.github.menchen.itemgen.Wrapper;

import net.minecraft.server.v1_12_R1.NBTTagList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTList {
    private static boolean inited = false;
    private Object base;

    private static Class<?> cls;
    private static Class<?> nbtCls;
    private static Constructor cons;
    private static Method add;
    private static Method remove;
    private static Method getInt;
    private static Method getFloat;
    private static Method getString;
    private static Method getNBT;
    private static Method getDouble;
    private static Method size;
    private static Method clone;
    private static Method insert;

    private static void init(){
        NBTTagList tagList = new NBTTagList();

        cls = ReflectionManager.getOrCreateClass("NBTTagList",ReflectionManager.ClassType.NMS);
        nbtCls = ReflectionManager.getOrCreateClass("NBTTagCompound",ReflectionManager.ClassType.NMS);
        cons = ReflectionManager.getOrCreateConstructor(cls);
        add = ReflectionManager.getOrCreateMethod(cls,"add",nbtCls);
        insert = ReflectionManager.getOrCreateMethod(cls,"a",nbtCls,int.class);
        remove = ReflectionManager.getOrCreateMethod(cls,"remove",int.class);
        getInt = ReflectionManager.getOrCreateMethod(cls,"c",int.class);
        getFloat = ReflectionManager.getOrCreateMethod(cls,"g",int.class);
        getDouble = ReflectionManager.getOrCreateMethod(cls,"f",int.class);
        getNBT = ReflectionManager.getOrCreateMethod(cls,"get",int.class);
        getString = ReflectionManager.getOrCreateMethod(cls,"getString",int.class);
        size = ReflectionManager.getOrCreateMethod(cls,"size");
        clone = ReflectionManager.getOrCreateMethod(cls,"d");

        inited = true;
    }
    public NBTList clone(){
        try {
            return new NBTList(clone.invoke(base));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public double getDouble(int i) throws InvocationTargetException, IllegalAccessException {
        return (double) getDouble.invoke(i);
    }

    public NBTCompound get(int i) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        return new NBTCompound(getNBT.invoke(base,i));
    }
    public String getString(int i) throws InvocationTargetException, IllegalAccessException {
        return (String) getString.invoke(base,i);
    }
    public float getFloat(int i) throws InvocationTargetException, IllegalAccessException {
        return (float) getFloat.invoke(base,i);
    }
    public int getInt(int i) throws InvocationTargetException, IllegalAccessException {
        return (int) getInt.invoke(base,i);
    }
    public int size() throws InvocationTargetException, IllegalAccessException {
        return (int) size.invoke(base);
    }
    public void remove(int i) throws InvocationTargetException, IllegalAccessException {
        remove.invoke(base,i);
    }
    public void insert(NBTCompound nbt,int i) throws InvocationTargetException, IllegalAccessException {
        insert.invoke(base,nbt.getBase(),i);
    }
    public void add(NBTCompound nbt) throws InvocationTargetException, IllegalAccessException {
        add.invoke(base,nbt.getBase());
    }

    public NBTList() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        base = cons.newInstance();
    }
    public NBTList(Object nbtList) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        base = nbtList;
    }




}
