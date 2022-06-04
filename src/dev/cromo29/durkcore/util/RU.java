package dev.cromo29.durkcore.util;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.*;

public class RU {
    public static final String serverVersion = null;
    private static Field FIELD_DOT_MODIFIERS;

    public static void makeAccessible(Field field) {
        try {
            field.setAccessible(true);
            FIELD_DOT_MODIFIERS.setInt(field, field.getModifiers() & -Modifier.FINAL);
        } catch (Exception exception) {
            throw asRuntimeException(exception);
        }
    }

    public static void makeAccessible(Method method) {
        try {
            method.setAccessible(true);
        } catch (Exception exception) {
            throw asRuntimeException(exception);
        }
    }

    public static void makeAccessible(Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
        } catch (Exception exception) {
            throw asRuntimeException(exception);
        }
    }

    public static RuntimeException asRuntimeException(Throwable throwable) {

        // Runtime
        if (throwable instanceof RuntimeException) return (RuntimeException) throwable;

        // Invocation
        if (throwable instanceof InvocationTargetException) return asRuntimeException(throwable.getCause());

        // Rest
        return new IllegalStateException(throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
    }

    public static Class<?> getCraftPlayer() throws Exception {
        return getBukkitClass("entity.CraftPlayer");
    }

    public static Class<?> getPacket() throws Exception {
        return getNMSClass("Packet");
    }

    public static Object getHandle(Player player) throws Exception {
        return getMethod(getCraftPlayer(), "getHandle").invoke(player);
    }

    public static Object getConnection(Player player) throws Exception {
        Object handle = getHandle(player);
        return handle.getClass().getField("playerConnection").get(handle);
    }

    public static void sendPacket(Player player, Object packet) throws Exception {
        Object connection = getConnection(player);
        connection.getClass().getMethod("sendPacket", getPacket()).invoke(connection, packet);
    }

    public static Class<?> getBukkitClass(String clazz) throws Exception {
        return Class.forName("org.bukkit.craftbukkit." + serverVersion + "." + clazz);
    }

    public static Class<?> getBungeeClass(String path, String clazz) throws Exception {
        return Class.forName("net.md_5.bungee." + path + "." + clazz);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) throws Exception {
        Constructor<?> constructor = clazz.getConstructor(args);
        constructor.setAccessible(true);

        return constructor;
    }

    public static Enum<?> getEnum(Class<?> clazz, String constant) throws Exception {
        Class<?> c = Class.forName(clazz.getName());
        Enum<?>[] econstants = (Enum[]) c.getEnumConstants();

        for (Enum<?> enumm : econstants) {

            if (enumm.name().equalsIgnoreCase(constant)) return enumm;
        }

        throw new Exception("Enum constant not found " + constant);
    }

    public static Enum<?> getEnum(Class<?> clazz, String enumName, String constant) throws Exception {
        Class<?> c = Class.forName(clazz.getName() + "$" + enumName);
        Enum<?>[] econstants = (Enum[]) c.getEnumConstants();

        for (Enum<?> enumm : econstants) {

            if (enumm.name().equalsIgnoreCase(enumName)) return enumm;
        }

        throw new Exception("Enum constant not found " + constant);
    }

    public static Field getField(Class<?> clazz, String fieldName) throws Exception {
        Field field;

        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (Exception exception) {
            field = clazz.getField(fieldName);
        }

        setFieldAccessible(field);
        return field;
    }

    public static Object getFirstObject(Class<?> clazz, Class<?> objectClass, Object instance) throws Exception {
        Field field = null;

        for (Field field1 : clazz.getDeclaredFields()) {

            if (field1.getType().equals(objectClass)) {
                field = field1;
                break;
            }

        }

        if (field == null) {

            for (Field field1 : clazz.getFields()) {

                if (field1.getType().equals(objectClass)) {
                    field = field1;
                    break;
                }
            }

        }

        setFieldAccessible(field);
        return field.get(instance);
    }

    public static Method getMethod(Class<?> clazz, String methodName) throws Exception {
        Method method;

        try {
            method = clazz.getDeclaredMethod(methodName);
        } catch (Exception exception) {

            try {
                method = clazz.getMethod(methodName);
            } catch (Exception exception1) {
                return null;
            }

        }

        method.setAccessible(true);
        return method;
    }

    public static <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
        Field[] declaredFields = target.getDeclaredFields();

        for (Field field : declaredFields) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return field;
            }
        }

        if (target.getSuperclass() != null) return getField(target.getSuperclass(), name, fieldType, index);

        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) throws Exception {
        Method method;

        try {
            method = clazz.getDeclaredMethod(methodName, args);
        } catch (Exception exception) {
            try {
                method = clazz.getMethod(methodName, args);
            } catch (Exception exception1) {
                return null;
            }
        }

        method.setAccessible(true);
        return method;
    }

    public static String getServerVersion() {
        return serverVersion;
    }

    public synchronized static Class<?> getOBCClass(String obcClassName) {

        String clazzName = "org.bukkit.craftbukkit." + serverVersion + obcClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }

        return clazz;
    }

    public static Class<?> getNMSClass(String clazz) throws Exception {
        return Class.forName("net.minecraft.server." + serverVersion + "." + clazz);
    }

    public static Object getObject(Class<?> clazz, Object object, String fieldName) throws Exception {
        return getField(clazz, fieldName).get(object);
    }

    public static Object getObject(Object object, String fieldName) throws Exception {
        return getField(object.getClass(), fieldName).get(object);
    }

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initArgs) throws Exception {
        return getConstructor(clazz, args).newInstance(initArgs);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String method) throws Exception {
        return getMethod(clazz, method).invoke(object);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String method, Class<?>[] args, Object... initArgs) throws Exception {
        return getMethod(clazz, method, args).invoke(object, initArgs);
    }

    public static Object invokeMethod(Class<?> clazz, Object object, String method, Object... initArgs) throws Exception {
        return getMethod(clazz, method).invoke(object, initArgs);
    }

    public static Object invokeMethod(Object object, String method) throws Exception {
        return getMethod(object.getClass(), method).invoke(object);
    }

    public static Object invokeMethod(Object object, String method, Object[] initArgs) throws Exception {
        return getMethod(object.getClass(), method).invoke(object, initArgs);
    }

    public static void setFieldAccessible(Field field) throws Exception {
        field.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");

        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }

    public static void setObject(Class<?> clazz, Object object, String fieldName, Object value) throws Exception {
        getField(clazz, fieldName).set(object, value);
    }

    public static void setObject(Object object, String fieldName, Object value) throws Exception {
        getField(object.getClass(), fieldName).set(object, value);
    }

    static {
        try {
            Class.forName("org.bukkit.Bukkit");

            FIELD_DOT_MODIFIERS = Field.class.getDeclaredField("modifiers");
            FIELD_DOT_MODIFIERS.setAccessible(true);

            setObject(RU.class, null, "serverVersion", Bukkit.getServer().getClass().getPackage().getName()
                    .substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(46) + 1));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

    }
}
