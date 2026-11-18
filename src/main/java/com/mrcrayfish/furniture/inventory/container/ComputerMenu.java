package com.mrcrayfish.furniture.inventory.container;

import com.mrcrayfish.furniture.core.ModContainers;
import com.mrcrayfish.furniture.core.ModRecipeTypes;
import com.mrcrayfish.furniture.item.crafting.MineBayRecipe;
import com.mrcrayfish.furniture.tileentity.ComputerBlockEntity;
import com.mrcrayfish.furniture.util.RecipeLookup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * MineBay on a computer (1.12.2): flick through what is for sale and buy it with the money in the computer's slot. The arrows and Buy
 * are menu buttons; the server checks the money and hands the item over.
 *
 * Author: MrCrayfish
 */
public class ComputerMenu extends ApplianceMenu<ComputerBlockEntity>
{
    public static final int BUTTON_PREVIOUS = 0;
    public static final int BUTTON_NEXT = 1;
    public static final int BUTTON_BUY = 2;

    private final Level level;

    public ComputerMenu(int windowId, Inventory playerInventory, ComputerBlockEntity blockEntity)
    {
        super(ModContainers.COMPUTER.get(), windowId, playerInventory, blockEntity, new SimpleContainerData(1));
        this.level = playerInventory.player.level();
        this.addSlot(new Slot(blockEntity, 0, 119, 40));
        this.addPlayerSlots(playerInventory, 95);
    }

    public List<RecipeHolder<MineBayRecipe>> getOffers()
    {
        return RecipeLookup.all(this.level, ModRecipeTypes.MINEBAY.get());
    }

    /**
     * The offer being looked at.
     */
    public int getSelected()
    {
        return this.data.get(0);
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        List<RecipeHolder<MineBayRecipe>> offers = this.getOffers();
        if(offers.isEmpty())
            return false;

        int selected = Math.min(this.getSelected(), offers.size() - 1);
        switch(id)
        {
            case BUTTON_PREVIOUS -> this.data.set(0, Math.max(0, selected - 1));
            case BUTTON_NEXT -> this.data.set(0, Math.min(offers.size() - 1, selected + 1));
            case BUTTON_BUY -> this.buy(player, offers.get(selected).value());
            default -> {
                return false;
            }
        }
        return true;
    }

    private void buy(Player player, MineBayRecipe offer)
    {
        ItemStack money = this.blockEntity.getItem(0);
        if(!offer.matches(new SingleRecipeInput(money), this.level))
            return;

        money.shrink(offer.price().count());
        this.blockEntity.setChanged();
        player.getInventory().placeItemBackInInventory(offer.result().create(), Prediction.SERVER_ONLY);
        this.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
    }
}
