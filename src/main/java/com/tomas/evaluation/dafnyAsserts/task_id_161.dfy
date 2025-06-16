method RemoveElementsTest(){
  var a1:= new int[] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var a2:= new int[] [2, 4, 6, 8];
  var res1:=RemoveElements(a1,a2);
  expect res1==[1, 3, 5, 7, 9, 10];

  var a3:= new int[] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var a4:= new int[] [1, 3, 5, 7];
  var res2:=RemoveElements(a3,a4);
  expect res2==[2, 4, 6, 8, 9, 10];

  var a5:= new int[] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var a6:= new int[] [15, 7];
  var res3:=RemoveElements(a5,a6);
  expect res3==[1, 2, 3, 4, 5, 6, 8, 9, 10];

}