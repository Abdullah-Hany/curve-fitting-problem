/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curve_fitting.problem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Beido
 */
public class Curve_fittingProblem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        int counter = 0, chromosome_number = 50;
        float crossover_prob = (float) 0.5;
        int test_case_number = 0, number_of_points = 0, degree = 0, number_of_iterations = 2000;

        Scanner scanner = new Scanner(new File("input.txt"));

        test_case_number = scanner.nextInt();
        System.out.println("test cases : " + test_case_number + "    ");

        ArrayList<Points> points = new ArrayList();

        //getting input from file
        for (int i = 0; i < test_case_number; i++) {

            Points new_point = new Points();

            number_of_points = scanner.nextInt();

            new_point.setNumber_of_points(number_of_points);
            degree = scanner.nextInt();

            new_point.setDegree(degree);

            ArrayList<X_and_Y> x_y = new ArrayList();

            float x, y;
            while (counter < number_of_points) {
                X_and_Y x_y_object = new X_and_Y();
                x = scanner.nextFloat();
                y = scanner.nextFloat();
                x_y_object.setX(x);
                x_y_object.setY(y);
                x_y.add(x_y_object);

                counter++;
            }
            new_point.setPoints(x_y);
            points.add(new_point);
            counter = 0;
        }

    
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        for (int i = 0; i < test_case_number; i++) {
            System.out.println("\n test case number " + (i + 1));
            System.out.println("**************************");
            //number_of_points = points.get(i).getNumber_of_points();
            degree = points.get(i).getDegree();
            ArrayList<Chromosome> chromosomes = new ArrayList<>();
            for (int j = 0; j < number_of_iterations; j++) {
                if (j == 0) {
                    chromosomes = random_intialization(degree, chromosome_number);  
                    
                } else {
                    chromosomes = calculate_error(chromosomes, points, i);
                    sort(chromosomes);
                    
                    chromosomes = roulette_wheel_selection(chromosomes, chromosome_number, degree);
                    
                }

                chromosomes = crossover(chromosomes, crossover_prob);
                chromosomes = mutation(chromosomes, (float) j, (float) number_of_iterations);

                chromosomes = calculate_error(chromosomes, points, i);

            }
            Chromosome best = least_error(chromosomes);

            writer.write("test case number : " + (i + 1));
            writer.newLine();
            writer.newLine();
            writer.write("chromosome: ");
            for (int z = 0; z < best.getChromosome().size(); z++) {
                writer.write(String.valueOf(best.getChromosome().get(z)));
                if (z != best.getChromosome().size() - 1) {
                    writer.write(" , ");
                }

            }
            writer.newLine();
            writer.newLine();
            writer.write("error : ");
            writer.write(String.valueOf(best.getError()));
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.newLine();

        }
        writer.close();

    }

    public static Chromosome least_error(ArrayList<Chromosome> chromosomes) {
        Chromosome c = chromosomes.get(0);
        double error = chromosomes.get(0).getError();
        for (int i = 1; i < chromosomes.size(); i++) {
            if (chromosomes.get(i).getError() < error) {
                error = chromosomes.get(i).getError();
                c = chromosomes.get(i);
            }
        }

        return c;
    }

    public static ArrayList<Chromosome> random_intialization(int degree, int chromosome_number) {
        ArrayList<Chromosome> chromosomes = new ArrayList<>();

        for (int i = 0; i < chromosome_number; i++) {
            ArrayList<Double> chromosome_generation = new ArrayList<>();
            Chromosome c1 = new Chromosome();

            for (int j = 0; j < degree + 1; j++) {
                double random = randFloat(-10, 10);
                chromosome_generation.add(random);

            }
            c1.setChromosome(chromosome_generation);
            chromosomes.add(c1);
        }

        return chromosomes;
    }

    public static ArrayList<Chromosome> roulette_wheel_selection(ArrayList<Chromosome> chromosomes, int number_of_chromosomes, int degree) {

        ArrayList<Chromosome> new_chromosomes = new ArrayList();
        float total_fitness = 0;
        for (int i = 0; i < chromosomes.size(); i++) {
            total_fitness += chromosomes.get(i).getError();
        }

        // set boundries for the selection process
        for (int i = 0; i < chromosomes.size(); i++) {

            if (i != 0) {

                float min = 1 - (chromosomes.get(i - 1).getError() / total_fitness);
                float max = min + (1 - (chromosomes.get(i).getError() / total_fitness));

                chromosomes.get(i).setMin(min);
                chromosomes.get(i).setMax(max);
            }

            if (i == 0) {
                chromosomes.get(i).setMin(0);
                chromosomes.get(i).setMax(chromosomes.get(i).getError() / total_fitness);
            }

        }

  
        for (int i = 0; i < chromosomes.size(); i++) {
            float rand = (float) Math.random();

            for (int j = 0; j < chromosomes.size(); j++) {

                if (rand <= chromosomes.get(j).getMax() && rand > chromosomes.get(j).getMin()) {

                    new_chromosomes.add(chromosomes.get(j));

                }
            }

        }

        ArrayList<Chromosome> new_chromosomes2 = new ArrayList();
        if (new_chromosomes.size() < number_of_chromosomes) {
            //new_chromosomes2 = random_intialization(degree, number_of_chromosomes - new_chromosomes.size());
            new_chromosomes2 = sort(chromosomes);
            int counter = 0 ;
            while (new_chromosomes.size() <number_of_chromosomes)
            {
                if (counter >= new_chromosomes2.size())
                    counter = 0;
                new_chromosomes.add(new_chromosomes2.get(counter));
                counter++;
            }
           

        }

        return new_chromosomes;
    }

    public static void printing_chromosomes(ArrayList<Chromosome> chromosomes) {
        for (int i = 0; i < chromosomes.size(); i++) {

            System.out.print(chromosomes.get(i).getChromosome());
            System.out.print(chromosomes.get(i).getError());

            System.out.println();

        }
    }

