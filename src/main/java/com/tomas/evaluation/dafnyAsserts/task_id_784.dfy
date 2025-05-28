method ProductEvenOddTest(){

  var a1:seq<int>:= [1,3,5,7,4,1,6,8];
  var out1:=ProductEvenOdd(a1);
  expect out1==4;

  var a2:seq<int>:= [1,2,3,4,5,6,7,8,9,10];
  var out2:=ProductEvenOdd(a2);
  expect out2==2;

  var a3:seq<int>:= [1,5,7,9,10];
  var out3:=ProductEvenOdd(a3);
  expect out3==10;

}
