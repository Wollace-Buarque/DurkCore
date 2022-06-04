package dev.cromo29.durkcore.entity;

import com.google.common.collect.Lists;
import dev.cromo29.durkcore.DurkCore;
import dev.cromo29.durkcore.inventory.Inv;
import dev.cromo29.durkcore.specificutils.LocationUtil;
import dev.cromo29.durkcore.specificutils.PlayerUtil;
import dev.cromo29.durkcore.util.MakeItem;
import dev.cromo29.durkcore.util.ParticleEffect;
import dev.cromo29.durkcore.util.TXT;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

public class DurkPlayer implements Player {

    private final Player player;

    private DurkPlayer(Player player) {
        this.player = player;
    }

    private DurkPlayer(String playerName) {
        this.player = Bukkit.getPlayerExact(playerName);
    }

    public static DurkPlayer fromPlayer(Player player) {
        return new DurkPlayer(player);
    }

    public static DurkPlayer of(Player player) {
        return new DurkPlayer(player);
    }

    public static DurkPlayer by(Player player) {
        return new DurkPlayer(player);
    }

    public static DurkPlayer get(Player player) {
        return new DurkPlayer(player);
    }

    /*

    CUSTOM METHODS

     */

    public List<Block> getNearbyBlocks(int radius) {
        return getNearbyBlocks(radius, null);
    }

    public List<Block> getNearbyBlocks(int radius, List<Material> ignore) {
        List<Block> nearbyBlocks = LocationUtil.getNearbyBlocks(getLocation().getBlock(), radius);

        if (ignore == null || ignore.isEmpty()) return nearbyBlocks;

        return Lists.newArrayList(nearbyBlocks.stream().filter(block -> !ignore.contains(block.getType())).iterator());
    }

    public List<Player> getNearbyPlayers(int radius) {
        return LocationUtil.getClosestPlayersFromLocation(getLocation(), radius);
    }

    public List<Entity> getNearbyEntities(int radius) {
        return LocationUtil.getClosestEntitiesFromLocation(getLocation(), radius);
    }

    public Player getTargetPlayer(double searchRadius, double targetOffset) {
        return PlayerUtil.getTargetPlayer(player, searchRadius, targetOffset);
    }

    public Entity getTargetEntity(double searchRadius, double targetOffset) {
        return PlayerUtil.getTargetEntity(player, searchRadius, targetOffset, null);
    }

    public Entity getTargetEntity(double searchRadius, double targetOffset, List<EntityType> targetTypes) {
        return PlayerUtil.getTargetEntity(player, searchRadius, targetOffset, targetTypes);
    }

    public Location getRandomLocationArroundPlayer(int maxX, int maxZ) {
        return PlayerUtil.getRandomLocationArroundPlayer(player, maxX, maxZ);
    }

    public PlayerUtil.CardinalDirection getCardinalDirection() {
        return PlayerUtil.getCardinalDirection(player);
    }

    public String getIP() {
        return getAddress().getAddress().getHostAddress();
    }

    public int getPing() {
        return PlayerUtil.getPing(player);
    }

    public void lookAt(Location location) {
        PlayerUtil.lookAt(player, location);
    }

    public void openInventory(Inv inv) {
        inv.open(player);
    }

    public void spawnRandomFirework() {
        LocationUtil.detonateRandomFirework(getLocation());
    }

    public void sendParsedMessage(String message, Object... format) {
        sendMessage(TXT.parse(message, format));
    }

    public void sendParsedMessages(String... messages) {
        TXT.sendMessages(player, messages);
    }

    public int getInventoryEmptySlots() {
        return PlayerUtil.emptySlots(player);
    }

    public void decreaseItem(int slot) {
        PlayerUtil.decreaseItem(player, slot);
    }

    public void decreaseItem(int slot, int amount) {
        PlayerUtil.decreaseItem(player, slot, amount);
    }

    public void giveItem(ItemStack item, boolean dropExcess) {
        PlayerUtil.giveItem(player, item, dropExcess);
    }

    public void giveItems(List<ItemStack> items, boolean dropExcess) {
        PlayerUtil.giveItems(player, items, dropExcess);
    }

