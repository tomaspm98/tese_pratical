method HasCommonElementTest(){
  var a1:= new int[] [1,2,3,4,5];
  var a2:= new int[] [5,6,7,8,9];
  var out1:=HasCommonElement(a1,a2);
  expect out1==true;

  var a3:= new int[] [1,2,3,4,5];
  var a4:= new int[] [6,7,8,9];
  var out2:=HasCommonElement(a3,a4);
  expect out2==false;

  var a5:= new int[] [1,0,1,0];
  var a6:= new int[] [2,0,1];
  var out3:=HasCommonElement(a5,a6);
  expect out3==true;

}

