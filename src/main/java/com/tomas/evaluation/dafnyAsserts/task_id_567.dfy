method IsSortedTest(){
  var a1:= new int[] [1,2,4,6,8,10,12,14,16,17];
  var out1:=IsSorted(a1);
  expect out1==true;

  var a2:= new int[] [1, 2, 4, 6, 8, 10, 12, 14, 20, 17];
  var out2:=IsSorted(a2);
  expect out2==false;

  var a3:= new int[] [1, 2, 4, 6, 8, 10,15,14,20];
  var out3:=IsSorted(a3);
  expect out3==false;

}
