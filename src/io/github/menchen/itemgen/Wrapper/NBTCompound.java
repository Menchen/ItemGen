package io.github.menchen.itemgen.Wrapper;

import io.github.menchen.itemgen.Wrapper.ReflectionManager.ClassType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTCompound {
    private Object base;
    private static boolean inited = false;

    private static Class<?> cls;
    private static Constructor NBTTagCons;
    private static Method NBTTag_setString;
    private static Method NBTTag_setByte;
    private static Method NBTTag_setShort;
    private static Method NBTTag_setBoolean;
    private static Method NBTTag_setInt;
    private static Method NBTTag_setFloat;
    private static Method NBTTag_set;
    private static Method NBTTag_setLong;

    private static Method NBTTag_getString;
    private static Method NBTTag_getByte;
    private static Method NBTTag_getShort;
    private static Method NBTTag_getBoolean;
    private static Method NBTTag_getInt;
    private static Method NBTTag_getFloat;
    private static Method NBTTag_get;
    private static Method NBTTag_getLong;
    private static Method NBTTag_clone;

    private static Method NBTTag_remove;

    private static void init(){

        cls = ReflectionManager.getOrCreateClass("NBTTagCompound",ClassType.NMS);
        NBTTagCons = ReflectionManager.getOrCreateConstructor(cls);
        NBTTag_setString = ReflectionManager.getOrCreateMethod(cls,"setString",String.class,String.class);
        NBTTag_setByte = ReflectionManager.getOrCreateMethod(cls,"setByte",String.class,Byte.class);
        NBTTag_setShort = ReflectionManager.getOrCreateMethod(cls,"setShort",String.class,Short.class);
        NBTTag_setBoolean = ReflectionManager.getOrCreateMethod(cls,"setBoolean",String.class,Boolean.class);
        NBTTag_setInt = ReflectionManager.getOrCreateMethod(cls,"setInt",String.class,Integer.class);
        NBTTag_set = ReflectionManager.getOrCreateMethod(cls,"set",String.class,cls);


        NBTTag_getString = ReflectionManager.getOrCreateMethod(cls,"getString",String.class);
        NBTTag_getByte = ReflectionManager.getOrCreateMethod(cls,"getByte",String.class);
        NBTTag_getShort = ReflectionManager.getOrCreateMethod(cls,"getShort",String.class);
        NBTTag_getBoolean = ReflectionManager.getOrCreateMethod(cls,"getBoolean",String.class);
        NBTTag_getInt = ReflectionManager.getOrCreateMethod(cls,"getInt",String.class);
        NBTTag_get = ReflectionManager.getOrCreateMethod(cls,"get",String.class);

        NBTTag_remove = ReflectionManager.getOrCreateMethod(cls,"remove",String.class);
        NBTTag_clone = ReflectionManager.getOrCreateMethod(cls,"g");
        inited = true;

    }
    public NBTCompound cloneNBT(){
        try {
            return new NBTCompound(NBTTag_clone.invoke(base));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Class<?> getBaseClass(){
        return cls;
    }
    public Object getBase(){
        return base;
    }
    public short getShort(String name) throws InvocationTargetException, IllegalAccessException {
            return (Short) NBTTag_getShort.invoke(base,name);
    }
    public String getString(String name) throws InvocationTargetException, IllegalAccessException {
            return (String) NBTTag_getString.invoke(base,name);
    }
    public byte getByte(String name) throws InvocationTargetException, IllegalAccessException {
            return (Byte) NBTTag_getByte.invoke(base,name);
    }
    public int getInt(String name) throws InvocationTargetException, IllegalAccessException {
            return (int) NBTTag_getInt.invoke(base,name);
    }
    public boolean getBoolean(String name) throws InvocationTargetException, IllegalAccessException {
            return (boolean) NBTTag_getBoolean.invoke(base,name);
    }
    public NBTCompound get(String name) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        return new NBTCompound(NBTTag_get.invoke(base,name));
    }
    public void remove(String name) throws InvocationTargetException, IllegalAccessException {
        NBTTag_remove.invoke(base,name);
    }


    public NBTCompound(Object nbt) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!inited) init();
        this.base = NBTTagCons.newInstance();
    }
    public NBTCompound() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!inited) init();
        this.base = NBTTagCons.newInstance();
    }
    public void put(String name,boolean b) throws InvocationTargetException, IllegalAccessException {
        NBTTag_setBoolean.invoke(base,name,b);
    }
    public void put(String name,String s) throws InvocationTargetException, IllegalAccessException {
        NBTTag_setString.invoke(base,name,s);
    }
    public void put(String name,int i) throws InvocationTargetException, IllegalAccessException {
        NBTTag_setInt.invoke(base,name,i);
    }
    public void put(String name,byte b) throws InvocationTargetException, IllegalAccessException {
        NBTTag_setByte.invoke(base,name,b);
    }
    public void put(String name,NBTCompound c) throws InvocationTargetException, IllegalAccessException {
        NBTTag_set.invoke(base,name,c);
    }
    public void put(String name,short s) throws InvocationTargetException, IllegalAccessException {
        NBTTag_setShort.invoke(base,name,s);
    }
}
