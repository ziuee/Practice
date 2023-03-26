package com.hysteria.practice.player.party.menu;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.party.enums.PartyEvent;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartyEventSelectEventMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&bSelect an event";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		buttons.put(11, new SelectEventButton(PartyEvent.FFA));
		buttons.put(13, new SelectEventButton(PartyEvent.SPLIT));
		buttons.put(15, new SelectEventButton(PartyEvent.REDROVER));
		return buttons;
	}

	@Override
	public int getSize() {
		return 3 * 9;
	}

	@AllArgsConstructor
	private static class SelectEventButton extends Button {

		private final PartyEvent partyEvent;

		@Override
		public ItemStack getButtonItem(Player player) {
			if(partyEvent == PartyEvent.FFA) {
				return new ItemBuilder(Material.QUARTZ)
					.name("&b"+ partyEvent.getName())
					.lore(CC.SB_BAR)
					.lore("&7A fight all against all")
					.lore("&7And the last one to stay alive wins")
					.lore(CC.SB_BAR)
					.build();
			} else if (partyEvent == PartyEvent.SPLIT) {
				return new ItemBuilder(Material.REDSTONE)
						.name("&b" + partyEvent.getName())
						.lore(CC.SB_BAR)
						.lore("&7The party is divided into two")
						.lore("&7Teams and they fight")
						.lore(CC.SB_BAR)
						.build();
			} else {
				return new ItemBuilder(Material.DIAMOND_AXE)
						.name("&b" + partyEvent.getName())
						.lore(CC.SB_BAR)
						.lore("&7Fight in a series of 1v1's")
						.lore("&7until there is a winner")
						.lore("")
						.lore("&c&lWIP")
						.lore(CC.SB_BAR)
						.build();
			}
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				player.sendMessage(CC.RED + "You are not in a party.");
				return;
			}

			for (Player member : profile.getParty().getListOfPlayers()) {
				Profile profileMember = Profile.get(member.getUniqueId());
				if (profileMember.getState() != ProfileState.LOBBY) {
					player.sendMessage(CC.translate("&7All players must be in spawn to start a party"));
					player.closeInventory();
					return;
				}
			}

			if(partyEvent == PartyEvent.REDROVER) return;

			new PartyEventSelectKitMenu(partyEvent).openMenu(player);
		}
	}
}
