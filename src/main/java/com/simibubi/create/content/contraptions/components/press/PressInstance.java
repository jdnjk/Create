package com.simibubi.create.content.contraptions.components.press;

import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.foundation.render.backend.instancing.IDynamicInstance;
import com.simibubi.create.foundation.render.backend.instancing.InstanceKey;
import com.simibubi.create.foundation.render.backend.instancing.InstancedTileRenderer;
import com.simibubi.create.foundation.render.backend.instancing.impl.ModelData;
import com.simibubi.create.foundation.render.backend.instancing.impl.OrientedData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.MatrixStacker;

public class PressInstance extends ShaftInstance implements IDynamicInstance {

    private final InstanceKey<OrientedData> pressHead;
    private final MechanicalPressTileEntity press;

    public PressInstance(InstancedTileRenderer<?> dispatcher, MechanicalPressTileEntity tile) {
        super(dispatcher, tile);
        press = tile;

        pressHead = dispatcher.getOrientedMaterial()
                .getModel(AllBlockPartials.MECHANICAL_PRESS_HEAD, blockState)
                .createInstance();

        Quaternion q = Vector3f.POSITIVE_Y.getDegreesQuaternion(AngleHelper.horizontalAngle(blockState.get(MechanicalPressBlock.HORIZONTAL_FACING)));

        pressHead.getInstance().setRotation(q);

        transformModels();
    }

    @Override
    public void beginFrame() {
        if (!press.running)
            return;

        transformModels();
    }

    private void transformModels() {
        float renderedHeadOffset = getRenderedHeadOffset(press);

        pressHead.getInstance()
                .setPosition(getInstancePosition())
                .nudge(0, -renderedHeadOffset, 0);
    }

    private float getRenderedHeadOffset(MechanicalPressTileEntity press) {
        return press.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public void updateLight() {
        super.updateLight();

        relight(pos, pressHead.getInstance());
    }

    @Override
    public void remove() {
        super.remove();
        pressHead.delete();
    }
}
