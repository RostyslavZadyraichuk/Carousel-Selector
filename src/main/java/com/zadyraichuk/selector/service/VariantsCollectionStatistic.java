package com.zadyraichuk.selector.service;

import com.zadyraichuk.selector.domain.RationalVariant;
import com.zadyraichuk.selector.domain.RationalVariantsList;
import com.zadyraichuk.selector.domain.Variant;
import com.zadyraichuk.selector.domain.VariantsCollection;

public class VariantsCollectionStatistic {

    public static void showStatistic(VariantsCollection<?, ?> variants) {
        if (variants.isEmpty()) {
            System.out.println("Empty collection");
        } else {
            VariantsCollectionStatistic.showCollectionStatistic(variants);
            VariantsCollectionStatistic.showVariants(variants);
        }
    }

    public static void showStatistic(RationalVariantsList<?> variants) {
        if (variants.isEmpty()) {
            System.out.println("Empty collection");
        } else {
            VariantsCollectionStatistic.showCollectionStatistic(variants);
            VariantsCollectionStatistic.showVariants(variants);
        }
    }

    protected static void showCollectionStatistic(VariantsCollection<?, ?> variants) {
        String oneItem = "#";
        double oneItemPercent = VariantsCollection.getOneWeightPercent(variants);

        System.out.printf("Single weight: %s = %.3f%n",
                oneItem, oneItemPercent);
        System.out.printf("Total weights: %d%n", VariantsCollection.getTotalWeight(variants));
        System.out.printf("Total percent: %f%n", VariantsCollection.getTotalPercent(variants));
    }

    protected static void showVariants(VariantsCollection<?, ?> variants) {
        String oneItem = "#";
        double minimalPercent = VariantsCollection.getMinimalPercent(variants);

        System.out.printf("\t%-20s: Percent | %-20s | %-20s%n",
                "Element", "Init weight", "Weight");

        for (Variant<?> variant : variants) {
            String initialWeight = oneItem.repeat(variant.getVariantWeight());
            double percentWeight = variant.getPercentWeight(minimalPercent);
            StringBuilder currentWeight = new StringBuilder(oneItem.repeat((int) percentWeight));
            if (percentWeight % 1 >= 0.5)
                currentWeight.append('|');

            System.out.printf("\t%-20s: %6.3f%% | %-20s | %-20s%n",
                    variant.getValue(),
                    variant.getCurrentPercent(),
                    initialWeight,
                    currentWeight);
        }
    }

    protected static void showVariants(RationalVariantsList<?> variants) {
        String oneItem = "#";
        double minimalPercent = VariantsCollection.getMinimalPercent(variants);

        System.out.printf("\t%-20s: Min percent | Percent | %-20s | %-20s%n",
                "Element", "Init weight", "Weight");

        for (RationalVariant<?> variant : variants) {
            String initialWeight = oneItem.repeat(variant.getVariantWeight());
            double percentWeight = variant.getPercentWeight(minimalPercent);
            StringBuilder currentWeight = new StringBuilder(oneItem.repeat((int) percentWeight));
            if (percentWeight % 1 >= 0.5)
                currentWeight.append('|');

            System.out.printf("\t%-20s: %10.3f%% | %6.3f%% | %-20s | %-20s%n",
                    variant.getValue(),
                    variant.getMinPercent(),
                    variant.getCurrentPercent(),
                    initialWeight,
                    currentWeight);
        }
    }
    
}