//function that returns a random number between 2 limits [-10,10]
    public static double randFloat(float min, float max) {

        Random rand = new Random();
        double result = rand.nextFloat() * (max - min) + min;
        result = round(result, 2);
        return result;

    }
//function that rounds up a double
    public static double round(double number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
        //return bd.floatValue();
    }

    public static ArrayList<Chromosome> crossover(ArrayList<Chromosome> chromosomes, float crossover_prob) {

        int number_of_genes;
        try {
            ArrayList<Chromosome> to_crossover = new ArrayList();
            number_of_genes = chromosomes.get(0).getChromosome().size();

            for (int i = 0; i < chromosomes.size(); i++) {

                float rand = (float) Math.random();

                if (rand >= crossover_prob) {
                    to_crossover.add(chromosomes.get(i));
                }
                if (to_crossover.size() == 2) {
                    //call function crossover2
                    ArrayList<Double> child = new ArrayList();

                    Chromosome c1 = new Chromosome();

                    child = crossover2(to_crossover, number_of_genes);

                    c1.setChromosome(child);
                  

                    chromosomes.remove(to_crossover.get(0));
                    chromosomes.remove(to_crossover.get(1));
                    chromosomes.add(c1);
                    to_crossover = new ArrayList();

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return chromosomes;
    }

    public static ArrayList<Double> crossover2(ArrayList<Chromosome> chromosomes, int number_of_genes) {
        ArrayList<Double> child = new ArrayList();

        Random r = new Random();
        int Low = 2;
        int High = number_of_genes;
        int crosspoint = r.nextInt(High - Low) + Low;
       

        for (int i = 0; i < number_of_genes; i++) {
            if (i < crosspoint) {
                child.add(chromosomes.get(0).getChromosome().get(i));
            } else {
                child.add(chromosomes.get(1).getChromosome().get(i));
            }
        }

        return child;
    }

    public static ArrayList<Chromosome> calculate_error(ArrayList<Chromosome> chromosomes, ArrayList<Points> points, int testcase_number) {
        double mean_squared_error = 0, error;
        for (int i = 0; i < chromosomes.size(); i++) {
            int number_of_genes = 0, number_of_points;

            double y_actual = 0, X, y_pred = 0;

            number_of_points = points.get(testcase_number).getNumber_of_points();

            number_of_genes = chromosomes.get(i).getChromosome().size();

            for (int n = 0; n < number_of_points; n++) {
                y_actual = points.get(testcase_number).getPoints().get(n).getY();

                X = points.get(testcase_number).getPoints().get(n).getX();
                y_pred = chromosomes.get(i).getChromosome().get(0);
                for (int j = 1; j < number_of_genes; j++) {
                    double addition = 0;
                    double result = round((float) Math.pow(X, j), 3);
                    addition = (double) (chromosomes.get(i).getChromosome().get(j) * result);

                    addition = round(addition, 3);
              
                    y_pred += addition;
              
                }

                mean_squared_error = mean_squared_error + (Math.pow((y_pred - y_actual), 2));
                mean_squared_error = round(mean_squared_error, 3);
            }

            chromosomes.get(i).setError((float) (mean_squared_error / (double) number_of_points));
        }

        return chromosomes;
    }

    public static ArrayList<Chromosome> mutation(ArrayList<Chromosome> chromosomes, float current_generation, float max_genetration) {
        int number_of_genes = chromosomes.get(0).getChromosome().size();
        double y = 0, min = -10, max = 10, current_gene, mutation_value, complex_term1, complex_term2;
        double b = 2.5;
        double mutated = 0, mutation_prob = 0.5;

        for (int i = 0; i < chromosomes.size(); i++) {
           // System.out.println("\n\n" + chromosomes.get(i).getChromosome() + "\n\n");
            for (int j = 0; j < number_of_genes; j++) {
                current_gene = chromosomes.get(i).getChromosome().get(j);
                float rand = (float) Math.random();
                float mutation_rand = (float) Math.random();

                if (true) {
                    if (rand > 0.5) {
                            
                        
                        double rand2 = Math.random();
                        y = max - current_gene;
                        complex_term1 = (double) Math.pow((1 - (double)current_generation / max_genetration), b);

                        complex_term2 = (double) (1 - (Math.pow(rand2, complex_term1)));
                        mutation_value = y * complex_term2;


                        mutation_value = round(mutation_value, 3);
                  
                    
                        
                        mutated = (double) round((current_gene + mutation_value), 3);
                        
                        
                        chromosomes.get(i).getChromosome().set(j, mutated);

                    } else {
                        
                      
                        double rand2 =  Math.random();

                        y = current_gene - min;
                        complex_term1 = (double) Math.pow((1 - (double)current_generation / max_genetration), b);

                        complex_term2 = (double) (1 - (Math.pow(rand2, complex_term1)));
                        mutation_value = y * complex_term2;
                    
                        mutation_value = round(mutation_value, 3);
                   
                        mutated = (double) round(( current_gene - mutation_value), 3);
                        
                        chromosomes.get(i).getChromosome().set(j, mutated);
  
                    }
                }

            }
        }

        return chromosomes;
    }

    public static ArrayList<Chromosome> sort(ArrayList<Chromosome> chromosomes) {
        int n = chromosomes.size();

        // One by one move boundary of unsorted subarray 
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array 
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (chromosomes.get(j).getError() <chromosomes.get(min_idx).getError() )
                {
                    min_idx = j;
                }
            }

            Chromosome temp = chromosomes.get(min_idx);
            chromosomes.set(min_idx, chromosomes.get(i));
            chromosomes.set(i, temp);
            
            
        }
        return chromosomes;
    }
}
