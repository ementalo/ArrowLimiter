package com.ementalo.arrowlimiter;

import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
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
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static Yaml yaml = new Yaml(new SafeConstructor());
	public Configuration config = null;
	public HashMap<Player, Double> timeLimit = new HashMap<Player, Double>();
	public Object permissions = null;
	Plugin permPlugin = null;
	Boolean isGm = false;

	public ArrowLimiter()
	{
	}

	@SuppressWarnings("LoggerStringConcat")
	public void onEnable()
	{
		ArrowLimiterPlayerListener playerListener = new ArrowLimiterPlayerListener(this);
		ArrowLimiterEntityListener entityListener = new ArrowLimiterEntityListener(this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.High, this);
		getServer().getPluginManager().registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.High, this);
		logger.info("Loaded " + this.getDescription().getName() + " build " + this.getDescription().getVersion() + " maintained by " + AUTHORS);
		logger.log(Level.INFO, "[ArrowLimiter] Checking for permission plugins....");
		permPlugin = this.getServer().getPluginManager().getPlugin("GroupManager");
		if (permPlugin != null)
		{
			if (!this.getServer().getPluginManager().isPluginEnabled(permPlugin))
			{
				this.getServer().getPluginManager().enablePlugin(permPlugin);
			}
			logger.log(Level.INFO, "Found GroupManager. Using it for permissions");
			isGm = true;
		}
		else
		{
			permPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

			if (permPlugin != null)
			{
				if (!this.getServer().getPluginManager().isPluginEnabled(permPlugin))
				{
					this.getServer().getPluginManager().enablePlugin(permPlugin);
				}
				logger.log(Level.INFO, "Found Permissions. Using it for permissions");
			}
			else
			{
				logger.log(Level.INFO, "[ArrowLimiter] Permissions plugins not found, defaulting to OPS.txt");
			}
		}
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

	public void onLoad()
	{

	}

	public void LoadSettings() throws Exception
	{
		if (!this.getDataFolder().exists())
		{
			this.getDataFolder().mkdirs();
		}
		File arrows = new File(this.getDataFolder(), "arrowConfig.yml");
		if (!arrows.exists()) arrows.createNewFile();
		config = new Configuration(arrows);
		Map<String, Object> data = (Map<String, Object>)yaml.load(new FileReader(arrows));
		if (data == null)
		{
			logger.info("Generating ArrowLimiter config file.");
			data = new HashMap<String, Object>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("timedelay", 1.00);
			map.put("damage", 1);
			data.put("arrows", map);
			FileWriter tx = new FileWriter(arrows);
			tx.write(yaml.dump(data));
			tx.flush();
			tx.close();
		}
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
