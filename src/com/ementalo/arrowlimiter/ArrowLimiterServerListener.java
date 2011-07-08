package com.ementalo.arrowlimiter;

import java.util.logging.Level;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;


public class ArrowLimiterServerListener extends ServerListener
{
	ArrowLimiter parent = null;

	public ArrowLimiterServerListener(ArrowLimiter parent)
	{
		this.parent = parent;
	}

	@Override
	public void onPluginEnable(PluginEnableEvent event)
	{
		if (parent.permPlugin != null) return;

		String pluginName = event.getPlugin().getDescription().getName();
		if (pluginName.equalsIgnoreCase("Permissions"))
		{
			parent.setPermPlugin(event.getPlugin());
			ArrowLimiter.logger.log(Level.INFO, "[ArrowLimiter] Found " + pluginName + ". Using it for permissions");
		}
	}

	@Override
	public void onPluginDisable(PluginDisableEvent event)
	{
		if (parent.permPlugin == null) return;
		String pluginName = event.getPlugin().getDescription().getName();
		if (pluginName.equalsIgnoreCase("Permissions"))
		{
			parent.setPermPlugin(null);
			ArrowLimiter.logger.log(Level.INFO, "[ArrowLimiter] " + pluginName + " disabled, using OPS.txt");
		}
	}
}
