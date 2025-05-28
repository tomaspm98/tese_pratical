method LastPositionTest(){

  var a1:= new int[] [1,2,3];
  var out1:=LastPosition(a1,1);
  expect out1==0;

  var a2:= new int[] [1,1,1,2,3,4];
  var out2:=LastPosition(a2,1);
  expect out2==2;

  var a3:= new int[] [2,3,2,3,6,8,9];
  var out3:=LastPosition(a3,3);
  expect out3==3;

}
