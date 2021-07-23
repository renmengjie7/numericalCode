public class ReserveCount {

    public static int getReverseCount(int[][] object) {
        int objectTemp[]=new int[object.length* object.length-1];
        int p=0;
        for (int i = 0; i< object.length; i++){
            for (int j = 0; j< object.length; j++){
                if(object[i][j]!=0){
                    objectTemp[p]=object[i][j];
                    p++;
                }
            }
        }
        return ReserveCount.mergeSort(objectTemp);
    }

    //计算逆序对
    public static int mergeSort(int a[]) {
        int count = 0;
        int n = a.length;
        if (n <= 1) {
            return 0;
        }
        int b[] = new int[n / 2];
        System.arraycopy(a, 0, b, 0, n / 2);
        int c[];
        if (n % 2 == 0) {
            c = new int[n / 2];
            System.arraycopy(a, n / 2, c, 0, n / 2);
        } else {
            c = new int[n / 2 + 1];
            System.arraycopy(a, n / 2, c, 0, n / 2 + 1);
        }

        count += mergeSort(b);
        count += mergeSort(c);
        count += merge(b, c, a);
        return count;
    }

    public static int merge(int b[], int c[], int a[]) {
        int count = 0;      //标志
        int i = 0, j = 0, k = 0;
        int p = b.length, q = c.length;
        while (i < p && j < q) {
            if (b[i] <= c[j]) {
                a[k] = b[i];
                i++;
            } else {
                a[k] = c[j];
                j++;
                count += p - i;     //记录逆序对个数
            }
            k++;
        }
        if (i == p) {
            for (; j < q; j++, k++) {
                a[k] = c[j];
            }
        } else if (j == q) {
            for (; i < p; i++, k++) {
                a[k] = b[i];
            }
        }
        return count;
    }
}
