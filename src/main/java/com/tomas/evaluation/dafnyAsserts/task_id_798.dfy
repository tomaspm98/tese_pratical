method ArraySumTest(){

  var a1:= new int[] [1, 2, 3];
  var out1:=ArraySum(a1);
  expect out1==6;

  var a2:= new int[] [15, 12, 13, 10];
  var out2:=ArraySum(a2);
  expect out2==50;

  var a3:= new int[] [0, 1, 2];
  var out3:=ArraySum(a3);
  expect out3==3;

}

