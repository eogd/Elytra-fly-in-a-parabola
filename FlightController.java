package com.example.elytraflight.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FlightController extends BukkitRunnable {
    private final Player player;
    private final JavaPlugin plugin;
    private int timer = 0;
    private Vector lockedDirection; 

    public FlightController(Player player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (timer++ >= 60) { 
            lockPreciseDirection();
            executeDash();
            cancel();
            return;
        }
        updateRealTimePreview();
    }

    private void lockPreciseDirection() {
        Vector eyeDirection = player.getEyeLocation().getDirection();
        this.lockedDirection = eyeDirection.normalize()
            .normalize()
            .normalize();
    }

    private void updateRealTimePreview() {
        Vector currentDir = player.getEyeLocation().getDirection().normalize();
        Location preview = player.getEyeLocation().add(currentDir.multiply(200));
        

        player.spawnParticle(
            Particle.DUST,
            preview,
            5,
            0.1, 0.1, 0.1,
            new Particle.DustOptions(Color.RED, 2.0f)
        );
    }

    private void executeDash() {

        Vector velocity = lockedDirection.clone();
        if (velocity.lengthSquared() < 0.0001) {
            velocity = player.getEyeLocation().getDirection();
        }
        

        velocity.multiply(35);
        player.setVelocity(velocity);
        

        player.getWorld().spawnParticle(
            Particle.FIREWORK,
            player.getLocation(),
            1000,
            2.0, 2.0, 2.0,
            0.8
        );
        player.playSound(player.getLocation(), 
            Sound.ENTITY_ENDER_DRAGON_FLAP, 3f, 0.1f);
    }

    public void start() {
        this.runTaskTimer(plugin, 0, 1); 
    }
}