package com.ementalo.arrowlimiter;

import java.util.Calendar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;


public class ArrowLimiterPlayerListener extends PlayerListener
{
	private ArrowLimiter arrLim = new ArrowLimiter();
	double delay;

	public ArrowLimiterPlayerListener(ArrowLimiter parent)
	{
		this.arrLim = parent;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		delay = arrLim.config.getDouble("timedelay", 0.1) * 1000D;
		Player archer = event.getPlayer();
		if (event.isCancelled() & event.getAction() != Action.RIGHT_CLICK_AIR|| event.hasBlock() || arrLim.hasPermission("arrowlimiter.exempt", archer)) return;
		{
			if (event.getItem().getType() == Material.BOW && event.getPlayer().getInventory().contains(Material.ARROW))
			{
				if (arrLim.timeLimit.get(archer) == null)
				{
					arrLim.timeLimit.put(archer, 0.0D);
				}

				double now = Calendar.getInstance().getTimeInMillis();
				double left = arrLim.timeLimit.get(archer) + delay - now;
				arrLim.timeLimit.put(archer, now);
				if (left > 0)
				{
					event.getPlayer().sendMessage("You have to wait " + delay / 1000D + " second(s) between arrows");
					event.setCancelled(true);
					event.getPlayer().updateInventory();
					return;
				}

			}
		}
	}
}
