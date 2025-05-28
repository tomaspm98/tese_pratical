method RotateRightTest(){

  var a1:seq<int>:= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res1:=RotateRight(a1,3);
  expect res1==[8, 9, 10, 1, 2, 3, 4, 5, 6, 7];

  var a2:seq<int>:= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res2:=RotateRight(a2,2);
  expect res2==[9, 10, 1, 2, 3, 4, 5, 6, 7, 8];

  var a3:seq<int>:= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res3:=RotateRight(a3,5);
  expect res3==[6, 7, 8, 9, 10, 1, 2, 3, 4, 5];

}
