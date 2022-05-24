import java.util.*;
import java.io.*;
import java.lang.*;
public class gaussianInput{
    public static void main(String[] args) throws Exception{
        gaussianInput gi = new gaussianInput();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Please enter the number of equations:");
        int number_of_equations = sc.nextInt();
        float[][] matrix = new float[number_of_equations][number_of_equations+1];
        
        boolean valid_input_selection = false;
        while(valid_input_selection==false){
            System.out.println("Enter 1 if you want to input by console. Enter 2 if by file");
            int input_selection = sc.nextInt();
            sc.nextLine();
            if(input_selection == 1){
                valid_input_selection= true;
                for(int aa =0; aa<matrix.length; aa++){
                    System.out.print("Enter the values of equation "+ aa + " with spaces in between: ");
                    String user_input = sc.nextLine();
                    String[] split_user_input = user_input.split(" ");
                    for(int cc = 0; cc< split_user_input.length; cc++){
                        float user_number = Float.parseFloat(split_user_input[cc]);
                        matrix[aa][cc] =user_number;
                    }
                    sc.nextLine();
                }
                gi.solve(matrix);
            }
            else if(input_selection == 2){
                valid_input_selection= true;
                System.out.println("Please enter the name of your .txt file. You may need to press enter multiple times.");
                String file_path = sc.nextLine();
                System.out.println();
                File file = new File(file_path);
                Scanner fileScanner = new Scanner(file);

                for(int yy = 0; yy<matrix.length;yy++){
                    String scannedString = fileScanner.nextLine();
                    String[] split_user_input = scannedString.split(" ");
                    for(int cc = 0; cc< split_user_input.length; cc++){
                        float user_number = Float.parseFloat(split_user_input[cc]);
                        matrix[yy][cc] = user_number;
                    }
                    sc.nextLine();
                }
                fileScanner.close();
                gi.solve(matrix);
            }
            else{
                System.out.println("Please enter a valid option");
            }
        }
        sc.close();
    }
    public void solve(float[][]matrix){
        HashMap<Integer,Integer> equations_not_to_normalize = new HashMap<Integer,Integer>();
        ArrayList<Integer> indexes_todo = new ArrayList<Integer>();
        Stack<Integer> order_of_operations = new Stack<Integer>();
        int back_sub_index;
        int skip = matrix.length-1;;
        int solution_index = matrix.length-1;
        float sum;
        float right_side;
        float [] scale_vectors = new float[matrix.length];
        //the permanant initial numbers
        float [] scale_factors = new float[matrix.length];
        //the changing ratios with each vertical iteration
        float [] pivot_equation = new float[matrix.length+1];
        //the current pivot equation based on the largest ratio
        float number_used_to_normalize;
        float [] normalized_pivot = new float[matrix.length+1];
        //pivot equation divided by number_used_to_normalize
        float current_largest = 0;
        float inverse;
        float [] inverse_normalized_pivots = new float[matrix.length+1];
        float [] solutions = new float[matrix.length];
        int ll = 0;

        //matrix.length = 2
        //indexes: 0, 1
        //matrix[0].length = 3
        //indexes: 0, 1, 2

        //finding the scale vectors
        for (int i = 0; i < matrix.length; i++){
            current_largest = matrix[i][0];
            for (int j = 0; j< matrix[0].length-1; j++){
                float new_large = Math.abs(matrix[i][j]);
                if (new_large > current_largest){
                    current_largest = new_large;
                }
            }
            scale_vectors[i] = current_largest;
        }
        float largest_ratio = 0;
        float ratio;

        //filling the arraylist/hashset with the indexes of all of the equations
        for (int ArrL = 0; ArrL< matrix.length; ArrL++){
            indexes_todo.add(ArrL);
            equations_not_to_normalize.put(Integer.valueOf(ArrL), Integer.valueOf(ArrL));
        }
        System.out.println("The initial matrix is:");
        for(int matrixPrint = 0; matrixPrint<matrix.length; matrixPrint++){
            for(int matrixPrint2= 0; matrixPrint2<matrix.length+1; matrixPrint2++){
                System.out.print(matrix[matrixPrint][matrixPrint2]+" ");
            }
            System.out.println();
        }
        System.out.println();

        while(!equations_not_to_normalize.isEmpty()){
            ratio = 0;
            Set<Integer> keys = equations_not_to_normalize.keySet();
            System.out.println("The scaled ratios are:");
            for(int mm = 0; mm<matrix.length; mm++){
                if(keys.contains(mm)){
                    ratio = Math.abs(matrix[equations_not_to_normalize.get(mm)][ll]/scale_vectors[equations_not_to_normalize.get(mm)]);
                    System.out.print(ratio +" ");
                    scale_factors[mm] = ratio;
                }   
            }
            largest_ratio =0;
            int largest_ratio_location = 0;
            //int iterator = 0;
            float test_ratio;
            for (int n = 0; n < matrix.length; n++){
                if(keys.contains(n)){
                    test_ratio = scale_factors[equations_not_to_normalize.get(n)];
                    if(test_ratio > largest_ratio){
                        largest_ratio = test_ratio;
                        largest_ratio_location = equations_not_to_normalize.get(n);
                    }
                }
                
            }
            order_of_operations.push(largest_ratio_location);
            equations_not_to_normalize.remove(largest_ratio_location);
            //normalization step
            number_used_to_normalize = matrix[largest_ratio_location][ll];
            System.out.println();
            System.out.println("So, we select pivot row " + largest_ratio_location);
            System.out.println();
            pivot_equation = matrix[largest_ratio_location];
            

            for (int o = 0; o < pivot_equation.length; o ++){
                normalized_pivot[o] = pivot_equation[o]/number_used_to_normalize;
            }

            Set<Integer> keys2 = equations_not_to_normalize.keySet();
            for (int p = 0; p < matrix.length; p++){
                if(keys2.contains(p)){
                    inverse = (matrix[equations_not_to_normalize.get(p)][ll])*-1;
                    for(int q = 0; q < inverse_normalized_pivots.length; q++){
                        inverse_normalized_pivots[q] = normalized_pivot[q]*inverse;
                        matrix[equations_not_to_normalize.get(p)][q] = inverse_normalized_pivots[q] + matrix[equations_not_to_normalize.get(p)][q];
                    }
                }
            }
            System.out.println("The resultant matrix after zeroing the coefficients:");
            for (int a = 0; a < matrix.length; a++){
                for (int b = 0; b< matrix.length+1; b++){
                    System.out.print(matrix[a][b] + " ");
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------");
            ll++;
        }

        for(int stack_size = order_of_operations.size(); stack_size>0; stack_size--){
            back_sub_index = order_of_operations.pop();
            float solution = 0;
            sum = 0;
            right_side = 0;
            for(int c = matrix.length-1; c > -1; c--){
                if(c==skip){
                    continue;
                }
                sum=sum+(matrix[back_sub_index][c])*(solutions[c]);
            }
            right_side = matrix[back_sub_index][matrix.length] + (-1*sum);
            solution = right_side/matrix[back_sub_index][skip];
            solutions[solution_index] = solution;
            skip--;
            solution_index--;
        }
        System.out.println("solutions: ");
        for(int print_solution = 0; print_solution<solutions.length; print_solution++){
            System.out.println("x"+(print_solution+1)+" = "+ solutions[print_solution]);
        }
    }
    
}
