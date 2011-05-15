package com.ementalo.arrowlimiter;

import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;



public class ArrowLimiterEntityListener extends EntityListener
{
	
	private ArrowLimiter arrLim = new ArrowLimiter();
	private int arrowDamage;

	public ArrowLimiterEntityListener(ArrowLimiter parent)
	{
		this.arrLim = parent;
		
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event)
	{
		arrowDamage = arrLim.config.getInt("damage", 1);
		if(event instanceof EntityDamageByProjectileEvent)
		{
			EntityDamageByProjectileEvent edEvent = (EntityDamageByProjectileEvent)event;
			if(edEvent.isCancelled()) return;
			edEvent.setDamage(arrowDamage);
			return;
		}
	}

}
