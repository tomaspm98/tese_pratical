method FilterOddNumbersTest(){

  var a1:= new int[] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res1:=FilterOddNumbers(a1);
  print(res1);print("\n");
              //expected [1,3,5,7,9]

  var a2:= new int[] [10,20,45,67,84,93];
  var res2:=FilterOddNumbers(a2);
  print(res2);print("\n");
              //expected [45,67,93]

  var a3:= new int[] [5,7,9,8,6,4,3];
  var res3:=FilterOddNumbers(a3);
  print(res3);print("\n");
              //expected [5,7,9,3]

}