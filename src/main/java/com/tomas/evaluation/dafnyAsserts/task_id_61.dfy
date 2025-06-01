method CountSubstringsWithSumOfDigitsEqualToLengthTest() {
    var res1:= CountSubstringsWithSumOfDigitsEqualToLength("112112");
    print(res1);print("\n");
    //assert res1==6;
    var res2:= CountSubstringsWithSumOfDigitsEqualToLength("111");
    print(res2);print("\n");
    //assert res2==6;
    var res3:= CountSubstringsWithSumOfDigitsEqualToLength("1101112");
    print(res3);print("\n");
    //assert res3==12;
}