method SumOfNegativesTest(){

  var a1:= new int[] [2, 4, -6, -9, 11, -12, 14, -5, 17];
  var out1:=SumOfNegatives(a1);
  expect out1==-32;

  var a2:= new int[] [10,15,-14,13,-18,12,-20];
  var out2:=SumOfNegatives(a2);
  expect out2==-52;

  var a3:= new int[] [19, -65, 57, 39, 152,-639, 121, 44, 90, -190];
  var out3:=SumOfNegatives(a3);
  expect out3==-894;

}