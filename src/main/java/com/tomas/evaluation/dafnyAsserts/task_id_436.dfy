method FindNegativeNumbersTest(){
  var a1:= new int[] [-1,4,5,-6];
  var res1:=FindNegativeNumbers(a1);
  print(res1);print("\n");
              //expected [-1,-6]

  var a2:= new int[] [-1,-2,3,4];
  var res2:=FindNegativeNumbers(a2);
  print(res2);print("\n");
              //expected [-1,-2]

  var a3:= new int[] [-7,-6,8,9];
  var res3:=FindNegativeNumbers(a3);
  print(res3);print("\n");
              //expected [-7,-6]
}