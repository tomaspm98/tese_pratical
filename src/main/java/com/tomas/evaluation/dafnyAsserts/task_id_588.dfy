method DifferenceMinMaxTest(){
  var a1:= new int[] [1,2,3,4];
  var out1:=DifferenceMinMax(a1);
  expect out1==3;

  var a2:= new int[] [4,5,12];
  var out2:=DifferenceMinMax(a2);
  expect out2==8;

  var a3:= new int[] [9,2,3];
  var out3:=DifferenceMinMax(a3);
  expect out3==7;

}

