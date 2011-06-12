package com.ementalo.arrowlimiter;

import java.util.Calendar;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;


public class ArrowLimiterBlockListener extends BlockListener
{
	private ArrowLimiter arrLim = new ArrowLimiter();
	double delay;

	public ArrowLimiterBlockListener(ArrowLimiter parent)
	{
		this.arrLim = parent;
	}

	@Override
	public void onBlockDispense(BlockDispenseEvent event)
	{
		delay = arrLim.config.getDouble("dispenserdelay", 0.1) * 1000D;
		Block dispenser = event.getBlock();
		if (event.isCancelled() || event.getItem().getType() != Material.ARROW) return;
		{
			if (arrLim.dispenserLimit.get(dispenser) == null)
			{
				arrLim.dispenserLimit.put(dispenser, 0.0D);
			}
			double now = Calendar.getInstance().getTimeInMillis();
			double left = arrLim.dispenserLimit.get(dispenser) + delay - now;
			arrLim.dispenserLimit.put(dispenser, now);
			if (left > 0)
			{
				event.setCancelled(true);
				return;
			}

		}
	}
}

