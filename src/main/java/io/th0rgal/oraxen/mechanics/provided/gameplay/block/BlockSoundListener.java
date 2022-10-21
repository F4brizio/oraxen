package io.th0rgal.oraxen.mechanics.provided.gameplay.block;

import io.th0rgal.oraxen.utils.BlockHelpers;
import io.th0rgal.oraxen.utils.blocksounds.BlockSounds;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.GenericGameEvent;

import static io.th0rgal.oraxen.utils.BlockHelpers.*;

public class BlockSoundListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onStepFall(final GenericGameEvent event) {
        Entity entity = event.getEntity();
        if (entity == null) return;
        Location eLoc = entity.getLocation();
        if (!isLoaded(event.getLocation()) || !isLoaded(eLoc)) return;

        GameEvent gameEvent = event.getEvent();
        Block currentBlock = entity.getLocation().getBlock();
        Block blockBelow = currentBlock.getRelative(BlockFace.DOWN);
        String sound;

        if (!BlockHelpers.REPLACEABLE_BLOCKS.contains(currentBlock.getType()) || currentBlock.getType() == Material.TRIPWIRE) return;
        if (blockBelow.getType() != Material.MUSHROOM_STEM) return;
        final BlockMechanic mechanic = BlockMechanicListener.getBlockMechanic(blockBelow);
        if (mechanic == null) return;
        BlockSounds blockSounds = mechanic.getBlockSounds();
        if (blockSounds == null) return;
        if (gameEvent == GameEvent.STEP) sound = blockSounds.hasStepSound() ? blockSounds.getStepSound() : VANILLA_WOOD_STEP;
        else if (gameEvent == GameEvent.HIT_GROUND) sound = blockSounds.hasFallSound() ? blockSounds.getFallSound() : VANILLA_WOOD_FALL;
        else return;

        BlockHelpers.playCustomBlockSound(entity.getLocation(), sound, SoundCategory.PLAYERS, blockSounds.getVolume(), blockSounds.getPitch());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlacing(final BlockBreakEvent event) {
        BlockMechanic mechanic = BlockMechanicListener.getBlockMechanic(event.getBlock());
        if (mechanic == null || !mechanic.hasBlockSounds()) return;
        BlockSounds blockSounds = mechanic.getBlockSounds();
        if (blockSounds.hasPlaceSound())
            BlockHelpers.playCustomBlockSound(event.getBlock().getLocation(), blockSounds.getPlaceSound(), blockSounds.getVolume(), blockSounds.getPitch());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreaking(final BlockPlaceEvent event) {
        BlockMechanic mechanic = BlockMechanicListener.getBlockMechanic(event.getBlock());
        if (mechanic == null || !mechanic.hasBlockSounds()) return;
        BlockSounds blockSounds = mechanic.getBlockSounds();
            BlockHelpers.playCustomBlockSound(event.getBlock().getLocation(), blockSounds.getBreakSound(), blockSounds.getVolume(), blockSounds.getPitch());
    }
}
