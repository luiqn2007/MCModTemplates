package com.example.examplemod.util;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;

import java.util.List;

public final class FloatModifier {

    private final float baseValue;
    private float delta = 0;
    private List<Float2FloatFunction> calcBefore;
    private List<Float2FloatFunction> calcAfter;

    public FloatModifier(float baseValue) {
        this.baseValue = baseValue;
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
}
