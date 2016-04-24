/**
 * Created by jfcheng on 3/18/16.
 */
public class BinarySearchTest {

    public static void main(String[] args){
        int[] values = {6,7,8,9,10,1,2,3,4,5};
        System.out.println(binarySearchImplement(values,6));
        System.out.println(binarySearchImplement(values,7));
        System.out.println(binarySearchImplement(values,8));
        System.out.println(binarySearchImplement(values,9));
        System.out.println(binarySearchImplement(values,10));
        System.out.println(binarySearchImplement(values,1));
        System.out.println(binarySearchImplement(values,2));
        System.out.println(binarySearchImplement(values,3));
        System.out.println(binarySearchImplement(values,4));
        System.out.println(binarySearchImplement(values,5));
        System.out.println(binarySearchImplement(values,11));
        System.out.println(binarySearchImplement(values,0));
    }

    private static int binarySearchImplement(int[] values, int key){
        return binarySearch(values,0,values.length-1,key);

    }

    private static int binarySearch(int[] values, int l, int h, int key) {
        if(l <= h){
            int mid = l + (h-l)/2;
            int index = orderToIndext(mid);
            int midVal =values[index];
            if(midVal < key){
                return binarySearch(values,mid + 1, h,key);
            }else if(midVal > key){
                return binarySearch(values,l, mid - 1,key);
            }else{
                return index;
            }

        }else{
            return -1;
        }
    }


    private static int orderToIndext(int order){
        if(order < 5){
            return order +5;
        }else{
            return order - 5;
        }
    }

}