package dev.cromo29.durkcore.Util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class SectionBuilder<T> {

    private static final Map<Class<?>, Adapter<?>> classAdapters = new HashMap<>();

    static {
        classAdapters.put(ItemStack.class, new ItemAdapter());
        classAdapters.put(String.class, new StringAdapter());
        classAdapters.put(StringList.class, new ListAdapter<>(new StringAdapter()));
        classAdapters.put(ItemList.class, new ListAdapter<>(new ItemAdapter()));
    }

    private final ConfigurationSection mainSection;
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final Map<String, Class<?>> parameters;
    private final Map<String, Adapter<?>> parametersAdapters;

    public static <E> SectionBuilder<E> of(Class<E> clazz, ConfigurationSection section) {
        return new SectionBuilder<E>(clazz, section);
    }

    private SectionBuilder(Class<T> clazz, ConfigurationSection mainSection) {
        this.mainSection = mainSection;
        this.clazz = clazz;
        this.constructor = (Constructor<T>) clazz.getConstructors()[0];
        this.parameters = new LinkedHashMap<>();
        this.parametersAdapters = new HashMap<>();
    }


    public SectionBuilder<T> parameter(String key, Class<?> valueClass) {
        this.parameters.put(key, valueClass);
        return this;
    }

    public SectionBuilder<T> parameter(String key, Class<?> valueClass, Adapter<?> adapter) {
        this.parameters.put(key, valueClass);
        this.parametersAdapters.put(key, adapter);
        return this;
    }

    public SectionBuilder<T> adapter(Class<?> clazz, Adapter<?> adapter) {
        classAdapters.put(clazz, adapter);
        return this;
    }


    public List<T> build() {
        List<T> toReturn = new ArrayList<>();
        for (String key : mainSection.getKeys(false)) {
            try {
                ConfigurationSection section = mainSection.getConfigurationSection(key);
                List<Object> objects = new ArrayList<>();
                objects.add(key);
                for (Map.Entry<String, Class<?>> entry : parameters.entrySet()) {
                    String parameter = entry.getKey();
                    Class<?> parameterClass = entry.getValue();
                    if (parametersAdapters.containsKey(parameter)) {
                        objects.add(parametersAdapters.get(parameter).supply(section.get(parameter)));
                    } else if (classAdapters.containsKey(parameterClass)) {
                        objects.add(classAdapters.get(parameterClass).supply(section.get(parameter)));
                    } else {
                        objects.add(section.get(parameter));
                    }
                }

                T object = constructor.newInstance(objects.toArray());
                toReturn.add(object);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return toReturn;
    }


    public static interface Adapter<A> {
        A supply(Object object);
    }

    public static class ItemAdapter implements Adapter<ItemStack> {

        @Override
        public ItemStack supply(Object object) {
            ConfigurationSection section = (ConfigurationSection) object;
            Material material = Material.valueOf(section.getString("Material"));

            int data = !section.isSet("Data") ? 0 : section.getInt("Data");
            int amount = !section.isSet("Quantidade") ? 1 : section.getInt("Quantidade");

            String name = !section.isSet("Nome") ? null : TXT.parse(section.getString("Nome"));
            List<String> lore = !section.isSet("Lore") ? null : section.getStringList("Lore").stream().map(TXT::parse).collect(Collectors.toList());
            HashMap<Enchantment, Integer> enchants = !section.isSet("Enchants") ? null : (HashMap<Enchantment, Integer>) section.getStringList("Enchants").stream().collect(Collectors.toMap(string -> Enchantment.getByName(string.split(":")[0]), string -> Integer.parseInt(string.split(":")[1].trim())));
            ItemStack itemStack = new ItemStack(material, amount, (byte) data);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (name != null) itemMeta.setDisplayName(name);
            if (lore != null) itemMeta.setLore(lore);
            if (enchants != null)
                enchants.forEach((enchantment, integer) -> itemMeta.addEnchant(enchantment, integer, true));

            itemStack.setItemMeta(itemMeta);

            return itemStack;
        }
    }

    private static class StringAdapter implements Adapter<String> {

        @Override
        public String supply(Object object) {
            return TXT.parse((String) object);
        }
    }

    public static class ListAdapter<A> implements Adapter<List<A>> {

        private final Adapter<A> adapter;

        public ListAdapter(Adapter<A> adapter) {
            this.adapter = adapter;
        }

        @Override
        public List<A> supply(Object object) {
            ConfigurationSection section = (ConfigurationSection) object;

            return section.getKeys(false).stream().map(section::getConfigurationSection).map(adapter::supply).map(o -> (A) o).collect(Collectors.toList());
        }
    }

    public static class ItemList {
    }

    public static class StringList {
    }
}
