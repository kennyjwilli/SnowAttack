
package net.arcanerealm.snowattack;

import info.jeppes.ZoneWorld.ZoneWorld;
import net.vectorgaming.varenas.ArenaAPI;
import net.vectorgaming.varenas.ArenaManager;
import net.vectorgaming.varenas.framework.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Kenny
 */
public class SnowAttackArena extends Arena
{
    public SnowAttackArena(String name, String map, ZoneWorld world)
    {
        super(name, map, world);
        world.setPVP(true);
    }
    
    @Override
    public void start()
    {
        super.start();
        for(Player p : getPlayers())
        {
            p.setHealthScaled(true);
            p.setHealthScale(6.0D);
        }
    }
    
    @Override
    public void end()
    {
        super.end();
        for(Player p : getPlayers())
        {
            p.setHealthScale(20D);
            p.setHealthScaled(false);
        }
    }
    
    @Override
    public void forceStop()
    {
        super.forceStop();
        for(Player p : getPlayers())
        {
            p.setHealthScale(20D);
            p.setHealthScaled(false);
        }
    }

    @Override
    public void sendEndMessage()
    {
        
    }

    @Override
    public void onDeath(Player dead, Entity killer)
    {
        System.out.println("DEAD"+dead.getName());
        ArenaAPI.resetPlayerState(dead);
        dead.teleport(getSpectatorBox().getSpawn());
        dead.sendMessage("You are out!");
        getSpectatorBox().addSpectator(dead.getName(), true);
    }

    @Override
    public Location onRespawn(Player player)
    {
        player.setHealthScale(6.0D);
        player.setHealth(6.0D);
        return getSpawnLocation(player);
    }

    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Snowball))
        {
            return;
        }
        if(!(event.getEntity() instanceof Player))
        {
            return;
        }

        if(!(((Snowball) event.getDamager()).getShooter() instanceof Player))
        {
            return;
        }

        Player shooter = (Player) ((Snowball) event.getDamager()).getShooter();

        final Player hurt = (Player) event.getEntity();
        double healthScale = hurt.getHealthScale();

        if(healthScale - 2.0D <= 0)
        {
            onDeath(hurt, shooter);
        }else
        {
            //hurt.setHealth(hurt.getHealth() - damage);
            hurt.setHealthScale(hurt.getHealthScale() - 2.0D);
            hurt.teleport(ArenaManager.getAreanFramework(this.getMap()).getPoint3D("waiting").toLocation(getWorld().getCraftWorld()));
            
            //Teleports player to the waiting location for 5 seconds
            Bukkit.getScheduler().scheduleSyncDelayedTask(SnowAttackAPI.getPlugin(), new Runnable()
            {

                @Override
                public void run()
                {
                    //dead.setHealthScale(6.0D);
                    hurt.teleport(getSpawnLocation(hurt));
                }
            }, 100L);
        }
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if(!(event.getEntity() instanceof Snowball))
        {
            return;
        }
        Snowball snowball = (Snowball) event.getEntity();
        if(!(snowball.getShooter() instanceof Player))
        {
            return;
        }
        /*
        Maybe add if the player is jumping the velocity of the snowball increases
        */
        Player p = (Player) snowball.getShooter();
        
        if(this.getSpectatorBox().isSpectator(p.getName()))
        {
            event.setCancelled(true);
            return;
        }
        
        p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
        p.updateInventory();
    }

}
