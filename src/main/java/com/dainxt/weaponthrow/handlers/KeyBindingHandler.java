package com.dainxt.weaponthrow.handlers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class KeyBindingHandler {
	public static final KeyBinding KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.weaponthrow", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.GAMEPLAY_CATEGORY));
	
	public static void registerKeyBindings() {


	}
}
