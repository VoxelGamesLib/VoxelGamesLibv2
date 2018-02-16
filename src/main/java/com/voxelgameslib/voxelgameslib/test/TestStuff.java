package com.voxelgameslib.voxelgameslib.test;

import javax.inject.Inject;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class TestStuff implements Listener {

    @Inject
    private Plugin plugin;

    public void test() {
//        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK) {
//
//            @Override
//            public void onPacketReceiving(PacketEvent event) {
//                if (event.getPacket().getType() == PacketType.Play.Client.POSITION) {
//                    WrapperPlayClientPosition pos = new WrapperPlayClientPosition(event.getPacket());
//                    pos.setY(pos.getY() + 100);
//                    event.setPacket(pos.getHandle());
//                }
//            }
//        });
    }
}
