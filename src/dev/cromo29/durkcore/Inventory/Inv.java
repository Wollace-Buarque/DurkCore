package dev.cromo29.durkcore.Inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Inv implements InventoryHolder {

    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers;
    private Set<Consumer<InventoryOpenEvent>> openHandlers;
    private Set<Consumer<InventoryCloseEvent>> closeHandlers;
    private Set<Consumer<InventoryClickEvent>> clickHandlers;
    private List<Inv> pages;
    private Map<String, Inv> pagesByKey;
    private Predicate<Player> closeFilter;

    private Inventory inventory;
    private boolean ignorePlayerInventory;
    private boolean cancelPlayerInventoryClick;
    private boolean cancelClick;

    public Inv clone() {
        try {
            Inv toRet = new Inv(inventory.getSize(), inventory.getName() == null ? inventory.getType().getDefaultTitle() : inventory.getName());

            toRet.inventory.setContents(inventory.getContents());
            toRet.closeFilter = closeFilter;
            toRet.itemHandlers.putAll(itemHandlers);

            if (openHandlers != null)
                toRet.openHandlers = Sets.newHashSet(openHandlers);

            if (closeHandlers != null)
                toRet.closeHandlers = Sets.newHashSet(closeHandlers);

            if (clickHandlers != null)
                toRet.clickHandlers = Sets.newHashSet(clickHandlers);

            if (pages != null)
                toRet.pages = Lists.newArrayList(pages);

            if (pagesByKey != null)
                toRet.pagesByKey = Maps.newHashMap(pagesByKey);

            toRet.ignorePlayerInventory = ignorePlayerInventory;
            toRet.cancelPlayerInventoryClick = cancelPlayerInventoryClick;
            toRet.cancelClick = cancelClick;

            return toRet;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean hasPage(int id) {
        return id < pages.size();
    }

    public boolean hasPage(String id) {
        return pagesByKey.containsKey(id);
    }

    public Inv getPage(int id) {
        return hasPage(id) ? pages.get(id) : null;
    }

    public Inv getPage(String id) {
        return hasPage(id) ? pagesByKey.get(id) : null;
    }

    public void addPage(Inv page) {
        pages.add(page);
    }

    public boolean setPage(int id, Inv page) {
        if (!hasPage(id))
            return false;
        else {
            pages.set(id, page);
            return true;
        }
    }

    public void addPage(String id, Inv page) {
        pagesByKey.put(id, page);
    }

    public boolean setPage(String id, Inv page) {
        if (!hasPage(id))
            return false;
        else {
            pagesByKey.put(id, page);
            return true;
        }
    }

    public boolean removePage(int id) {
        if (!hasPage(id))
            return false;
        else {
            pages.remove(id);
            return true;
        }
    }

    public boolean removePage(String id) {
        if (!hasPage(id))
            return false;
        else {
            pagesByKey.remove(id);
            return true;
        }
    }

    public List<Inv> getPages() {
        return pages;
    }

    public Map<String, Inv> getPagesByStringId() {
        return pagesByKey;
    }

    public void clearPages() {
        pages.clear();
        pagesByKey.clear();
    }

    public void closeInventoryForAll() {
        Lists.newArrayList(getViewers()).forEach(HumanEntity::closeInventory);
    }

    public void closeInventoryAndPagesForAll() {
        closeInventoryForAll();

        Lists.newArrayList(getPages()).forEach(Inv::closeInventoryAndPagesForAll);
        Lists.newArrayList(getPagesByStringId().values()).forEach(Inv::closeInventoryAndPagesForAll);
    }

    public Inv(int size) {
        this(size, InventoryType.CHEST.getDefaultTitle());
    }

    public Inv(int size, String title) {
        this(size, InventoryType.CHEST, TXT.parse(title));
    }

    public Inv(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    public Inv(InventoryType type, String title) {
        this(0, type, TXT.parse(title));
    }

    private Inv(int size, InventoryType type, String title) {
        itemHandlers = new HashMap<>();
        pages = Lists.newArrayList();
        pagesByKey = Maps.newHashMap();
        ignorePlayerInventory = false;
        cancelPlayerInventoryClick = true;
        cancelClick = true;

        if (type == InventoryType.CHEST && size > 0)
            inventory = Bukkit.createInventory(this, size, TXT.parse(title));
        else inventory = Bukkit.createInventory(this, Objects.requireNonNull(type, "type"), TXT.parse(title));

        if (inventory.getHolder() != this)
            throw new IllegalStateException("Inventory holder is not Inv, found: " + inventory.getHolder());
    }

    public static int getInventorySizeByInt(int i) {
        return i < 9 ? 9 : (i < 18 ? 18 : (i < 27 ? 27 : (i < 36 ? 36 : (i < 45 ? 45 : 54))));
    }

    protected void onOpen(InventoryOpenEvent event) {
    }

    protected void onClick(InventoryClickEvent event) {
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    public void addItem(ItemStack item) {
        addItem(item, null);
    }

    public void addItem(ItemStack item, Consumer<InventoryClickEvent> handler) {
        int slot = inventory.firstEmpty();

        if (slot >= 0)
            setItem(slot, item, handler);
    }

    public boolean isPlayerInventory(Inventory inventory) {
        return inventory.getType() == InventoryType.PLAYER;
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        inventory.setItem(slot, item);

        if (handler != null)
            itemHandlers.put(slot, handler);
        else itemHandlers.remove(slot);
    }

    public void updateItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void setItems(int slotFrom, int slotTo, ItemStack item) {
        setItems(slotFrom, slotTo, item, null);
    }

    public void setItems(int slotFrom, int slotTo, ItemStack... items) {
        Iterator<ItemStack> iterator = Lists.newArrayList(items).iterator();

        for (int i = slotFrom; i <= slotTo && iterator.hasNext(); ++i)
            setItem(i, iterator.next());
    }

    public void setItems(int slotFrom, int slotTo, List<ItemStack> items) {
        setItems(slotFrom, slotTo, items, null);
    }

    public void setItems(int slotFrom, int slotTo, List<ItemStack> items, Consumer<InventoryClickEvent> handler) {
        Iterator<ItemStack> iterator = items.iterator();

        for (int i = slotFrom; i <= slotTo && iterator.hasNext(); ++i)
            setItem(i, iterator.next(), handler);
    }

    public void setItems(int slotFrom, int slotTo, ItemStack item, Consumer<InventoryClickEvent> handler) {
        for (int i = slotFrom; i <= slotTo; ++i)
            setItem(i, item, handler);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = Lists.newArrayList();

        for (ItemStack content : inventory.getContents())
            if (content != null && content.getType() != Material.AIR) items.add(content);

        return items;
    }

    public List<ItemStack> getItemsIncludingNull() {
        return Lists.newArrayList(inventory.getContents());
    }

    public List<ItemStack> getItems(int fromSlot, int toSlot) {
        List<ItemStack> items = Lists.newArrayList();

        for (int i = fromSlot; i <= toSlot; i++) {
            ItemStack item = getItem(i);

            if (item != null && item.getType() != Material.AIR)
                items.add(item);
        }
        return items;
    }

    public List<ItemStack> getItemsIncludingNull(int fromSlot, int toSlot) {
        List<ItemStack> items = Lists.newArrayList();

        for (int i = fromSlot; i <= toSlot; i++) items.add(getItem(i));

        return items;
    }

    public List<ItemStack> getItems(int... slots) {
        List<ItemStack> items = Lists.newArrayList();

        for (int slot : slots) {
            ItemStack item = getItem(slot);

            if (item != null && item.getType() != Material.AIR)
                items.add(item);
        }
        return items;
    }

    public List<ItemStack> getItemsIncludingNull(int... slots) {
        List<ItemStack> items = Lists.newArrayList();

        for (int slot : slots) items.add(getItem(slot));

        return items;
    }

    public void setInMiddle(ItemStack item) {
        setItem(TXT.getMiddleSlot(inventory), item);
    }

    public void setInMiddle(ItemStack... item) {
        for (ItemStack itemStack : item) setItem(TXT.getMiddleSlot(inventory), itemStack);
    }

    public int setInMiddle(ItemStack item, Consumer<InventoryClickEvent> clickHandler) {
        int toReturn = TXT.getMiddleSlot(inventory);

        setItem(toReturn, item, clickHandler);

        return toReturn;
    }

    public void setInMiddle(Consumer<InventoryClickEvent> clickHandler, ItemStack... item) {
        for (ItemStack itemStack : item)
            setItem(TXT.getMiddleSlot(inventory), itemStack, clickHandler);
    }

    public void setInMiddle(Material material) {
        setItem(TXT.getMiddleSlot(inventory), new ItemStack(material));
    }

    public void setInMiddle(Material... materials) {
        for (Material material : materials)
            setItem(TXT.getMiddleSlot(inventory), new ItemStack(material));
    }

    public void setInMiddle(Material material, Consumer<InventoryClickEvent> clickHandler) {
        setItem(TXT.getMiddleSlot(inventory), new ItemStack(material), clickHandler);
    }

    public void setInMiddle(Consumer<InventoryClickEvent> clickHandler, Material... materials) {
        for (Material material : materials)
            setItem(TXT.getMiddleSlot(inventory), new ItemStack(material), clickHandler);
    }

    public void fillBorders(ItemStack item) {
        setItems(getBorders(), item);
    }

    public void fillBorders(ItemStack... items) {
        Iterator<ItemStack> itemStackIterator = Arrays.asList(items).iterator();
        int[] slots = getBorders();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], itemStackIterator.next());
    }

    public void fillBorders(ItemStack item, Consumer<InventoryClickEvent> clickHandler) {
        setItems(getBorders(), item, clickHandler);
    }

    public void fillBorders(Consumer<InventoryClickEvent> clickHandler, ItemStack... items) {
        Iterator<ItemStack> itemStackIterator = Arrays.asList(items).iterator();
        int[] slots = getBorders();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], itemStackIterator.next(), clickHandler);
    }

    public void fillBorders(Material material) {
        setItems(getBorders(), new ItemStack(material));
    }

    public void fillBorders(Material... materials) {
        Iterator<Material> itemStackIterator = Arrays.asList(materials).iterator();
        int[] slots = getBorders();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], new ItemStack(itemStackIterator.next()));
    }

    public void fillBorders(Material material, Consumer<InventoryClickEvent> clickHandler) {
        setItems(getBorders(), new ItemStack(material), clickHandler);
    }

    public void fillBorders(Consumer<InventoryClickEvent> clickHandler, Material... materials) {
        Iterator<Material> itemStackIterator = Arrays.asList(materials).iterator();
        int[] slots = getBorders();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], new ItemStack(itemStackIterator.next()), clickHandler);
    }

    public void fillMiddle(ItemStack item) {
        setItems(getMiddle(), item);
    }

    public void fillMiddle(ItemStack... items) {
        Iterator<ItemStack> itemStackIterator = Arrays.asList(items).iterator();
        int[] slots = getMiddle();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], itemStackIterator.next());
    }

    public void fillMiddle(ItemStack item, Consumer<InventoryClickEvent> clickHandler) {
        setItems(getMiddle(), item, clickHandler);
    }

    public void fillMiddle(Consumer<InventoryClickEvent> clickHandler, ItemStack... items) {
        Iterator<ItemStack> itemStackIterator = Arrays.asList(items).iterator();
        int[] slots = getMiddle();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], itemStackIterator.next(), clickHandler);
    }

    public void fillMiddle(Material material) {
        setItems(getMiddle(), new ItemStack(material));
    }

    public void fillMiddle(Material... materials) {
        Iterator<Material> itemStackIterator = Arrays.asList(materials).iterator();
        int[] slots = getMiddle();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], new ItemStack(itemStackIterator.next()));
    }

    public void fillMiddle(Material material, Consumer<InventoryClickEvent> clickHandler) {
        setItems(getMiddle(), new ItemStack(material), clickHandler);
    }

    public void fillMiddle(Consumer<InventoryClickEvent> clickHandler, Material... materials) {
        Iterator<Material> itemStackIterator = Arrays.asList(materials).iterator();
        int[] slots = getMiddle();
        int slotAmount = slots.length;

        for (int currentSlot = 0; itemStackIterator.hasNext() && currentSlot < slotAmount; ++currentSlot)
            setItem(slots[currentSlot], new ItemStack(itemStackIterator.next()), clickHandler);
    }

    public void setItems(int[] slots, ItemStack item) {
        setItems(slots, item, null);
    }

    public void setItems(int[] slots, ItemStack item, Consumer<InventoryClickEvent> handler) {

        for (int slot : slots)
            setItem(slot, item, handler);
    }

    public void setItems(Consumer<InventoryClickEvent> handler, int[] slots, ItemStack... items) {
        for (ItemStack itemStack : items)
            for (int slot : slots)
                setItem(slot, itemStack, handler);
    }

    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    public void removeItem(int slot) {
        inventory.clear(slot);
        itemHandlers.remove(slot);
    }

    public void removeItems(int... slots) {

        for (int slot : slots)
            removeItem(slot);
    }

    public void setCloseFilter(Predicate<Player> closeFilter) {
        this.closeFilter = closeFilter;
    }

    public void addOpenHandler(Consumer<InventoryOpenEvent> openHandler) {
        if (openHandlers == null)
            openHandlers = new HashSet<>();

        openHandlers.add(openHandler);
    }

    public void setCancelClick(boolean cancel) {
        cancelClick = cancel;
    }

    public boolean isCancellingClick() {
        return cancelClick;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void ignorePlayerInventoryClick(boolean ignore) {
        ignorePlayerInventory = ignore;
    }

    public void setIgnorePlayerInventoryClick(boolean ignore, boolean cancelClick) {
        ignorePlayerInventory = ignore;
        cancelPlayerInventoryClick = cancelClick;
    }

    public boolean isIgnoringPlayerInventoryClick() {
        return ignorePlayerInventory;
    }

    public boolean isCancellingPlayerInventoryClick() {
        return cancelPlayerInventoryClick;
    }

    public void addCloseHandler(Consumer<InventoryCloseEvent> closeHandler) {
        if (closeHandlers == null)
            closeHandlers = new HashSet<>();

        closeHandlers.add(closeHandler);
    }

    public void addCloseHandlerToAllPages(Consumer<InventoryCloseEvent> closeHandler) {
        addCloseHandler(closeHandler);

        pages.forEach(page -> page.addCloseHandler(closeHandler));
    }

    public void setItemToFirstEmpty(ItemStack item) {
        setItem(firstEmpty(), item);
    }

    public void setItemToFirstEmpty(ItemStack... items) {
        for (ItemStack item : items) if (firstEmpty() != -1) setItem(firstEmpty(), item);
    }

    public void setItemToFirstEmpty(Material material) {
        setItem(firstEmpty(), new ItemStack(material));
    }

    public void setItemToFirstEmpty(Material... materials) {
        for (Material material : materials) if (firstEmpty() != -1) setItem(firstEmpty(), new ItemStack(material));
    }

    public void setItemToFirstEmpty(ItemStack item, Consumer<InventoryClickEvent> clickHandler) {
        setItem(firstEmpty(), item, clickHandler);
    }

    public void setItemToFirstEmpty(Consumer<InventoryClickEvent> clickHandler, ItemStack... items) {
        for (ItemStack item : items) if (firstEmpty() != -1) setItem(firstEmpty(), item, clickHandler);
    }

    public void setItemToFirstEmpty(Material material, Consumer<InventoryClickEvent> clickHandler) {
        setItem(firstEmpty(), new ItemStack(material), clickHandler);
    }

    public void setItemToFirstEmpty(Consumer<InventoryClickEvent> clickHandler, Material... materials) {
        for (Material material : materials)
            if (firstEmpty() != -1) setItem(firstEmpty(), new ItemStack(material), clickHandler);
    }

    public int inventorySize() {
        return inventory.getSize();
    }

    public int middleFirstEmpty() {
        for (int i : getMiddle()) {
            ItemStack item = getItem(i);

            if (item == null || item.getType() == Material.AIR) return i;
        }
        return -1;
    }

    public int firstEmpty() {
        return inventory.firstEmpty();
    }

    public List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    public void clearInventorySafe(int... slot) {

        for (int i = 0; i < inventory.getSize(); i++)
            for (int safeSlot : slot) {
                if (i == safeSlot) continue;

                removeItem(safeSlot);
            }
    }

    public void clearInventory() {
        inventory.clear();
        itemHandlers.clear();
    }

    public int emptySlots() {
        int empty = 0;

        for (ItemStack content : inventory.getContents())
            if (content == null || content.getType() == Material.AIR) empty++;

        return empty;
    }

    public void removeAll(ItemStack item) {
        Map<Integer, ItemStack> items = Maps.newHashMap();
        int slot = 0;

        for (ItemStack content : inventory.getContents()) {
            if (content != null && content.getType() != Material.AIR) items.put(slot, content);
            slot++;
        }

        items.forEach((s, i) -> {
            if (item.isSimilar(i)) removeItem(s);
        });
    }

    public void removeAll(Material material) {
        Map<Integer, Material> items = Maps.newHashMap();
        int slot = 0;

        for (ItemStack content : inventory.getContents()) {
            if (content != null && content.getType() != Material.AIR) items.put(slot, content.getType());
            slot++;
        }

        items.forEach((s, m) -> {
            if (m == material) removeItem(s);
        });
    }

    public void addClickHandler(Consumer<InventoryClickEvent> clickHandler) {
        if (clickHandlers == null)
            clickHandlers = new HashSet<>();

        clickHandlers.add(clickHandler);
    }

    public void addClickHandlerToAllPages(Consumer<InventoryClickEvent> clickHandler) {
        addClickHandler(clickHandler);

        pages.forEach(page -> page.addClickHandler(clickHandler));
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void open(HumanEntity whoClicked) {
        whoClicked.openInventory(inventory);
    }

    public int[] getBorders() {
        int size = inventory.getSize();

        return IntStream.range(0, size).filter(i -> size < 27 || i < 9 || i % 9 == 0 || (i - 8) % 9 == 0 || i > size - 9).toArray();
    }

    public int[] getMiddle() {
        int size = inventory.getSize();

        if (size == 3 * 9) return new int[]{10, 11, 12, 13, 14, 15, 16};

        return IntStream.range(0, size).filter(i -> i > 9 && i < 17 && size > 18 || i > 18 && i < 26 && size > 27 || i > 27 && i < 35 && size > 36 || i > 36 && i < 44 && size > 45).toArray();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void handleOpen(InventoryOpenEvent inventoryOpenEvent) {
        onOpen(inventoryOpenEvent);

        if (openHandlers != null) {
            openHandlers.forEach((consumer) -> {
                consumer.accept(inventoryOpenEvent);
            });
        }

    }

    public boolean handleClose(InventoryCloseEvent inventoryCloseEvent) {
        onClose(inventoryCloseEvent);

        if (closeHandlers != null) {
            closeHandlers.forEach((consumer) -> {
                consumer.accept(inventoryCloseEvent);
            });
        }

        return closeFilter != null && closeFilter.test((Player) inventoryCloseEvent.getPlayer());
    }

    public void handleClick(InventoryClickEvent inventoryClickEvent) {
        onClick(inventoryClickEvent);

        if (clickHandlers != null) {
            clickHandlers.forEach((consumer) -> {
                consumer.accept(inventoryClickEvent);
            });
        }

        Consumer<InventoryClickEvent> clickConsumer = itemHandlers.get(inventoryClickEvent.getSlot());
        if (clickConsumer != null) {
            clickConsumer.accept(inventoryClickEvent);
        }

    }
}
