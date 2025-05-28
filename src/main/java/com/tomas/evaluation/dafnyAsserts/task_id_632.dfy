method MoveZeroesToEndTest(){
  var a1:= new int[] [1,0,2,0,3,4];
  MoveZeroesToEnd(a1);
  var exp1:= new int[] [1,2,3,4,0,0];
  var out1:=assertArrayEquals(exp1,a1);
  expect out1==true;

  var a2:= new int[] [2,3,2,0,0,4,0,5,0];
  MoveZeroesToEnd(a2);
  var exp2:= new int[] [2,3,2,4,5,0,0,0,0];
  var out2:=assertArrayEquals(exp2,a2);
  expect out2==true;

  var a3:= new int[] [0,1,0,1,1];
  MoveZeroesToEnd(a3);
  var exp3:= new int[] [1,1,1,0,0];
  var out3:=assertArrayEquals(exp3,a3);
  expect out3==true;

}
