method IsProductEvenTest(){

  var a1:= new int[] [1,2,3];
  var out1:=IsProductEven(a1);
  expect out1==true;

  var a2:= new int[] [1,2,1,4];
  var out2:=IsProductEven(a2);
  expect out2==true;

  var a3:= new int[] [1,1];
  var out3:=IsProductEven(a3);
  expect out3==false;

}