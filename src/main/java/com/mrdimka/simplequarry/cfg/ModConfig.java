package com.mrdimka.simplequarry.cfg;

import java.io.File;

import com.mrdimka.hammercore.api.HammerCoreAPI;
import com.mrdimka.hammercore.cfg.HCModConfigurations;
import com.mrdimka.hammercore.cfg.IConfigReloadListener;
import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyInt;
import com.mrdimka.simplequarry.ref.ModInfo;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HCModConfigurations(modid = ModInfo.MOD_ID)
public class ModConfig implements IConfigReloadListener
{
	@ModConfigPropertyInt(name = "Powered Quarry Recipe", category = "Gameplay", defaultValue = 1, min = -1, max = 1, comment = "What recipe sould be used for Powered Quarry?\n-1 - disable quarry at all (ignore presence of RF API)\n0 - 4 eyes of ender & normal chest\n1 - podzol, sea lantern, slime block, magma block & ender chest")
	public static int POWERED_QUARRY_RECIPE = 0;
}