package com.usehover.testerv2.api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AlgorithmsPlay {
    public static int findSingleAppearance(int[] intArray) {
        int singleUnique = -1;
        //Check for bad Array format.
        if(intArray.length == 1) return intArray[0];
        if(intArray.length <= 3) return singleUnique;
        //If format is valid then proceed to check, but sort first.
        Arrays.sort(intArray);
        //Check if lowest number is the unique one.
        if(intArray[0] != intArray[1]) {
           return intArray[0];
        }
        for(int i=0; i<intArray.length; i++) {
            if(intArray[i+1] != -1 && intArray[i+2] !=-1) {
                // Check if the next two are the same e.g 2,2,3. if not, return the third number.
                if (intArray[i + 1] != intArray[i + 2]) {
                    // Numbers are likely 2,2,3,3,3. So check if the 3rd and 4th are the same.
                    if(intArray[i+3] !=-1) {
                        if(intArray[i+2] != intArray[i+3]) {
                            singleUnique = intArray[i+2];
                            break;
                        }
                    }
                    else {
                        singleUnique = intArray[i+2];
                        break;
                    }

                }
            }
        }
        return singleUnique;
    }

    public static int findSingleAppearanceV2(int[] intArray) {
        Map<Integer, Integer>  integerMap= new HashMap<>();
        for (int value : intArray) {

            if(integerMap.containsKey(value)) {
                integerMap.put(value, integerMap.get(value) + 1);
                if(integerMap.get(value) == 3)  integerMap.remove(value);

            } else {
                integerMap.put(value, 1);
            }
        }
        return (int) integerMap.keySet().toArray()[0];
    }
}
