/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curve_fitting.problem;

import java.util.ArrayList;

/**
 *
 * @author Beido
 */
public class Chromosome {
     ArrayList<Double> chromosome;
     float error;
     float min , max;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getError() {
        return error;
    }

    public void setError(float error) {
        this.error = error;
    }
    
     public Chromosome() {
    }
    
     public ArrayList<Double> getChromosome() {
        return chromosome;
    }

    public void setChromosome(ArrayList<Double> chromosome) {
        this.chromosome = chromosome;
    }

    

     
    
}
