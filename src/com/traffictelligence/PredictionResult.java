package com.traffictelligence;

public class PredictionResult {

    private double predictedTrafficVolume;

    public PredictionResult(double predictedTrafficVolume) {
        this.predictedTrafficVolume = predictedTrafficVolume;
    }

    public double getPredictedTrafficVolume() {
        return predictedTrafficVolume;
    }

    @Override
    public String toString() {
        return "Estimated Traffic Volume: "
                + Math.round(predictedTrafficVolume)
                + " vehicles";
    }
}
