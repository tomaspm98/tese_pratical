method FindOddNumbersTest(){
  var a1:= new int[] [1,2,3,4,5,6];
  var res1:=FindOddNumbers(a1);
  print(res1);print("\n");
              //expected [1,3,5]


  var a2:= new int[] [10,11,12,13];
  var res2:=FindOddNumbers(a2);
  print(res2);print("\n");
              //expected [11,13]


  var a3:= new int[] [7,8,9,1];
  var res3:=FindOddNumbers(a3);
  print(res3);print("\n");
              //expected  [7,9,1]


}