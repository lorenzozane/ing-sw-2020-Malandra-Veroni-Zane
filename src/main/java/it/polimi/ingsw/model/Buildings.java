package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.EnumSet;


// Implementazione della classe Buildings con assegnamento dei valori dei livelli e delle proprietà tramite enum
// Essendo pochi e ben definiti i livelli e le proprietà che possono avere, inserirli tramite enum aiuta ad avere
// un codice più chiaro e "sicuro"
public class Buildings {

    public enum BuildingsProperty{
        CAN_BUILD_ON_IT,
        IS_SCALABLE;
    }

    public enum BuildingsLevel {
        LEVEL1(1, BuildingsProperty.CAN_BUILD_ON_IT, BuildingsProperty.IS_SCALABLE),
        LEVEL2(2, BuildingsProperty.CAN_BUILD_ON_IT, BuildingsProperty.IS_SCALABLE),
        LEVEL3(3, BuildingsProperty.CAN_BUILD_ON_IT, BuildingsProperty.IS_SCALABLE),
        DOME(4);

        private int levelValue;
        private final EnumSet<BuildingsProperty> buildingsProperties;

        private BuildingsLevel(int levelValue, BuildingsProperty ... buildingsProperties){
            this.levelValue = levelValue;
            this.buildingsProperties = buildingsProperties.length == 0 ? EnumSet.noneOf(BuildingsProperty.class) : EnumSet.copyOf(Arrays.asList(buildingsProperties));
        }

        public boolean hasProperty(BuildingsProperty property){
            return buildingsProperties.contains(property);
        }

        public int GetBuildingsLevel(){
            return levelValue;
        }
    }

    BuildingsLevel level;

    public Buildings(BuildingsLevel level){
        this.level = level;
    }
//    private boolean isScalable;
//    private int blockType;
}