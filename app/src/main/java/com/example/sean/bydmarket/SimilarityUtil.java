package com.example.sean.bydmarket;

public class SimilarityUtil {
    public static SimilarityUtil mInstance ;

    public static SimilarityUtil getmInstance() {
        if(mInstance == null) {
            synchronized (SimilarityUtil.class) {
                if (mInstance == null) {
                    mInstance = new SimilarityUtil();
                }
            }
        }
        return mInstance;
    }

    public int LowerOfThree(int first, int second, int third) {

        int min = first;
        if(second < min) {
            min = second;
        }

        if(second < min) {
            min = third;
        }

        return min;
    }

    public int Compare_Distance(String str1, String str2) {
        int [] [] martix ;
        int a = str1.length();
        int b = str2.length();
        if(a == 0) {return a;}
        if(b == 0) {return b;}
        martix = new int [a+1] [b+1];

        int temp = 0;
        char ch1;
        char ch2;
        int i = 0;
        int j = 0;

        //初始化二维矩阵边界值
        for(i = 0; i<=a; i++){
            martix[i][0] = i;
        }

        for(j = 0; j<=b; j++) {
            martix[0][j] = j;
        }

        char[] char1 = str1.toCharArray();
        char[] char2 = str2.toCharArray();
        for(i = 1 ; i <=a; i++){
            for(j = 1; j<=b; j++) {
                ch2 = char2[j-1];
                ch1 = char1[i-1];
                if(ch1 == ch2) {
                    martix[i][j] = martix[i-1][j-1];
                } else {
                    martix[i][j] = LowerOfThree(martix[i-1][j],
                            martix[i-1][j-1],martix[i][j-1] )+1;
                }

            }
        }
        return martix[a][b];
    }

}
