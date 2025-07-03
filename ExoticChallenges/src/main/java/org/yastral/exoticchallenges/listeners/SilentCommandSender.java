package org.yastral.exoticchallenges.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class SilentCommandSender implements CommandSender {
    private final Player player;

    public SilentCommandSender(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(@NotNull String message) {

    }

    @Override
    public void sendMessage(@NotNull String[] messages) {

    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String message) {

    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String[] messages) {

    }

    @Override
    public @NotNull Server getServer() {
        return player.getServer();
    }

    @Override
    public @NotNull String getName() {
        return player.getName();
    }

    @NotNull
    @Override
    public Spigot spigot() {
        return player.spigot();
    }

    @Override
    public @NotNull Component name() {
        return null;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return player.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(org.bukkit.permissions.Permission perm) {
        return player.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return player.hasPermission(name);
    }

    @Override
    public boolean hasPermission(org.bukkit.permissions.Permission perm) {
        return player.hasPermission(perm);
    }



    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return player.addAttachment(plugin, name, value);
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(Plugin plugin) {
        return player.addAttachment(plugin);
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return player.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public org.bukkit.permissions.PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return player.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(org.bukkit.permissions.PermissionAttachment attachment) {
        player.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    @Override
    public java.util.Set<org.bukkit.permissions.PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void setOp(boolean value) {
        player.setOp(value);
    }


}

