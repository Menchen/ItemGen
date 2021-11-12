package io.github.menchen.itemgen.Wrapper;


import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionManager {
    private static Map<String,Class<?>> classPool = new HashMap<>();
    private static Map<String,Method> methodPool = new HashMap<>();
    private static Map<String,Field> fieldPool = new HashMap<>();
    private static Map<String,Constructor> constructorPool = new HashMap<>();
    public enum ClassType {
        BUKKIT ("org.bukkit.craftbukkit."),
        NMS ("net.minecraft.server.");

        private final String name;

        private ClassType(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private static Class<?> getClass(String name,ClassType type) throws ClassNotFoundException {
        //private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(type.name + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }


    public static Class<?> getOrCreateClass(String name,ClassType type){

        if (classPool.containsKey(name)){
            return classPool.get(type.name+name);
        }else {
            try {
                Class<?> cls = getClass(name,type);
                classPool.put(type.name+name,cls);
                return cls;
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("Class Not Found: "+type.name+name);
                return null;
            }
        }
    }


    public static Method getOrCreateMethod(Class<?> cls,String name,@Nullable Class<?>...args){

        if (methodPool.containsKey(name)){
            return methodPool.get(cls.getName()+"#"+name);
        }else {
            try {
                Method method = cls.getDeclaredMethod(name,args);
                classPool.put(cls.getName()+"#"+name,cls);
                return method;
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("Method Not Found: "+cls.getName()+"#"+name);
                return null;
            }
        }
    }

    public static Field getOrCreateField(Class<?> cls,String name){

        if (fieldPool.containsKey(cls.getName()+"_"+name)){
            return fieldPool.get(cls.getName()+"_"+name);
        }else {
            try {
                Field field = cls.getDeclaredField(name);
                fieldPool.put(cls.getName()+"_"+name,field);
                return field;
            } catch (NoSuchFieldException e) {
                Bukkit.getLogger().severe("Field Not Found: "+cls.getName()+"_"+name);
                return null;
            }
        }
    }


    public static Constructor getOrCreateConstructor(Class<?> cls){

        if (constructorPool.containsKey(cls.getName())){
            return constructorPool.get(cls.getName());
        }else {
            try {
                Constructor constructor = cls.getConstructor();
                constructorPool.put(cls.getName(),constructor);
                return constructor;
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("Constructor Not Found: "+cls.getName());
                return null;
            }
        }
    }
}
