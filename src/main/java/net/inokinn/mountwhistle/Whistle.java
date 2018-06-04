package net.inokinn.mountwhistle;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Whistle extends Item {

	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Entity mount = playerIn.getRidingEntity();
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		// 騎乗しているか
		if (mount != null) {
			// 現在騎乗しているマウントを登録
			NBTTagCompound nbtTag = itemStack.getTagCompound();
			if (nbtTag == null) {
				nbtTag = new NBTTagCompound();
			}

			if (nbtTag.hasKey("MountId") &&
					!UUID.fromString(nbtTag.getString("MountId")).equals(mount.getUniqueID())) {
				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
			}

			String name = mount.getName();

			if (worldIn.isRemote) {
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("マウントホイッスルに" + name + "を登録しました！"));
			} else {
				nbtTag.setString("MountId", mount.getUniqueID().toString());
				nbtTag.setString("Name", name);
				itemStack.setStackDisplayName(name);
				itemStack.setTagCompound(nbtTag);

				// マウントを不死身にする
				mount.setEntityInvulnerable(true);
			}
		} else {
			NBTTagCompound nbtTag = itemStack.getTagCompound();
			if (nbtTag == null || !nbtTag.hasKey("MountId")) {
				if (worldIn.isRemote) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("騎乗中に使用するとマウントを登録できます。"));
				}
				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
			}

			UUID mountId = UUID.fromString(nbtTag.getString("MountId"));

			MinecraftServer server =  worldIn.getMinecraftServer();

			if(server == null) {
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("召喚を試みました。"));
			} else {
				// シングルプレイの場合、すべてのエンティティからマウントを探す
				mount = server.getEntityFromUuid(mountId);
				mount.setLocationAndAngles(playerIn.posX, playerIn.posY, playerIn.posZ, 0.0F, 0.0F);

				if (worldIn.isRemote) {
					if (mount != null) {
						String name = mount.getName();
						Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(name + " を召喚しました。"));
					}
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
}
