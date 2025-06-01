method SharedElementsTest(){
  var a1:= new int[] [3, 4, 5, 6];
  var a2:= new int[] [5, 7, 4, 10];
  var res1:=SharedElements(a1,a2);
  print(res1);print("\n");
              //expected[4, 5];

  var a3:= new int[] [1, 2, 3, 4];
  var a4:= new int[] [5, 4, 3, 7];
  var res2:=SharedElements(a3,a4);
  print(res2);print("\n");
              //expected [3, 4];

  var a5:= new int[] [11, 12, 14, 13];
  var a6:= new int[] [17, 15, 14, 13];
  var res3:=SharedElements(a5,a6);
  print(res3);print("\n");
              //expected [13, 14];
}
