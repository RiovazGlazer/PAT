/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arrays;


public class ArrayRandomValues {
     public static void main(String[] args) {
    int [] arra; // Arrays are declared in a int class called "arr"
        
        
         arra = new int [10]; // object ""arr"" instantiated to 10 int values(starts from 0)
        
   
   for (int i=0;i< arra.length;i++) //indexed from 0 and ends at index 9. Length property used to get exact number of elements in array
    {
      arra [i]=(int) (Math.random()*100) +1; //used to assign randim rumber from 1 to 100, +1 used because we wanna start at 1, dont add if u wanna start at 0
    }
    // display values that are stored in array "ArrayRandomValues", across screen with space inbetween 
    System.out.println(arra.length+"random intergers:\n\n"); 
    for (int i=0; i< arra.length;i++)//indexed from 0 and ends at index 9. Length property used to get exact number of elements in array. i++:increments(increasing by 1 to next line) 
    {
        System.out.println(arra[i]+"");// converts integer to string to be visable but adds no characters and is empty adding no space or seperators
    }
     
     
     }
}
