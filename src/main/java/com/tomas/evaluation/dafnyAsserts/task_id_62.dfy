method FindSmallestTest(){
  var a1:= new int[] [10, 20, 1, 45, 99];
  var out1:=FindSmallest(a1);
  expect out1==1;

  var a2:= new int[] [1, 2, 3];
  var out2:=FindSmallest(a2);
  expect out2==1;

  var a3:= new int[] [45, 46, 50, 60];
  var out3:=FindSmallest(a3);
  expect out3==45;
  
}
