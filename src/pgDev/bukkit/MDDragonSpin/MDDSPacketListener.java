package pgDev.bukkit.MDDragonSpin;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet32EntityLook;
import net.minecraft.server.Packet33RelEntityMoveLook;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.packet.listener.PacketListener;
import org.getspout.spoutapi.packet.standard.MCPacket;
import org.getspout.spoutapi.packet.standard.MCPacketUnknown;

public class MDDSPacketListener implements PacketListener {
	private final MDDSMain plugin;
	
	public MDDSPacketListener(final MDDSMain pluginI) {
		plugin = pluginI;
	}
	
	public boolean checkPacket(Player player, MCPacket packet) {
		Packet packt = (Packet) ((MCPacketUnknown) packet).getRawPacket();
		if (packet.getId() == 32) {
			Packet32EntityLook pkt = (Packet32EntityLook) packt;
			if (plugin.isDisguisedDragon(player.getWorld(), pkt.a)) {
				//pkt.b = plugin.spin(pkt.b);
				
				if (plugin.fixedPackets.contains(packt)) {
					plugin.fixedPackets.remove(packt);
					//System.out.println("32 dragon packet sent");
				} else {
					plugin.sendEntityLookSpin(player, pkt);
					//System.out.println("32 dragon packet queued");
					return false;
				}
			}
		} else if (packet.getId() == 33) {
			Packet33RelEntityMoveLook pkt = (Packet33RelEntityMoveLook) packt;
			if (plugin.isDisguisedDragon(player.getWorld(), pkt.a)) {
				//pkt.e = plugin.spin(pkt.e);
				
				if (plugin.fixedPackets.contains(packt)) {
					plugin.fixedPackets.remove(packt);
					//System.out.println("33 dragon packet sent");
				} else {
					plugin.sendEntityLookSpin(player, pkt);
					//System.out.println("33 dragon packet queued");
					return false;
				}
			}
		} else if (packet.getId() == 24) {
			Packet24MobSpawn pkt = (Packet24MobSpawn) packt;
			if (plugin.isDisguisedDragon(player.getWorld(), pkt.a)) {
				//pkt.f = plugin.spin(pkt.f);
				
				if (plugin.fixedPackets.contains(packt)) {
					plugin.fixedPackets.remove(packt);
					//System.out.println("24 dragon packet sent");
				} else {
					plugin.sendEntityLookSpin(player, pkt);
					//System.out.println("24 dragon packet queued");
					return false;
				}
			}
		}
		return true;
	}

}
