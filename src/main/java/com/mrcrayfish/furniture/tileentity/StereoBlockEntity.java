package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Comparator;
import java.util.List;

/**
 * Which song a stereo plays: it goes through every jukebox song in turn (sorted by name, so the order holds across restarts).
 *
 * Author: MrCrayfish
 */
public class StereoBlockEntity extends BlockEntity
{
    // Position in the sorted song list, or -1 when off
    private int song = -1;

    public StereoBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.STEREO.get(), pos, state);
    }

    private List<Holder.Reference<JukeboxSong>> songs()
    {
        if(this.level == null)
            return List.of();
        return this.level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).listElements()
                .sorted(Comparator.comparing(holder -> holder.key().identifier().toString())).toList();
    }

    public void playNext()
    {
        List<Holder.Reference<JukeboxSong>> songs = this.songs();
        if(this.level == null || songs.isEmpty())
            return;

        this.song = (this.song + 1) % songs.size();
        Registry<JukeboxSong> registry = this.level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG);
        this.level.levelEvent(null, LevelEvent.SOUND_PLAY_JUKEBOX_SONG, this.worldPosition, registry.getId(songs.get(this.song).value()));
        this.setChanged();
    }

    /**
     * @return whether it was playing
     */
    public boolean stop()
    {
        if(this.song < 0 || this.level == null)
            return false;

        this.song = -1;
        this.level.levelEvent(null, LevelEvent.SOUND_STOP_JUKEBOX_SONG, this.worldPosition, 0);
        this.setChanged();
        return true;
    }

    public int getSignal()
    {
        return this.song < 0 ? 0 : Math.min(15, this.song + 1);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        this.stop();
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.song = input.getIntOr("Song", -1);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("Song", this.song);
    }
}