    public void insertItem(final ItemStack stack) {
        insertItem(stack, true);
    }

    public void insertItem(final ItemStack stack, boolean playSound) {
        PlayerUtil.insert(player, stack, playSound);
    }

    public boolean isAbilityMayBuild() {
        return PlayerUtil.isAbilityMayBuild(player);
    }

    public void setAbilityMayBuild(boolean mayBuild) {
        PlayerUtil.setAbilityMayBuild(player, mayBuild);
    }

    public boolean isAbilityInvulnerable() {
        return PlayerUtil.isAbilityInvulnerable(player);
    }

    public void setAbilityIsInvulnerable(boolean isInvulnerable) {
        PlayerUtil.setAbilityIsInvulnerable(player, isInvulnerable);
    }

    public float getAbilityWalkSpeed() {
        return PlayerUtil.getAbilityWalkSpeed(player);
    }

    public void setAbilityWalkSpeed(float walkSpeed) {
        PlayerUtil.setAbilityWalkSpeed(player, walkSpeed);
    }

    public float getAbilityFlySpeed() {
        return PlayerUtil.getAbilityFlySpeed(player);
    }

    public void setAbilityFlySpeed(float flySpeed) {
        PlayerUtil.setAbilityFlySpeed(player, flySpeed);
    }

    public boolean isAbilityFlying() {
        return PlayerUtil.isAbilityFlying(player);
    }

    public boolean isAbilityCanInstantlyBuild() {
        return PlayerUtil.isAbilityCanInstantlyBuild(player);
    }

    public void setAbilityCanInstantlyBuild(boolean canInstantlyBuild) {
        PlayerUtil.setAbilityCanInstantlyBuild(player, canInstantlyBuild);
    }

    public boolean isAbilityCanFly() {
        return PlayerUtil.isAbilityCanFly(player);
    }

    public void setAbilityCanFly(boolean canFly) {
        PlayerUtil.setAbilityCanFly(player, canFly);
    }

    public void setMetadata(Plugin plugin, String key, Object value) {
        PlayerUtil.setMetadata(plugin, player, key, value);
    }

    public <T> T getMetadataValue(String key) {
        return PlayerUtil.getMetadata(player, key);
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PlayerUtil.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendActionBar(String actionBar) {
        PlayerUtil.sendActionBar(player, actionBar);
    }

    public void sendTimedActionBar(String actionBar, int stayInSeconds) {
        PlayerUtil.sendTimedActionBar(player, actionBar, stayInSeconds);
    }

    public void sendTablist(String header, String footer) {
        PlayerUtil.sendTablist(player, header, footer);
    }

    public void sendTablist(List<String> header, List<String> footer) {
        PlayerUtil.sendTablist(player, header, footer);
    }

    public void playSound(Sound sound, double volume, double distortion) {
        player.playSound(getLocation(), sound, (float) volume, (float) distortion);
    }

    public void playSound(Location location, Sound sound, double volume, double distortion) {
        location.getWorld().playSound(location, sound, (float) volume, (float) distortion);
    }

    public void playParticle(ParticleEffect particle, Location loc, double radiusX, double radiusY, double radiusZ, double speed, int amount) {
        particle.display((float) radiusX, (float) radiusY, (float) radiusZ, (float) speed, amount, loc, player);
    }

    public boolean hasItemInHand() {
        ItemStack itemInHand = getItemInHand();
        return itemInHand != null && itemInHand.getType() != Material.AIR;
    }

    public boolean hasItemOnCursor() {
        ItemStack itemOnCursor = getItemOnCursor();
        return itemOnCursor != null && itemOnCursor.getType() != Material.AIR;
    }

    public boolean hasEmptySlots() {
        return getInventory().firstEmpty() != -1;
    }

    public void setHelmet(ItemStack itemStack) {
        getInventory().setHelmet(itemStack);
    }

    public void setHelmet(Material material) {
        getInventory().setHelmet(new MakeItem(material).build());
    }

    public ItemStack getHelmet() {
        return getInventory().getHelmet();
    }

    public void setChestplate(ItemStack itemStack) {
        getInventory().setChestplate(itemStack);
    }

    public void setChestplate(Material material) {
        getInventory().setChestplate(new MakeItem(material).build());
    }

    public ItemStack getChestplate() {
        return getInventory().getChestplate();
    }

    public void setLeggings(ItemStack itemStack) {
        getInventory().setLeggings(itemStack);
    }

    public void setLeggings(Material material) {
        getInventory().setLeggings(new MakeItem(material).build());
    }

    public ItemStack getLeggings() {
        return getInventory().getLeggings();
    }

    public void setBoots(ItemStack itemStack) {
        getInventory().setBoots(itemStack);
    }

    public void setBoots(Material material) {
        getInventory().setBoots(new MakeItem(material).build());
    }

    public ItemStack getBoots() {
        return getInventory().getBoots();
    }

    public boolean isEmptyInventory(boolean armor) {
        for (ItemStack item : getInventory().getContents()) {

            if (item != null && item.getType() != Material.AIR) return false;
        }

        if (!armor) return true;

        return getHelmet() == null
                && getChestplate() == null
                && getLeggings() == null
                && getBoots() == null;
    }

    public boolean isEmptyInventory() {
        for (ItemStack item : getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR)
                return false;
        }

        return getHelmet() == null
                && getChestplate() == null
                && getLeggings() == null
                && getBoots() == null;
    }

