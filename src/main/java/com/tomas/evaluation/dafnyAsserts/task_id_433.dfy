method IsGreaterTest(){

  var a1:= new int[] [1, 2, 3, 4, 5];
  var out1:=IsGreater(4,a1);
  expect out1==false;

  var a2:= new int[] [2, 3, 4, 5, 6];
  var out2:=IsGreater(8,a2);
  expect out2==true;

  var a3:= new int[] [9, 7, 4, 8, 6, 1];
  var out3:=IsGreater(11,a3);
  expect out3==true;

}

