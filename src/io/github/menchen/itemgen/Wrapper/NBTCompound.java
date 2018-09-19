package io.github.menchen.itemgen.Wrapper;

import io.github.menchen.itemgen.Wrapper.ReflectionManager.ClassType;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Reflection;

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
        inited = true;

    }
    public Class<?> getBaseClass(){
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
    public NBTCompound get(String name) throws InvocationTargetException, IllegalAccessException {
        return new NBTCompound(NBTTag_get.invoke(base,name));
    }


    public NBTCompound(Object nbt){
        if (!inited) init();
        try {
            this.base = NBTTagCons.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public NBTCompound(){
        if (!inited) init();
        try {
            this.base = NBTTagCons.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,boolean b){
        try {
            NBTTag_setBoolean.invoke(base,name,b);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,String s){
        try {
            NBTTag_setString.invoke(base,name,s);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,int i){
        try {
            NBTTag_setInt.invoke(base,name,i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,byte b){
        try {
            NBTTag_setByte.invoke(base,name,b);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,NBTCompound c){
        try {
            NBTTag_set.invoke(base,name,c);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void put(String name,short s){
        try {
            NBTTag_setShort.invoke(base,name,s);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
