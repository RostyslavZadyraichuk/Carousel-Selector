package com.zadyraichuk.selector.service;

import com.zadyraichuk.selector.domain.Variant;
import com.zadyraichuk.selector.domain.VariantsCollection;

import java.io.Serializable;
import java.util.*;

//todo add documentation
public abstract class AbstractRandomSelector<E, V extends Variant<E>>
        implements Selector<Variant<E>>, Serializable {

    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    protected VariantsCollection<E, V> variantsList;

    private String name;

    private int currentRotation;

    public AbstractRandomSelector(String name, VariantsCollection<E, V> collection) {
        this.name = name;
        variantsList = collection;
        currentRotation = 90;
    }

    public abstract void setVariants(List<?> values);

    public abstract void setVariants(Object... values);

    public VariantsCollection<E, V> getVariantsList() {
        return variantsList;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation % 360;
    }

    public void setVariantsList(VariantsCollection<E, V> variantsList) {
        this.variantsList = variantsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected int nextRandomIndex() {
        double percent = AbstractRandomSelector.RANDOM.nextDouble();
        return getVariantIndexByPercent(percent);
    }

    protected int nextIndexByDegree(int degree) {
        double percent = (double) 360 / degree;
        return getVariantIndexByPercent(percent);
    }

    private int getVariantIndexByPercent(double percent) {
        double[] probabilities = variantsList.probabilities();

        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (percent <= sum)
                return i;
        }

        return probabilities.length - 1;
    }
}