    public boolean containsItem(Material material, String name, boolean parse) {
        if (!player.getInventory().contains(material)) return false;

        for (ItemStack item : getInventory()) {
			
			if (item == null || item.getType() != material) continue;

            if (parse) {

                if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;

                if (TXT.parse(item.getItemMeta().getDisplayName()).equals(TXT.parse(name))) return true;

            } else {

                if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;

                if (item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(name)) return true;

            }
        }

        return false;
    }

    public boolean containsItem(Material material, String name, String... lore) {
        if (!player.getInventory().contains(material)) return false;

        for (ItemStack item : getInventory()) {

			if (item == null || item.getType() != material) continue;

            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
			
            if (item.getItemMeta().getDisplayName().equals(name) && item.getItemMeta().hasLore()) {

                List<String> loreList = item.getItemMeta().getLore();
                List<String> checkLore = Arrays.asList(lore.clone());

                if (checkLore.containsAll(loreList)) return true;
            }
        }

        return false;
    }

    // Verifica se o jogador está se movendo (CONTRANDO COM O MOVIMENTO DOS OLHOS)
    public boolean isMovingFull(boolean both) {
        if (player == null) return false;

        if (!DurkCore.durkCore.playersMoving.containsKey(player.getUniqueId())) return false;

        DurkMoving durkMoving = DurkCore.durkCore.playersMoving.get(player.getUniqueId());

        return both ? durkMoving.isBody() && durkMoving.isEyes() : durkMoving.isBody() || durkMoving.isEyes();
    }

    // Verifica se o jogador está se movendo (IGNORANDO O MOVIMENTO DOS OLHOS)
    public boolean isMovingBody() {
        if (player == null) return false;

        if (!DurkCore.durkCore.playersMoving.containsKey(player.getUniqueId())) return false;

        DurkMoving durkMoving = DurkCore.durkCore.playersMoving.get(player.getUniqueId());

        return durkMoving.isBody();
    }

    // Verifica se o jogador está se movendo o mouse.
    public boolean isMovingEyes() {
        if (player == null) return false;

        if (!DurkCore.durkCore.playersMoving.containsKey(player.getUniqueId())) return false;

        DurkMoving durkMoving = DurkCore.durkCore.playersMoving.get(player.getUniqueId());

        return durkMoving.isEyes();
    }


