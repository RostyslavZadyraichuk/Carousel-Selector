package com.zadyraichuk.selector.domain;

import com.zadyraichuk.general.MathUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractVariantsList<E, V extends Variant<E>>
        implements VariantsCollection<E, V>, Serializable {

    protected final List<V> variants;

    /**
     * Collection stage for lazy normalization during obtain elements operation
     */
    protected boolean isChanged;

    private static final long serialVersionUID = -2543453820153602704L;

    private VariantColorPalette palette;

    public AbstractVariantsList() {
        variants = new ArrayList<>();
        palette = VariantColorPalette.generateOrderedPalette();
        isChanged = true;
    }

    public AbstractVariantsList(List<V> variants) {
        this.variants = variants;
        palette = VariantColorPalette.generateOrderedPalette();
        setUpColors();
        isChanged = true;
    }

    @Override
    public E getValue(int index) {
        initVariantPercents();
        return variants.get(index).getValue();
    }

    @Override
    public V get(int index) {
        initVariantPercents();
        return variants.get(index);
    }

    @Override
    public void add(V variant) {
        variants.add(variant);
        variant.setColor(palette.nextColor());
        isChanged = true;
    }

    @Override
    public void swap(int firstIndex, int secondIndex) {
        V first = variants.get(firstIndex);
        V second = variants.get(secondIndex);
        VariantColor temp = first.getColor();
        first.setColor(second.getColor());
        second.setColor(temp);
        variants.set(firstIndex, second);
        variants.set(secondIndex, first);
    }

    @Override
    public void remove(int index) {
        V removed = variants.remove(index);
        updateColors(removed, index);
        isChanged = true;
    }

    @Override
    public void remove(V variant) {
        int variantIndex = variants.indexOf(variant);
        V removed = variants.remove(variantIndex);
        updateColors(removed, variantIndex);
        isChanged = true;
    }

    @Override
    public boolean contains(V variant) {
        return variants.contains(variant);
    }

    @Override
    public int indexOf(V variant) {
        return variants.indexOf(variant);
    }

    @Override
    public boolean isEmpty() {
        return variants.isEmpty();
    }

    @Override
    public int size() {
        return variants.size();
    }

    @Override
    public Stream<V> stream() {
        initVariantPercents();
        return variants.stream();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(variants);
        setUpColors();
    }

    @Override
    public double[] probabilities() {
        initVariantPercents();
        return variants.stream()
                .mapToDouble(Variant::getCurrentPercent)
                .toArray();
    }

    @Override
    public Iterator<V> iterator() {
        initVariantPercents();
        return variants.iterator();
    }

    @Override
    public double leftProbabilityBound(V variant) {
        double percentSum = 0;

        for (V v : variants) {
            if (v.equals(variant))
                break;

            percentSum += v.getCurrentPercent();
        }

        return percentSum;
    }

    @Override
    public double rightProbabilityBound(V variant) {
        int variantIndex = variants.indexOf(variant);
        if (variantIndex == -1)
            return 0;

        variant = variants.get(variantIndex);
        return leftProbabilityBound(variant) + variant.getCurrentPercent();
    }

    @Override
    public void initVariantPercents() {
        if (isChanged) {
            isChanged = false;

            int totalWeight = VariantsCollection.totalWeight(this);
            if (totalWeight != 0) {
                double singleWeightPercent = VariantsCollection.singleWeightPercent(this, 1.0);
                singleWeightPercent = MathUtils.cutRound(singleWeightPercent, Variant.DIGITS);

                for (Variant<E> variant : variants) {
                    double normalized = variant.getVariantWeight() * singleWeightPercent;
                    variant.setCurrentPercent(normalized);
                }
            }
        }
    }

    public VariantColorPalette getPalette() {
        return palette;
    }

    public void setPalette(VariantColorPalette palette) {
        this.palette = palette;
    }

    public void generateNewPalette(int colorsCount) {
        palette = VariantColorPalette.generateOrderedPalette(colorsCount);
        setUpColors();
    }

    private void setUpColors() {
        palette.resetColorIndex();

        for (V variant : variants) {
            variant.setColor(palette.nextColor());
        }
    }

    private void updateColors(V removed, int updateStartIndex) {
        int nextColorIndex = palette.indexInPalette(removed.getColor());
        palette.setColorIndex(nextColorIndex);

        for (int i = updateStartIndex; i < variants.size(); i++) {
            variants.get(i).setColor(palette.nextColor());
        }
    }

}
