package com.example.michael.team3speechtherapy;

import java.util.Arrays;

/**
 * Created by michael on 2/16/18.
 */

public class LinearPredictiveCoding {

    private final int windowSize;
    private final int poles;
    private final double[] output;
    private final double[] error;
    private final double[] k;
    private final double[][] matrix;

    public LinearPredictiveCoding(int ws, int p) {
        windowSize = ws;
        poles = p;
        output = new double[poles];
        error = new double[poles];
        k = new double[poles];
        matrix = new double[poles][poles];
    }

    public double[][] applyLinearPredictiveCoding(double[] window) {

        Arrays.fill(k, 0.0d);
        Arrays.fill(output, 0.0d);
        Arrays.fill(error, 0.0d);
        for (double[] d : matrix) {
            Arrays.fill(d, 0.0d);
        }

        double[] autocorrelations = new double[poles];
        for (int i = 0; i < poles; i++) {
            autocorrelations[i] = autocorrelate(window, i);
        }
        error[0] = autocorrelations[0];

        for (int m = 1; m < poles; m++) {
            double tmp = autocorrelations[m];
            for (int i = 1; i < m; i++) {
                tmp -= matrix[m - 1][i] * autocorrelations[m - i];
            }
            k[m] = tmp / error[m - 1];

            for (int i = 0; i < m; i++) {
                matrix[m][i] = matrix[m - 1][i] - k[m] * matrix[m - 1][m - i];
            }
            matrix[m][m] = k[m];
            error[m] = (1 - (k[m] * k[m])) * error[m - 1];
        }

        for (int i = 0; i < poles; i++) {
            if (Double.isNaN(matrix[poles - 1][i])) {
                output[i] = 0.0;
            } else {
                output[i] = matrix[poles - 1][i];
            }
        }

        return new double[][]{output, error};
    }

    public double autocorrelate(double[] buffer, int lag) {
        if(lag > -1 && lag < buffer.length) {
            double result = 0.0;
            for (int i = lag; i < buffer.length; i++) {
                result += buffer[i] * buffer[i - lag];
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("Lag parameter range is : -1 < lag < buffer size. Received ["
                    + lag + "] for buffer size of [" + buffer.length + "]");
        }
    }
}
