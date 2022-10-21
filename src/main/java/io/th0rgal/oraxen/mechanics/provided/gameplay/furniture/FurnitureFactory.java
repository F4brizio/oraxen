package io.th0rgal.oraxen.mechanics.provided.gameplay.furniture;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.mechanics.Mechanic;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.gameplay.furniture.evolution.EvolutionTask;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class FurnitureFactory extends MechanicFactory {

    public static FurnitureFactory instance;
    public final List<String> toolTypes;
    public final int evolutionCheckDelay;
    private boolean evolvingFurnitures;
    private static EvolutionTask evolutionTask;
    public final boolean customSounds;

    public FurnitureFactory(ConfigurationSection section) {
        super(section);
        toolTypes = section.getStringList("tool_types");
        evolutionCheckDelay = section.getInt("evolution_check_delay");
        evolvingFurnitures = false;
        instance = this;
        customSounds = section.getBoolean("custom_sounds", true);

        MechanicsManager.registerListeners(OraxenPlugin.get(), new FurnitureListener(this));
        if (customSounds) MechanicsManager.registerListeners(OraxenPlugin.get(), new FurnitureSoundListener());
    }

    @Override
    public Mechanic parse(ConfigurationSection itemMechanicConfiguration) {
        Mechanic mechanic = new FurnitureMechanic(this, itemMechanicConfiguration);
        addToImplemented(mechanic);
        return mechanic;
    }

    public static FurnitureFactory getInstance() {
        return instance;
    }

    public void registerEvolution() {
        if (evolvingFurnitures)
            return;
        if (evolutionTask != null)
            evolutionTask.cancel();
        evolutionTask = new EvolutionTask(this, evolutionCheckDelay);
        evolutionTask.runTaskTimer(OraxenPlugin.get(), 0, evolutionCheckDelay);
        evolvingFurnitures = true;
    }

}
