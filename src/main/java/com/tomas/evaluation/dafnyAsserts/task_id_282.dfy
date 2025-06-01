method ElementWiseSubtractionTest(){
  var a1:= new int[] [1, 2, 3];
  var a2:= new int[] [4,5,6];
  var e1:= new int[] [-3,-3,-3];
  var res1:=ElementWiseSubtraction(a1,a2);
  PrintArray(res1);// expected [-3,-3,-3]

  var a3:= new int[] [1, 2];
  var a4:= new int[] [3,4];
  var e2:= new int[] [-2,-2];
  var res2:=ElementWiseSubtraction(a3,a4);
  PrintArray(res2); // expected [-2,-2]

  var a5:= new int[] [90, 120];
  var a6:= new int[] [50,70];
  var e3:= new int[] [40,50];
  var res3:=ElementWiseSubtraction(a5,a6);
  PrintArray(res3);// expected [40,50]

}