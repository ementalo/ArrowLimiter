package com.ementalo.arrowlimiter;

import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.*;
import org.bukkit.util.config.Configuration;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.SafeConstructor;


public class ArrowLimiter extends JavaPlugin
{
	public static final String AUTHORS = "ementalo";
	public static final Logger logger = Logger.getLogger("Minecraft");
	private static Yaml yaml = new Yaml(new SafeConstructor());
	public Configuration config = null;
	public HashMap<Player, Double> timeLimit = new HashMap<Player, Double>();
	public Object permissions = null;
	Plugin permPlugin = null;
	Boolean isGm = false;

	@SuppressWarnings("LoggerStringConcat")
	public void onEnable()
	{
		config = getConfiguration();
		ArrowLimiterPlayerListener playerListener = new ArrowLimiterPlayerListener(this);
		ArrowLimiterEntityListener entityListener = new ArrowLimiterEntityListener(this);
		ArrowLimiterServerListener serverListener = new ArrowLimiterServerListener(this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.High, this);
		getServer().getPluginManager().registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.High, this);
		getServer().getPluginManager().registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Low, this);
		getServer().getPluginManager().registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Low, this);
		logger.info("Loaded " + this.getDescription().getName() + " build " + this.getDescription().getVersion() + " maintained by " + AUTHORS);
		try
		{
			LoadSettings();
		}
		catch (Throwable ex)
		{
			logger.log(Level.SEVERE, "Error with processing config file", ex);
		}
	}

	public void onDisable()
	{
	}

	@Override
	public void onLoad()
	{
	}

	public void LoadSettings() throws Exception
	{
		if (!this.getDataFolder().exists())
		{
			this.getDataFolder().mkdirs();
		}
		config.load();
		final List<String> keys = config.getKeys(null);
		if (!keys.contains("timedelay"))
			config.setProperty("timedelay", 1.00);
		if (!keys.contains("damage"))
			config.setProperty("damage", 1);
		config.save();
		config.load();
	}

	public Boolean hasPermission(String node, Player base)
	{
		if (permPlugin == null && base.isOp())
			return true;
		if (isGm)
		{
			GroupManager gm = (GroupManager)permPlugin;
			return gm.getWorldsHolder().getWorldPermissions(base).has(base, node);

		}
		else
		{
			Permissions pm = (Permissions)permPlugin;
			return pm.getHandler().has(base, node);
		}
	}
}
