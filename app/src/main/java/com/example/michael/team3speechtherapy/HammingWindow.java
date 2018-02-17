package com.example.michael.team3speechtherapy;

/*
 * (C) Copyright 2014 Amaury Crickx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.util.HashMap;
import java.util.Map;

public class HammingWindow {

    protected static final double TWO_PI = 2 * Math.PI;

    private final int windowSize;
    private final double[] factors;

    private static final Map<Integer, double[]> factorsByWindowSize = new HashMap<Integer, double[]>();

    public HammingWindow(int windowSize) {
        this.windowSize = windowSize;
        this.factors = getPrecomputedFactors(windowSize);
    }

    public double[] applyFunction(double[] window) {
        if (window.length == this.windowSize) {
            for (int i = 0; i < window.length; i++) {
                window[i] *= factors[i];
            }
        } else {
            throw new IllegalArgumentException("Incompatible window size for this WindowFunction instance : " +
                    "expected " + windowSize + ", received " + window.length);
        }
        return window;
    }

    protected double[] getPrecomputedFactors(int windowSize) {
        // precompute factors for given window, avoid re-calculating for several instances
        synchronized (HammingWindow.class) {
            double[] factors;
            if(factorsByWindowSize.containsKey(windowSize)) {
                factors = factorsByWindowSize.get(windowSize);
            } else {
                factors = new double[windowSize];
                int sizeMinusOne = windowSize - 1;
                for(int i = 0; i < windowSize; i++) {
                    factors[i] = 0.54d - (0.46d * Math.cos((TWO_PI * i) / sizeMinusOne));
                }
                factorsByWindowSize.put(windowSize, factors);
            }
            return factors;
        }
    }

}