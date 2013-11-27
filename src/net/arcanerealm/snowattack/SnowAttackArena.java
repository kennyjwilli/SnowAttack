
package net.arcanerealm.snowattack;

import info.jeppes.ZoneWorld.ZoneWorld;
import java.util.ArrayList;
import net.vectorgaming.varenas.ArenaAPI;
import net.vectorgaming.varenas.ArenaManager;
import net.vectorgaming.varenas.framework.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Kenny
 */
public class SnowAttackArena extends Arena
{
    private int KIT_TASK_ID;
    private ArrayList<Player> alivePlayers;
    
    public SnowAttackArena(String name, String map, ZoneWorld world)
    {
        super(name, map, world);
    }
    
    @Override
    public void start()
    {
        super.start();
        alivePlayers = (ArrayList<Player>) getPlayers().clone();
        
        KIT_TASK_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SnowAttackAPI.getPlugin(), new Runnable()
        {
            @Override
            public void run()
            {
                for(Player p : getPlayers())
                {
                    getSpawnKit().giveKit(p, true);
                }
            }
        }, 100L, 100L);
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
        Bukkit.getScheduler().cancelTask(KIT_TASK_ID);
        reloadArena(10);
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
        Bukkit.getScheduler().cancelTask(KIT_TASK_ID);
    }

    @Override
    public void sendEndMessage()
    {
        
    }

    @Override
    public void onDeath(Player dead, Entity killer)
    {
        ArenaAPI.resetPlayerState(dead);
        dead.teleport(getSpectatorBox().getSpawn());
        dead.getInventory().clear();
        dead.sendMessage("You are out!");
        getSpectatorBox().addSpectator(dead.getName(), true);
        alivePlayers.remove(dead);
        if(alivePlayers.size() == 1)
        {
            getChannel().sendChannelMessage("Game over! Player "+alivePlayers.get(0).getName()+" has won!");
            getChannel().sendChannelMessage("Match closing in 10 seconds.");
            Bukkit.getScheduler().scheduleSyncDelayedTask(SnowAttackAPI.getPlugin(), new Runnable()
            {

                @Override
                public void run()
                {
                    end();
                }
            }, 200L);
        }
    }

    @Override
    public Location onRespawn(Player player)
    {
        player.setHealthScale(6.0D);
        player.setHealth(6.0D);
        return getSpawnLocation(player);
    }
    
    @Override
    public void onJoin(Player player)
    {
        super.onJoin(player);
        player.teleport(getLobby().getSpawn());
        player.setHealthScaled(true);
        player.setHealthScale(6.0D);
    }
    
    @Override
    public void onQuit(PlayerQuitEvent event)
    {
        super.onQuit(event);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(true);
    }
    
    @Override
    public boolean canJoin(Player p)
    {
        return super.canJoin(p) && !isRunning();
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
            setInvisible(hurt);
            hurt.teleport(ArenaManager.getAreanFramework(this.getMap()).getPoint3D("waiting").toLocation(getWorld().getCraftWorld()));
            
            //Teleports player to the waiting location for 5 seconds
            Bukkit.getScheduler().scheduleSyncDelayedTask(SnowAttackAPI.getPlugin(), new Runnable()
            {

                @Override
                public void run()
                {
                    //dead.setHealthScale(6.0D);
                    hurt.teleport(getSpawnLocation(hurt));
                    setVisible(hurt);
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
        
//        p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
//        p.updateInventory();
    }
    
    private void setInvisible(Player p)
    {
        for(Player pl : getPlayers())
        {
            if(pl != p)
            {
                pl.hidePlayer(p);
            }
        }
    }
    
    private void setVisible(Player p)
    {
        for(Player pl : getPlayers())
        {
            if(pl != p)
            {
                pl.showPlayer(p);
            }
        }
    }
}
