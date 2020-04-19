package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.EnumSet;


public class Building {

    protected enum BuildingProperty {
        CAN_BUILD_ON_IT,
        IS_SCALABLE;
    }

    public enum BuildingLevel {
        LEVEL1(1, BuildingProperty.CAN_BUILD_ON_IT, BuildingProperty.IS_SCALABLE),
        LEVEL2(2, BuildingProperty.CAN_BUILD_ON_IT, BuildingProperty.IS_SCALABLE),
        LEVEL3(3, BuildingProperty.CAN_BUILD_ON_IT, BuildingProperty.IS_SCALABLE),
        DOME(4);

        private final int levelValue;
        private final EnumSet<BuildingProperty> buildingsProperties;
        private static BuildingLevel[] buildingLevelValues = values();

        /**
         * Constructor of the enum BuildingsLevel
         *
         * @param levelValue Height level related to the building
         * @param buildingsProperties Properties assignable to buildings
         */
        BuildingLevel(int levelValue, BuildingProperty... buildingsProperties){
            this.levelValue = levelValue;
            this.buildingsProperties = buildingsProperties.length == 0 ? EnumSet.noneOf(BuildingProperty.class) : EnumSet.copyOf(Arrays.asList(buildingsProperties));
        }

        /**
         * Check if the building owns the property
         *
         * @param property Property assignable to buildings
         * @return True if the building owns the property, False otherwise
         */
        public boolean hasProperty(BuildingProperty property){
            return buildingsProperties.contains(property);
        }

        public int getLevelAsInt(){
            return levelValue;
        }

        //TODO: Test
        public BuildingLevel getNextLevel(){
            return this.ordinal() < buildingLevelValues.length ? buildingLevelValues[(this.ordinal() + 1)] : null;
        }
    }

    private final BuildingLevel level;

    /**
     * Creates a new Building with the related height level
     *
     * @param level Height level related to the building
     */
    public Building(BuildingLevel level){
        this.level = level;
    }

    public BuildingLevel getLevel(){
        return level;
    }

    public int getLevelAsInt() {
        return level.levelValue;
    }
}