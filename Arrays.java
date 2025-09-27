/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package arrays;
import javax.swing.*; 

public class Arrays {
 
   
    public static void main(String[] args) {
   
        int [] arr; // Arrays are declared in a int class called "arr"
        
        
        arr = new int [10]; // object ""arr"" instantiated to 10 int values(starts from 0)
        

   // 9.1.2 Entering Data into Array 
         for (int i=0;i<arr.length;i++) //indexed from 0 and ends at index 9. Length property used to get exact number of elements in array
         {
             arr[i]= Integer.parseInt (JOptionPane.showInputDialog("Enter an integer")); // display GUI asking to enter interger array element from 0-9
         }
         // 9.1.3 How to Display Values Stored in Array
         for (int k=0; k<arr.length;k++)//length property used to get exact number of elements in array 
         {
          System.out.println(arr [k]);
         }
         
    }
    
}
