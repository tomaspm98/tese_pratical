method RemoveOddNumbersTest(){

  var a1:= new int[] [1,2,3];
  var res1:=RemoveOddNumbers(a1);
  print(res1);print("\n");
              //expected [2]


  var a2:= new int[] [2,4,6];
  var res2:=RemoveOddNumbers(a2);
  print(res2);print("\n");
              //expected [2,4,6]


  var a3:= new int[] [10,20,3];
  var res3:=RemoveOddNumbers(a3);
  print(res3);print("\n");
              //expected [10,20]

}