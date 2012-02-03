package pgDev.bukkit.MDDragonSpin;

import java.lang.reflect.Field;
import java.util.LinkedList;

import me.desmin88.mobdisguise.MobDisguise;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.Packet32EntityLook;
import net.minecraft.server.Packet33RelEntityMoveLook;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

public class MDDSMain extends JavaPlugin {
	// Packet Listener
	public final MDDSPacketListener packetListener = new MDDSPacketListener(this);
	
	// Packets to listen to
	static final int[] listenPackets = new int[] {24, 32, 33}; // The entity look related packets
	
	// Fixed packet list
	public LinkedList<Packet> fixedPackets = new LinkedList<Packet>();
	
	public void onEnable() {
		// Tell spout we want packets
		for (int id : listenPackets) {
			SpoutManager.getPacketManager().addListener(id, packetListener);
		}
		
		PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public void onDisable() {
		System.out.println("MobDisguiseDragonSpin disabled!");
	}
	
	// Player disguise related functions
	public boolean isDisguisedDragon(World world, int id) {
		for (Player player : world.getPlayers()) {
			if (player.getEntityId() == id && // Is a player
					MobDisguise.disList.contains(player.getName()) && // Is disguised
						MobDisguise.playerMobId.get(player.getName()) == 63) { // Is an EnderDragon
				return true;
			}
		}
		return false;
	}
	
	// Get player from ID
	public Player getPlayer(World world, int id) {
		for (Player player : world.getPlayers()) {
			if (player.getEntityId() == id) {
				return player;
			}
		}
		return null;
	}
	// Degree function
	public byte spin(byte original) {
		return (byte) (original - 128);
	}
	
	// Packet methods
	public void sendEntityLookSpin(Player player, Packet32EntityLook packet) {
		//Packet32EntityLook pkt = new Packet32EntityLook(packet.a, spin(packet.b), packet.c);
		//Packet33RelEntityMoveLook pkt = new Packet33RelEntityMoveLook(packet.a, (byte) 0, (byte) 1, (byte) 0, spin(packet.b), packet.c);

		Packet29DestroyEntity killPkt = new Packet29DestroyEntity(packet.a);
		
		Packet24MobSpawn pkt = new Packet24MobSpawn();
		DataWatcher tmp = new DataWatcher();
		Location spot = getPlayer(player.getWorld(), packet.a).getLocation();
        pkt.a = packet.a;
        pkt.b = 63;
        pkt.c = MathHelper.floor(spot.getX() * 32.0D);
        pkt.d = MathHelper.floor(spot.getY() * 32.0D);
        pkt.e = MathHelper.floor(spot.getZ() * 32.0D);
        pkt.f = spin((byte) ((int) (spot.getYaw() * 256.0F / 360.0F)));
        pkt.g = (byte) ((int) (spot.getPitch() * 256.0F / 360.0F));;
        Field datawatcher;
        try {
            datawatcher = pkt.getClass().getDeclaredField("h");
            datawatcher.setAccessible(true);
            datawatcher.set(pkt, tmp);
            datawatcher.setAccessible(false);
        } catch (Exception e) {
            System.out.println("Error making dragon spawn look packet!");
        }
		
        fixedPackets.add(pkt);
		((CraftPlayer) player).getHandle().netServerHandler.sendPacket(killPkt);
		((CraftPlayer) player).getHandle().netServerHandler.sendPacket(pkt);
	}
	
	public void sendEntityLookSpin(Player player, Packet33RelEntityMoveLook packet) {
		Packet33RelEntityMoveLook pkt = new Packet33RelEntityMoveLook(packet.a, packet.b, packet.c, packet.d, spin(packet.e), packet.f);
		fixedPackets.add(pkt);
		((CraftPlayer) player).getHandle().netServerHandler.sendPacket(pkt);
	}
	
	public void sendEntityLookSpin(Player player, Packet24MobSpawn packet) {
		Packet24MobSpawn pkt = new Packet24MobSpawn();
		DataWatcher tmp = new DataWatcher();
        pkt.a = packet.a;
        pkt.b = packet.b;
        pkt.c = packet.c;
        pkt.d = packet.d;
        pkt.e = packet.e;
        pkt.f = spin(packet.f);
        pkt.g = packet.g;
        Field datawatcher;
        try {
            datawatcher = pkt.getClass().getDeclaredField("h");
            datawatcher.setAccessible(true);
            datawatcher.set(pkt, tmp);
            datawatcher.setAccessible(false);
        } catch (Exception e) {
            System.out.println("Error making dragon spawn packet!");
        }
		
		fixedPackets.add(pkt);
		((CraftPlayer) player).getHandle().netServerHandler.sendPacket(pkt);
	}
}
