method ReverseUptoKTest(){
  
  var a1:= new int[] [1, 2, 3, 4, 5, 6];
  ReverseUptoK(a1,4);
  var exp1:= new int[] [4, 3, 2, 1, 5, 6];
  var out1:=assertArrayEquals(exp1,a1);
  expect out1==true;

  var a2:= new int[] [4, 5, 6, 7];
  ReverseUptoK(a2,2);
  var exp2:= new int[] [5, 4, 6, 7];
  var out2:=assertArrayEquals(exp2,a2);
  expect out2==true;

  var a3:= new int[] [9, 8, 7, 6, 5];
  ReverseUptoK(a3,3);
  var exp3:= new int[] [7, 8, 9, 6, 5];
  var out3:=assertArrayEquals(exp3,a3);
  expect out3==true;

}


