package net.bauxite_ltk.immersive_metallurgy.event;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualElementTable;
import blusunrize.lib.manual.ManualInstance;
import net.bauxite_ltk.immersive_metallurgy.crafting.GasFuelRecipe;
import net.bauxite_ltk.immersive_metallurgy.crafting.HotAirFurnaceRecipe;
import net.bauxite_ltk.immersive_metallurgy.fluid.IMFluids;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class IMManualHelper {

    public static final Map<String, Supplier<Component[][]>> DYNAMIC_TABLES = new HashMap<>();

    public static void addIMElements(ManualInstance manualInstance){

        DYNAMIC_TABLES.put("hot_air_furnace", () -> formatToTable_ComponentStringMap(
                HotAirFurnaceRecipe.getInputValuesSorted(
                        Minecraft.getInstance().level,
                        IMFluids.HOT_AIR.getSource(),
                        true
                ),fuelAmount -> String.format("%.2f", fuelAmount), "mB"
        ));

        DYNAMIC_TABLES.put("gas_fuel", () -> formatToTable_ComponentStringMap(
                GasFuelRecipe.getBurnTimeValuesSorted(
                        Minecraft.getInstance().level,
                        true
                ), burnTime -> String.format("%d", burnTime), "Fuel"
        ));


        manualInstance.registerSpecialElement(IMUtils.modRL("im_dynamic_table"),
                s -> new ManualElementTable(
                        ManualHelper.getManual(),
                        DYNAMIC_TABLES.get(GsonHelper.getAsString(s, "table")).get(),
                        false
                ));
    }



    static <T extends Comparable<T>> Component[][] formatToTable_ComponentStringMap(Map<Component, T> map, Function<T,String> formater, String valueUnitSuffix)
    {
        List<Map.Entry<Component, T>> sortedMapArray = new ArrayList<>(map.entrySet());
        sortedMapArray.sort(Map.Entry.comparingByValue());
        ArrayList<Component[]> list = new ArrayList<>();
        try
        {
            for(Map.Entry<Component, T> entry : sortedMapArray)
            {
                Component item = entry.getKey();
                if(item==null)
                    item = Component.empty();

                T comparableValue = entry.getValue();
                Component am = Component.literal(formater.apply(comparableValue) +" "+ valueUnitSuffix);
                list.add(new Component[]{item, am});
            }
        } catch(Exception ignored)
        {
        }
        return list.toArray(new Component[0][]);
    }
}
