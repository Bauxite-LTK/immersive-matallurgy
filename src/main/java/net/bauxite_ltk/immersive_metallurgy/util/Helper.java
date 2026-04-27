package net.bauxite_ltk.immersive_metallurgy.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.text.DecimalFormat;

public class Helper {
    static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");

    public static String fDecimal(byte number){
        return FORMATTER.format(number);
    }

    public static String fDecimal(short number){
        return FORMATTER.format(number);
    }

    public static String fDecimal(int number){
        return FORMATTER.format(number);
    }

    public static String fDecimal(long number){
        return FORMATTER.format(number);
    }

    public static String fDecimal(float number){
        return FORMATTER.format(number);
    }

    public static String fDecimal(double number){
        return FORMATTER.format(number);
    }



    public static void applyRotationX(double pivotY, double pivotZ, double degree, PoseStack poseStack){
        double arcDegree = -degree/180 * Math.PI;
        //transY, transZ are the Y and Z position of the pivot after *directly* applying the rotation.
        double transY = Math.cos(arcDegree)*pivotY + Math.sin(arcDegree)*pivotZ;
        double transZ = -Math.sin(arcDegree)*pivotY + Math.cos(arcDegree)*pivotZ;
        //System.out.println("(" + transY + "," + transZ + ")");
        poseStack.translate(0, (pivotY-transY)/16, (pivotZ-transZ)/16);

        poseStack.mulPose(Axis.XP.rotationDegrees((float) degree));
    }

    public static void applyRotationY( double pivotX, double pivotZ, double degree, PoseStack poseStack){
        double arcDegree = -degree/180 * Math.PI;
        //transY, transZ are the Y and Z position of the pivot after *directly* applying the rotation.
        double transZ = Math.cos(arcDegree)*pivotZ + Math.sin(arcDegree)*pivotX;
        double transX = -Math.sin(arcDegree)*pivotZ + Math.cos(arcDegree)*pivotX;
        //System.out.println("(" + transY + "," + transZ + ")");
        poseStack.translate((pivotX-transX)/16,0, (pivotZ-transZ)/16);

        poseStack.mulPose(Axis.YP.rotationDegrees((float) degree));
    }

    public static void applyRotationZ(double pivotX, double pivotY, double degree, PoseStack poseStack){
        double arcDegree = -degree/180 * Math.PI;
        //transY, transZ are the Y and Z position of the pivot after *directly* applying the rotation.
        double transX = Math.cos(arcDegree)*pivotX + Math.sin(arcDegree)*pivotY;
        double transY = -Math.sin(arcDegree)*pivotX + Math.cos(arcDegree)*pivotY;
        //System.out.println("(" + transY + "," + transZ + ")");
        poseStack.translate((pivotX-transX)/16, (pivotY-transY)/16, 0);

        poseStack.mulPose(Axis.ZP.rotationDegrees((float) degree));
    }

    public static float calculateSpeedWithFactors(int x, int min, int max, int best, float strict){
        float s = strict;
        if(s > 1) s = 1f;
        else if(s < -1) s = -1f;

        float matchFactor = x > best? (float)(max-x)/(max-best) : (float)(x-min)/(best-min);
        if(x == best) matchFactor = 1f;
        if(s == 0) return matchFactor;

        float var0 = (matchFactor + 1/(2*s) - 0.5f);
        float var1 = 1/(2*s*s) + 0.5f - var0 * var0;
        float var2 = s > 0 ? -Mth.sqrt(var1) : Mth.sqrt(var1);
        float var3 = var2 + 1/(2*s) + 0.5f;

        return var3;
    }

    public static float calculateSpeedWithFactors(int x, int min, int max, int best, float strict, float tolerance){
        float s = strict;
        if(s > 1) s = 1f;
        else if(s < -1) s = -1f;

        float matchFactor = x > best? (float)(max-x)/(max-best) : (float)(x-min)/(best-min);
        if(x == best) matchFactor = 1f;
        if(s == 0) return matchFactor;

        float var0 = (matchFactor + 1/(2*s) - 0.5f);
        float var1 = 1/(2*s*s) + 0.5f - var0 * var0;
        float var2 = s > 0 ? -Mth.sqrt(var1) : Mth.sqrt(var1);
        float var3 = var2 + 1/(2*s) + 0.5f;

        return Mth.clamp(var3*tolerance,0,1);
    }


}
