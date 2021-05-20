package de.blu.loop;

import de.blu.loop.command.LoopCommand;
import de.blu.loop.command.ServerLoopCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loop extends JavaPlugin {

  @Override
  public void onEnable() {
    // Register Command
    this.getCommand("loop").setExecutor(new LoopCommand(this));
    this.getCommand("serverloop").setExecutor(new ServerLoopCommand(this));
  }
}
