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
public class Points {

    ArrayList<X_and_Y> points;

    int number_of_points, degree;

    public ArrayList<X_and_Y> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<X_and_Y> points) {
        this.points = points;
    }

    public Points() {
        points = new ArrayList();
        number_of_points = degree = 0;
    }

    public int getNumber_of_points() {
        return number_of_points;
    }

    public void setNumber_of_points(int number_of_points) {
        this.number_of_points = number_of_points;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
    
}
