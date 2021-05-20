package de.blu.loop.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class LoopCommand implements CommandExecutor {

  private JavaPlugin plugin;

  public LoopCommand(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }

    Player player = (Player) sender;

    if (args.length < 3) {
      this.sendUsage(player);
      return false;
    }

    int amount;
    int intervalTicks;

    try {
      amount = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      sender.sendMessage(ChatColor.RED + "Amount needs to be a number!");
      return false;
    }

    try {
      intervalTicks = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      sender.sendMessage(ChatColor.RED + "interval ticks needs to be a number!");
      return false;
    }

    args = Arrays.copyOfRange(args, 2, args.length);
    AtomicReference<String> playerCommand = new AtomicReference<>(String.join(" ", args));

    if (!playerCommand.get().startsWith("/")) {
      playerCommand.set("/" + playerCommand.get());
    }

    AtomicInteger currentTick = new AtomicInteger(0);
    new BukkitRunnable() {
      @Override
      public void run() {
        if (currentTick.incrementAndGet() > amount) {
          this.cancel();
          return;
        }

        player.chat(playerCommand.get().replaceAll("\\$tick", String.valueOf(currentTick.get())));
      }
    }.runTaskTimer(this.plugin, 0, intervalTicks);

    return true;
  }

  private void sendUsage(Player player) {
    player.sendMessage(ChatColor.YELLOW + "Usage: /loop <amount> <interval ticks> <playercommand>");
  }
}