    /*

    DEFAULT METHODS

     */

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        player.setDisplayName(displayName);
    }

    @Override
    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    @Override
    public void setPlayerListName(String playerListName) {
        player.setPlayerListName(playerListName);
    }

    @Override
    public void setCompassTarget(Location target) {
        player.setCompassTarget(target);
    }

    @Override
    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public void sendRawMessage(String rawMessage) {
        player.sendRawMessage(rawMessage);
    }

    @Override
    public void kickPlayer(String reason) {
        player.kickPlayer(reason);
    }

    @Override
    public void chat(String message) {
        player.chat(message);
    }

    @Override
    public boolean performCommand(String command) {
        return player.performCommand(command);
    }

    @Override
    public boolean isSneaking() {
        return player.isSneaking();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        player.setSneaking(sneaking);
    }

    @Override
    public boolean isSprinting() {
        return player.isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        player.setSprinting(sprinting);
    }

    @Override
    public void saveData() {
        player.saveData();
    }

    @Override
    public void loadData() {
        player.loadData();
    }

    @Override
    public void setSleepingIgnored(boolean sleepingIgnored) {
        player.setSleepingIgnored(sleepingIgnored);
    }

    @Override
    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    @Deprecated
    @Override
    public void playNote(Location location, byte instrument, byte note) {
        player.playNote(location, instrument, note);
    }

    @Override
    public void playNote(Location location, Instrument instrument, Note note) {
        player.playNote(location, instrument, note);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float distortion) {
        player.playSound(location, sound, volume, distortion);
    }

    @Override
    public void playSound(Location location, String soundName, float volume, float distortion) {
        player.playSound(location, soundName, volume, distortion);
    }

    @Deprecated
    @Override
    public void playEffect(Location location, Effect effect, int data) {
        player.playEffect(location, effect, data);
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        player.playEffect(location, effect, data);
    }

    @Deprecated
    @Override
    public void sendBlockChange(Location location, Material material, byte data) {
        player.sendBlockChange(location, material, data);
    }

    @Deprecated
    @Override
    public boolean sendChunkChange(Location location, int sx, int sy, int sz, byte[] data) {
        return player.sendChunkChange(location, sx, sy, sz, data);
    }

    @Deprecated
    @Override
    public void sendBlockChange(Location location, int materialID, byte data) {
        player.sendBlockChange(location, materialID, data);
    }

    @Override
    public void sendSignChange(Location location, String[] lines) throws IllegalArgumentException {
        player.sendSignChange(location, lines);
    }

    @Override
    public void sendMap(MapView mapView) {
        player.sendMap(mapView);
    }

    @Override
    public void updateInventory() {
        player.updateInventory();
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        player.awardAchievement(achievement);
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        player.removeAchievement(achievement);
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return player.hasAchievement(achievement);
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        player.decrementStatistic(statistic, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        player.setStatistic(statistic, amount);
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        player.setStatistic(statistic, material, amount);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        player.decrementStatistic(statistic, entityType, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int amount) {
        player.setStatistic(statistic, entityType, amount);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        player.setPlayerTime(time, relative);
    }

    @Override
    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    @Override
    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    @Override
    public void setPlayerWeather(WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    @Override
    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        player.giveExpLevels(amount);
    }

    @Override
    public float getExp() {
        return player.getExp();
    }

    @Override
    public void setExp(float amount) {
        player.setExp(amount);
    }

    @Override
    public int getLevel() {
        return player.getLevel();
    }

    @Override
    public void setLevel(int amount) {
        player.setLevel(amount);
    }

    @Override
    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    @Override
    public void setTotalExperience(int amount) {
        player.setTotalExperience(amount);
    }

    @Override
    public float getExhaustion() {
        return player.getExhaustion();
    }

    @Override
    public void setExhaustion(float amount) {
        player.setExhaustion(amount);
    }

    @Override
    public float getSaturation() {
        return player.getSaturation();
    }

    @Override
    public void setSaturation(float amount) {
        player.setSaturation(amount);
    }

    @Override
    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int amount) {
        player.setFoodLevel(amount);
    }

    @Override
    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        player.setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        player.setBedSpawnLocation(location, force);
    }

    @Override
    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        player.setAllowFlight(allowFlight);
    }

    @Override
    public void hidePlayer(Player player) {
        this.player.hidePlayer(player);
    }

    @Override
    public void showPlayer(Player player) {
        this.player.showPlayer(player);
    }

    @Override
    public boolean canSee(Player player) {
        return this.player.canSee(player);
    }

    @Deprecated
    @Override
    public boolean isOnGround() {
        return player.isOnGround();
    }

    @Override
    public boolean isFlying() {
        return player.isFlying();
    }

    @Override
    public void setFlying(boolean flying) {
        player.setFlying(flying);
    }

    @Override
    public void setFlySpeed(float flySpeed) throws IllegalArgumentException {
        player.setFlySpeed(flySpeed);
    }

    @Override
    public void setWalkSpeed(float walkSpeed) throws IllegalArgumentException {
        player.setWalkSpeed(walkSpeed);
    }

    @Override
    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    @Deprecated
    @Override
    public void setTexturePack(String url) {
        player.setTexturePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        player.setResourcePack(url);
    }

    @Override
    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    @Override
    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    @Override
    public void setHealthScaled(boolean healthScaled) {
        player.setHealthScaled(healthScaled);
    }

    @Override
    public void setHealthScale(double healthScale) throws IllegalArgumentException {
        player.setHealthScale(healthScale);
    }

    @Override
    public double getHealthScale() {
        return player.getHealthScale();
    }

    @Override
    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        player.setSpectatorTarget(entity);
    }

    @Deprecated
    @Override
    public void sendTitle(String title, String subtitle) {
        player.sendTitle(title, subtitle);
    }

    @Deprecated
    @Override
    public void resetTitle() {
        player.resetTitle();
    }

    @Override
    public Spigot spigot() {
        return player.spigot();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean isBanned() {
        return player.isBanned();
    }

    @Deprecated
    @Override
    public void setBanned(boolean banned) {
        player.setBanned(banned);
    }

    @Override
    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        player.setWhitelisted(whitelisted);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    @Override
    public Map<String, Object> serialize() {
        return player.serialize();
    }

    @Override
    public boolean isConversing() {
        return player.isConversing();
    }

    @Override
    public void acceptConversationInput(String conversationInput) {
        player.acceptConversationInput(conversationInput);
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return player.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        player.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    @Override
    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property property, int value) {
        return player.setWindowProperty(property, value);
    }

    @Override
    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return player.openWorkbench(location, force);
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return player.openEnchanting(location, force);
    }

    @Override
    public void openInventory(InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    @Override
    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    @Override
    public void setItemOnCursor(ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    @Override
    public boolean isSleeping() {
        return player.isSleeping();
    }

    @Override
    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    @Override
    public GameMode getGameMode() {
        return player.getGameMode();
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    @Override
    public boolean isBlocking() {
        return player.isBlocking();
    }

    @Override
    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    @Override
    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return player.getEyeHeight(ignorePose);
    }

    @Override
    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return player.getLineOfSight(transparent, maxDistance);
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return player.getLineOfSight(transparent, maxDistance);
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        return player.getTargetBlock(transparent, maxDistance);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return player.getTargetBlock(transparent, maxDistance);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return player.getLastTwoTargetBlocks(transparent, maxDistance);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return player.getLastTwoTargetBlocks(transparent, maxDistance);
    }

    @Deprecated
    @Override
    public Egg throwEgg() {
        return player.throwEgg();
    }

    @Deprecated
    @Override
    public Snowball throwSnowball() {
        return player.throwSnowball();
    }

    @Deprecated
    @Override
    public Arrow shootArrow() {
        return player.shootArrow();
    }

    @Override
    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int remainingAir) {
        player.setRemainingAir(remainingAir);
    }

    @Override
    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int maximumAir) {
        player.setMaximumAir(maximumAir);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int maximumNoDamageTicks) {
        player.setMaximumNoDamageTicks(maximumNoDamageTicks);
    }

    @Override
    public double getLastDamage() {
        return player.getLastDamage();
    }

    @Override
    public void setLastDamage(double lastDamage) {
        player.setLastDamage(lastDamage);
    }

    @Override
    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int noDamageTicks) {
        player.setNoDamageTicks(noDamageTicks);
    }

    @Override
    public Player getKiller() {
        return player.getKiller();
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    @Override
    public boolean addPotionEffect(PotionEffect potionEffect, boolean force) {
        return player.addPotionEffect(potionEffect, force);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> potionEffects) {
        return player.addPotionEffects(potionEffects);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(Entity entity) {
        return player.hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean removeWhenFarAway) {
        player.setRemoveWhenFarAway(removeWhenFarAway);
    }

    @Override
    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean canPickupItems) {
        player.setCanPickupItems(canPickupItems);
    }

    @Override
    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return player.isLeashed();
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(Entity entity) {
        return player.setLeashHolder(entity);
    }

    @Override
    public void damage(double amount) {
        player.damage(amount);
    }

    @Override
    public void damage(double amount, Entity entity) {
        player.damage(amount, entity);
    }

    @Override
    public double getHealth() {
        return player.getHealth();
    }

    @Override
    public void setHealth(double health) {
        player.setHealth(health);
    }

    @Override
    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        player.setMaxHealth(maxHealth);
    }

    @Override
    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public Location getLocation(Location location) {
        return player.getLocation(location);
    }

    @Override
    public void setVelocity(Vector velocity) {
        player.setVelocity(velocity);
    }

    @Override
    public Vector getVelocity() {
        return player.getVelocity();
    }

    @Override
    public World getWorld() {
        return player.getWorld();
    }

    @Override
    public boolean teleport(Location location) {
        return player.teleport(location);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    @Override
    public boolean teleport(Entity entity) {
        return player.teleport(entity);
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    @Override
    public List<Entity> getNearbyEntities(double rx, double ry, double rz) {
        return player.getNearbyEntities(rx, ry, rz);
    }

    @Override
    public int getEntityId() {
        return player.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return player.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int fireTicks) {
        player.setFireTicks(fireTicks);
    }

    @Override
    public void remove() {
        player.remove();
    }

    @Override
    public boolean isDead() {
        return player.isDead();
    }

    @Override
    public boolean isValid() {
        return player.isValid();
    }

    @Override
    public Server getServer() {
        return player.getServer();
    }

    @Override
    public Entity getPassenger() {
        return player.getPassenger();
    }

    @Override
    public boolean setPassenger(Entity entity) {
        return player.setPassenger(entity);
    }

    @Override
    public boolean isEmpty() {
        return player.isEmpty();
    }

    @Override
    public boolean eject() {
        return player.eject();
    }

    @Override
    public float getFallDistance() {
        return player.getFallDistance();
    }

    @Override
    public void setFallDistance(float fallDistance) {
        player.setFallDistance(fallDistance);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return player.getTicksLived();
    }

    @Override
    public void setTicksLived(int ticksLived) {
        player.setTicksLived(ticksLived);
    }

    @Override
    public void playEffect(EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    @Override
    public EntityType getType() {
        return player.getType();
    }

    @Override
    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    @Override
    public Entity getVehicle() {
        return player.getVehicle();
    }

    @Override
    public void setCustomName(String customName) {
        player.setCustomName(customName);
    }

    @Override
    public String getCustomName() {
        return player.getCustomName();
    }

    @Override
    public void setCustomNameVisible(boolean customNameVisible) {
        player.setCustomNameVisible(customNameVisible);
    }

    @Override
    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        player.sendMessage(messages);
    }

    @Override
    public void setMetadata(String key, MetadataValue metadataValue) {
        player.setMetadata(key, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String key) {
        return player.getMetadata(key);
    }

    @Override
    public boolean hasMetadata(String key) {
        return player.hasMetadata(key);
    }

    @Override
    public void removeMetadata(String key, Plugin plugin) {
        player.removeMetadata(key, plugin);
    }

    @Override
    public boolean isPermissionSet(String permission) {
        return player.isPermissionSet(permission);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return player.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return player.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean ticks) {
        return player.addAttachment(plugin, name, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return player.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return player.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return player.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void setOp(boolean op) {
        player.setOp(op);
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String channel, byte[] message) {
        player.sendPluginMessage(plugin, channel, message);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectileClass) {
        return player.launchProjectile(projectileClass);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectileClass, Vector velocity) {
        return player.launchProjectile(projectileClass, velocity);
    }

    /*
     * DurkMoving class.
     */

    public static class DurkMoving {

        private Location location;
        private boolean body, eyes;

        public DurkMoving(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public boolean isBody() {
            return body;
        }

        public void setBody(boolean body) {
            this.body = body;
        }

        public boolean isEyes() {
            return eyes;
        }

        public void setEyes(boolean eyes) {
            this.eyes = eyes;
        }
    }
}