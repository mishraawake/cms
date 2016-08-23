package com.sp.utils;

import com.sp.model.BinaryData;
import com.sp.model.Field;
import com.sp.model.FieldValue;
import org.junit.Assert;

import java.util.Arrays;

/**
 * Created by pankajmishra on 15/08/16.
 */
public class FieldUtils {

    public static void verifyFieldLists(FieldValue[] one, FieldValue[] second) {

        for (FieldValue fieldValue : one) {

            //System.out.println(fieldValue.getField().getName());

            if (fieldValue.getValue() instanceof int[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((int[]) fieldValue
                                .getValue(), (int[]) targetFieldValue.getValue()));
            } else if (fieldValue.getValue() instanceof boolean[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((boolean[])
                                fieldValue.getValue(), (boolean[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof byte[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((byte[])
                                fieldValue.getValue(), (byte[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof char[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((char[])
                                fieldValue.getValue(), (char[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof short[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((short[])
                                fieldValue.getValue(), (short[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof int[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((int[])
                                fieldValue.getValue(), (int[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof long[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((long[])
                                fieldValue.getValue(), (long[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof float[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((float[])
                                fieldValue.getValue(), (float[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof double[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.getField().equals(targetFieldValue.getField()) && Arrays.equals((double[])
                                fieldValue.getValue(), (double[]) targetFieldValue.getValue())));
            } else if (fieldValue.getValue() instanceof FieldValue[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                verifyFieldLists((FieldValue[]) fieldValue.getValue(), (FieldValue[]) targetFieldValue.getValue());
                // System.out.println(fieldValue.equals(getFieldValue(second, fieldValue.getField())));
            } else if (fieldValue.getValue() instanceof BinaryData ) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        ((BinaryData) fieldValue.getValue()).mimeType() .equals(((BinaryData) targetFieldValue.getValue()
                        ).mimeType()));
                // fieldValue.equals(getFieldValue(second, fieldValue.getField())));
                /// do nothing
            } else if(fieldValue.getValue() instanceof BinaryData[]){

                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());

                BinaryData[] targetBinaryData = (BinaryData[])targetFieldValue.getValue();
                BinaryData[] sourceBinaryData = (BinaryData[])fieldValue.getValue();

                for(int binaryIndex = 0; binaryIndex < sourceBinaryData.length; ++binaryIndex){
                    Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                            sourceBinaryData[binaryIndex].mimeType().equals(targetBinaryData[binaryIndex].mimeType()));
                }

            } else if (fieldValue.getValue() instanceof Object[]) {
                FieldValue targetFieldValue = getFieldValue(second, fieldValue.getField());
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())), (Arrays
                        .equals((Object[]) fieldValue.getValue(), (Object[]) targetFieldValue.getValue())));
                // fieldValue.equals(getFieldValue(second, fieldValue.getField())));
            } else {
                Assert.assertTrue(getFieldDetail(fieldValue, getFieldValue(second, fieldValue.getField())),
                        (fieldValue.equals(getFieldValue(second, fieldValue.getField()))));
            }


            //generateBibaries(fieldValue);
        }
    }


    private static FieldValue getFieldValue(FieldValue[] fromList, Field field) {
        for (FieldValue fieldValue : fromList) {
            if (fieldValue.getField().equals(field)) {
                return fieldValue;
            }
        }
        return null;
    }


    private static String getFieldDetail(FieldValue first, FieldValue second) {
        return String.format("do not match, first value is ( %s ) and second value is ( %s )", first, second);
    }
}
