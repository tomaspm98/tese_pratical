method RemoveElementTest(){
  var a1:= new int[] [1,1,2,3,4,4,5,1];
  var res1:=RemoveElement(a1,3);
  var exp1:= new int[][1,1,2,4,4,5,1];
  var out1:=assertArrayEquals(exp1,res1);
  expect out1==true;

  var a2:= new int[] [0, 0, 1, 2, 3, 4, 4, 5, 6, 6, 6, 7, 8, 9, 4, 4];
  var res2:=RemoveElement(a2,4);
  var exp2:= new int[] [0,0,1,2,4,4,5,6,6,6,7,8,9,4,4];
  var out2:=assertArrayEquals(exp2,res2);
  expect out2==true;

  var a3:= new int[] [10, 10, 15, 19, 18, 18, 17, 26, 26, 17, 18, 10];
  var res3:=RemoveElement(a3,5);
  var exp3:= new int[][10,10,15,19,18,17,26,26,17,18,10];
  var out3:=assertArrayEquals(exp3,res3);
  expect out3==true;


}

