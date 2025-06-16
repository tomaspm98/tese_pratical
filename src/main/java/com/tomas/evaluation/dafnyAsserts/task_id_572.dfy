method RemoveDuplicatesTest(){

  var a1:= new int[] [1,2,3,2,3,4,5];
  var res1:=RemoveDuplicates(a1);
  print(res1);print("\n");
              //expected  [1, 4, 5]

  var a2:= new int[] [1,2,3,2,4,5];
  var res2:=RemoveDuplicates(a2);
  print(res2);print("\n");
              //expected [1, 3, 4, 5]

  var a3:= new int[] [1,2,3,4,5];
  var res3:=RemoveDuplicates(a3);
  print(res3);print("\n");
              //expected [1, 2, 3, 4, 5]

}