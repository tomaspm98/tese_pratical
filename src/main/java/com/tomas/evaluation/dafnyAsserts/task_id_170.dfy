method SumInRangeTest(){

  var a1:= new int[] [2,1,5,6,8,3,4,9,10,11,8,12];
  var out1:=SumInRange(a1,8,11);
  expect out1==29;

  var a2:= new int[] [2,1,5,6,8,3,4,9,10,11,8,12];
  var out2:=SumInRange(a2,5,8);
  expect out2==16;


  var a3:= new int[] [2,1,5,6,8,3,4,9,10,11,8,12];
  var out3:=SumInRange(a3,7,11);
  expect out3==38;

}