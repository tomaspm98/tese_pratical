method CountTrueTest(){

  var a1:= new bool[] [true, false, true];
  var out1:=CountTrue(a1);
  expect out1==2;

  var a2:= new bool[] [false, false];
  var out2:=CountTrue(a2);
  expect out2==0;

  var a3:= new bool[] [true, true, true];
  var out3:=CountTrue(a3);
  expect out3==3;


}