/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arrays;

/**
 *
 * @author 27658
 */
import javax.swing.*; 

public class ArrayIndexBoundaries  {
 
   
    public static void main(String[] args) {
   
        int [] arr; // Arrays are declared in a int class called "arr"
        
        
        arr = new int [10]; // object ""arr"" instantiated to 10 int values(starts from 0)
        

   //  Entering Data into Array and Entering boundary
         for (int i=0;i<5;i++) //indexed from 0 and ends at index 5.then till 9 is empty=0, becaause theres a boundary of i<5 and k<10. 
         {
             arr[i]= Integer.parseInt (JOptionPane.showInputDialog("Enter an integer")); // display GUI asking to enter interger array element from 0-9
         }
         //  How to Display Values Stored in Array
         for (int k=0; k<10;k++) //k<10 diffirent from i<5, so after index 5 then the rest of the elements are left empty=0, 
         {
          System.out.println(arr [k]);
         }
         
         
    }
    
}

