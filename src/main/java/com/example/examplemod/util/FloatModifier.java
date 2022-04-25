package com.example.examplemod.util;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;

import java.util.LinkedList;
import java.util.List;

public final class FloatModifier {

    private final float baseValue;
    private final List<Float2FloatFunction> calcBefore;
    private final List<Float2FloatFunction> calcAfter;
    private float delta;

    public FloatModifier(float baseValue) {
        this(baseValue, 0, new LinkedList<>(), new LinkedList<>());
    }

    FloatModifier(float baseValue, float delta, List<Float2FloatFunction> calcBefore, List<Float2FloatFunction> calcAfter) {
        this.baseValue = baseValue;
        this.delta = delta;
        this.calcBefore = calcBefore;
        this.calcAfter = calcAfter;
    }

    public float getBaseValue() {
        return baseValue;
    }

    public float getResult() {
        float result = calc(baseValue, calcBefore);
        result += delta;
        return calc(baseValue, calcAfter);
    }

    private float calc(float baseValue, List<Float2FloatFunction> funcs) {
        if (funcs.isEmpty()) {
            return baseValue;
        }
        for (Float2FloatFunction func : funcs) {
            baseValue = func.apply(baseValue);
        }
        return baseValue;
    }

    public FloatModifier add(float value) {
        delta += value;
        return this;
    }

    public FloatModifier sub(float value) {
        delta -= value;
        return this;
    }

    public FloatModifier mulDelta(float value) {
        delta *= value;
        return this;
    }

    public FloatModifier divDelta(float value) {
        delta /= delta;
        return this;
    }

    public FloatModifier applyDelta(Float2FloatFunction func) {
        delta = func.apply(delta);
        return this;
    }

    public FloatModifier calcBefore(Float2FloatFunction func) {
        calcBefore.add(func);
        return this;
    }

    public FloatModifier calcAfter(Float2FloatFunction func) {
        calcAfter.add(func);
        return this;
    }

    public FloatModifier copy(float baseValue) {
        return new FloatModifier(baseValue, delta, new LinkedList<>(calcBefore), new LinkedList<>(calcAfter));
    }
}
